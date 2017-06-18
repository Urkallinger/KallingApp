package de.urkallinger.kallingapp.webservice.rest.authorization;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.persistence.NoResultException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.datastructure.Role;
import de.urkallinger.kallingapp.datastructure.User;
import de.urkallinger.kallingapp.webservice.database.DbSelect;
import de.urkallinger.kallingapp.webservice.rest.authentication.AuthenticationFilter;
import de.urkallinger.kallingapp.webservice.rest.authentication.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Get the resource class which matches with the requested URL
		// Extract the roles declared by it
		Class<?> resourceClass = resourceInfo.getResourceClass();
		List<Role> classRoles = extractRoles(resourceClass);

		// Get the resource method which matches with the requested URL
		// Extract the roles declared by it
		Method resourceMethod = resourceInfo.getResourceMethod();
		List<Role> methodRoles = extractRoles(resourceMethod);

		try {
			// Check if the user is allowed to execute the method
			// The method annotations override the class annotations
			String user = requestContext.getSecurityContext().getUserPrincipal().getName();
			if (methodRoles.isEmpty()) {
				checkPermissions(classRoles, user);
			} else {
				checkPermissions(methodRoles, user);
			}

		} catch (ForbiddenException e) {
			LOGGER.error(e.getMessage() + String.format(" (Method: '%s')", resourceMethod.getName()));
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
		}
	}

	// Extract the roles from the annotated element
	private List<Role> extractRoles(AnnotatedElement annotatedElement) {
		if (annotatedElement == null) {
			return new ArrayList<Role>();
		} else {
			Secured secured = annotatedElement.getAnnotation(Secured.class);
			if (secured == null) {
				return new ArrayList<Role>();
			} else {
				Role[] allowedRoles = secured.value();
				return Arrays.asList(allowedRoles);
			}
		}
	}

	private void checkPermissions(List<Role> allowedRoles, String username) throws ForbiddenException {
		
		try {
			User user = (User) new DbSelect("SELECT u FROM User u WHERE u.username = :un")
					.addParam("un", username)
					.getSingleResult();
			if(!allowedRoles.contains(user.getRole())) {
				throw new ForbiddenException(String.format("User '%s' is not allowed to use this method.", username));
			}
		} catch (NoResultException e) {
			throw new ForbiddenException(String.format("Cannot find user '%s' in database.", username));
		}
	}
}