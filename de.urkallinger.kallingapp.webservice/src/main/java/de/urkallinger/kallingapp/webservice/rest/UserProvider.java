package de.urkallinger.kallingapp.webservice.rest;

import java.util.List;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.datastructure.Role;
import de.urkallinger.kallingapp.datastructure.User;
import de.urkallinger.kallingapp.datastructure.exceptions.ValidationException;
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;
import de.urkallinger.kallingapp.webservice.database.DbQuery;
import de.urkallinger.kallingapp.webservice.rest.Param.Id;
import de.urkallinger.kallingapp.webservice.rest.authentication.Secured;
import de.urkallinger.kallingapp.webservice.utils.HashBuilder;
import de.urkallinger.kallingapp.webservice.utils.ListUtils;

@Path("kallingapp/users")
public class UserProvider {

	private final static Logger LOGGER = LoggerFactory.getLogger(UserProvider.class);
	
	@Context
	private HttpServletRequest request;
	
	@POST
	@Path("createUser")
	@Secured({Role.ADMIN})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User user, @Context SecurityContext ctx) {

		LOGGER.info(String.format("new createUser request (user: '%s', ip: %s)",
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			user.validate();
			user.setPassword(HashBuilder.sha512(user.getPassword(), Authentication.SALT));
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			dbHelper.persist(user);
			return Response.ok(new Param.Id(user.getId())).build();
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(new Param.Message(e.getMessage())).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}
	
	@POST
	@Path("getUsers")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@Context SecurityContext ctx) {

		LOGGER.info(String.format("new getUsers request (user: '%s', ip: %s)", 
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			List<User> users;
			users = ListUtils.castList(User.class, new DbQuery("SELECT u FROM Users u").getResultList());
			return Response.ok(users).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}
	
	@POST
	@Path("getUser")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(final Id input, @Context SecurityContext ctx) {

		try {
			LOGGER.info(String.format("new getUser request for user ID %d (user: '%s', ip: %s)", 
					input.getId(), ctx.getUserPrincipal().getName(), request.getRemoteAddr()));
			
			User user = (User) new DbQuery("SELECT u FROM User u WHERE u.id = :id")
									.addParam("id", input.getId())
									.getSingleResult();
			
			return Response.ok(user).build();
		} catch (NoResultException e) {
			String msg = String.format("No user found with id %d", input.getId());
			LOGGER.error(msg);
			return Response.status(Response.Status.BAD_REQUEST).entity(msg).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}
}
