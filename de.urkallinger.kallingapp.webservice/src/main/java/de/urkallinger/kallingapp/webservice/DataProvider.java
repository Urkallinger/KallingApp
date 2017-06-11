package de.urkallinger.kallingapp.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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

import de.urkallinger.kallingapp.datastructure.Motion;
import de.urkallinger.kallingapp.datastructure.User;
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;
import de.urkallinger.kallingapp.webservice.rest.Authentication;
import de.urkallinger.kallingapp.webservice.rest.Param.UserId;
import de.urkallinger.kallingapp.webservice.rest.filter.Secured;
import de.urkallinger.kallingapp.webservice.utils.HashBuilder;
import de.urkallinger.kallingapp.webservice.utils.ListUtils;

@Path("kallingapp")
public class DataProvider {
	private final static Logger LOGGER = LoggerFactory.getLogger(DataProvider.class);

	@Context
	private HttpServletRequest hsr;

	@POST
	@Secured
	@Path("getMotions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Motion> getMotions() {

		LOGGER.info("new getMotions request (from: " + getClientIp() + ")");

		List<Motion> motions = new ArrayList<>();
		EntityManager em = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			em = dbHelper.getEntityManager();
			Query q = em.createQuery("SELECT m FROM Motion m");
			motions = ListUtils.castList(Motion.class, q.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return motions;
	}

	@POST
	@Secured
	@Path("getUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {

		LOGGER.info("new getUsers request (from: " + getClientIp() + ")");

		List<User> users = new ArrayList<>();
		EntityManager em = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			em = dbHelper.getEntityManager();
			Query q = em.createQuery("SELECT u FROM User u");
			users = ListUtils.castList(User.class, q.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return users;
	}

	@POST
	@Secured
	@Path("createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User user) {

		LOGGER.info(String.format("new createUser request (from: %s)", getClientIp()));

		UserId userId = new UserId();
		try {
			if (user.isValid()) {
				user.setPassword(HashBuilder.sha512(user.getPassword(), Authentication.SALT));
				DatabaseHelper dbHelper = DatabaseHelper.getInstance();
				dbHelper.persist(user);
				userId.id = user.getId();
				return Response.ok(userId).build(); 
			} else {
				LOGGER.error("user is not valid.");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	@POST
	@Path("getUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured
	public Response getUser(final UserId input) {

		EntityManager em = null;
		try {
			LOGGER.info("new getUser for user ID " + input.id + " (from: " + getClientIp() + ")");
			
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			em = dbHelper.getEntityManager();
			Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :id");
			q.setParameter("id", input.id);

			User user = (User) q.getSingleResult();
			return Response.ok(user).build();
		} catch (NoResultException e) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		// TODO: Response status -> was nimmt man da?!
		return Response.status(Response.Status.CONFLICT).build();
	}

	private String getClientIp() {
		return hsr.getRemoteAddr();
	}
}