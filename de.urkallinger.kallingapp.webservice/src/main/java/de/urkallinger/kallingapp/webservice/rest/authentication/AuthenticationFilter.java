package de.urkallinger.kallingapp.webservice.rest.authentication;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;

import javax.annotation.Priority;
import javax.persistence.NoResultException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.datastructure.Token;
import de.urkallinger.kallingapp.webservice.config.ConfigurationManager;
import de.urkallinger.kallingapp.webservice.config.RestConfiguration;
import de.urkallinger.kallingapp.webservice.database.DbQuery;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

	private Duration tokenDurability;

	public AuthenticationFilter() {
		RestConfiguration cfg = ConfigurationManager.loadConfiguration().getRestConfig();
		int period = cfg.getTokenDurabilityPeriod();
		if(period <= 0) {
			LOGGER.warn("Token durability period is set to zero. no authentication is possible.");
		}
		tokenDurability = Duration.of(period, cfg.getTokenDurabilityUnit());
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		LOGGER.info("authenticate user...");

		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		try {
			// Check if the HTTP Authorization header is present and formatted
			// correctly
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				throw new NotAuthorizedException("Authorization header must be provided");
			}

			// Extract the token from the HTTP Authorization header
			String token = authorizationHeader.substring("Bearer".length()).trim();

			// Validate the token
			Token tok = validateToken(token);

			final SecurityContext currSecCtx = requestContext.getSecurityContext();
			requestContext.setSecurityContext(createSecurityContext(currSecCtx, tok.getUser().getUsername()));

		} catch (NoResultException e) {
			LOGGER.error("user could not be authenticated because of an invalid token");
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		} catch (NotAuthorizedException e) {
			LOGGER.error(e.getMessage());
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}

	private Token validateToken(String token) throws Exception {
		Token tok = (Token) new DbQuery("SELECT t FROM Token t WHERE t.token = :token")
				.addParam("token", token)
				.getSingleResult();

		Instant creationDate = tok.getCreationDate().toInstant();
		if (!creationDate.plus(tokenDurability).isAfter(Instant.now())) {
			throw new NotAuthorizedException("Could not authorize user because token is expired.",
					Response.Status.UNAUTHORIZED);
		}

		LOGGER.info(String.format("user '%s' used '%s' as authentication token",
				tok.getUser().getUsername(),
				tok.getToken()));

		return tok;
	}

	private SecurityContext createSecurityContext(final SecurityContext currentContext, final String username) {
		return new SecurityContext() {

			@Override
			public Principal getUserPrincipal() {

				return new Principal() {

					@Override
					public String getName() {
						return username;
					}
				};
			}

			@Override
			public boolean isUserInRole(String role) {
				return true;
			}

			@Override
			public boolean isSecure() {
				return currentContext.isSecure();
			}

			@Override
			public String getAuthenticationScheme() {
				return "Bearer";
			}
		};
	}
}