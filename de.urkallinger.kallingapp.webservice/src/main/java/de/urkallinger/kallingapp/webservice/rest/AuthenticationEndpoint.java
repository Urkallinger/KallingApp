package de.urkallinger.kallingapp.webservice.rest;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;

@Path("kallingapp")
public class AuthenticationEndpoint {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationEndpoint.class);
	
    @POST
    @Path("authentication")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username, 
                                     @FormParam("password") String password,
                                     @Context HttpServletRequest req) {

        try {
        	LOGGER.info("new authentication request from " + req.getRemoteAddr());
        	
            // Authenticate the user using the credentials provided
            User user = authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(user);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
        	LOGGER.error(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    private User authenticate(String username, String password) throws Exception {
    	DatabaseHelper dbHelper = DatabaseHelper.getInstance();
		EntityManager em = dbHelper.getEntityManager();
		
		Query q = em.createQuery("SELECT u FROM User u WHERE u.username = :un AND u.password = :pw");
		q.setParameter("un", username);
		q.setParameter("pw", password);

		User user = (User) q.getSingleResult(); // wirft exception wenn kein benutzer gefunden wurde
		return user;
    }

    private String issueToken(User user) {
    	DatabaseHelper dbHelper = DatabaseHelper.getInstance();
    	
    	SecureRandom random = new SecureRandom();
    	String token = new BigInteger(130, random).toString(32);
    	Token userToken = new Token()
    			.setToken(token)
    			.setUser(user);
    	
    	dbHelper.persist(userToken);
    	
        return token;
    }
}