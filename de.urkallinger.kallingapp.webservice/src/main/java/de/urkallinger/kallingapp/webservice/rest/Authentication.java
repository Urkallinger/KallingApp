package de.urkallinger.kallingapp.webservice.rest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.datastructure.Token;
import de.urkallinger.kallingapp.datastructure.User;
import de.urkallinger.kallingapp.datastructure.params.Param;
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;
import de.urkallinger.kallingapp.webservice.database.DbSelect;
import de.urkallinger.kallingapp.webservice.utils.HashBuilder;

@Path("kallingapp")
public class Authentication {

	public final static String SALT = "assilant";
	private final static Logger LOGGER = LoggerFactory.getLogger(Authentication.class);
	
    @POST
    @Path("authentication")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Param.Credentials credentials, @Context HttpServletRequest req) {

        try {
        	LOGGER.info("new authentication request from " + req.getRemoteAddr());
        	
            // Authenticate the user using the credentials provided
            User user = authenticate(credentials.getUsername(), credentials.getPassword());

            // Issue a token for the user
            String token = issueToken(user);

            // Return the token on the response
            return Response.ok(token).build();
        } catch (NoResultException e) {
        	LOGGER.error(String.format("user '%s' could not be authenticated", credentials.getUsername()));
        } catch (Exception e) {
        	LOGGER.error(e.getMessage(), e);
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    private User authenticate(String username, String password) throws Exception {
    	
    	User user = (User) new DbSelect("SELECT u FROM User u WHERE u.username = :un AND u.password = :pw")
    			.addParam("un", username)
				.addParam("pw", HashBuilder.sha512(password, SALT))
				.getSingleResult();
    	
    	return user;
    }

    private String issueToken(User user) {
    	Token userToken;
    	
    	DatabaseHelper dbHelper = DatabaseHelper.getInstance();    	
    	SecureRandom random = new SecureRandom();
    	String token = new BigInteger(130, random).toString(32);
		
		try {
			userToken = (Token) new DbSelect("SELECT t FROM Token t WHERE t.user = :user")
	    			.addParam("user", user)
	    			.getSingleResult();
			userToken.setCreationDate(new Date());
			userToken.setToken(token);
			dbHelper.merge(userToken);
			
		} catch (NoResultException e) {
			userToken = new Token()
					.setToken(token)
	    			.setUser(user);
	    	dbHelper.persist(userToken);
		}

		return userToken.getToken();
    }
}