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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.datastructure.Motion;
import de.urkallinger.kallingapp.datastructure.User;
import de.urkallinger.kallingapp.webservice.Param.GetUserParam;
import de.urkallinger.kallingapp.webservice.Param.LoginParam;
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;
import de.urkallinger.kallingapp.webservice.utils.ListUtils;

@Path("kallingapp")
public class DataProvider {
	private final Logger LOGGER = LoggerFactory.getLogger(DataProvider.class);

	@Context
	private HttpServletRequest hsr;

	public DataProvider() {}

	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResult login(final LoginParam input) {

		LOGGER.info("new login request for user " + input.username + " (from: " + getClientIp() + ")");

		LoginResult result = new LoginResult("login");
		EntityManager em = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			em = dbHelper.getEntityManager();

			Query q = em.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.username = :un AND u.password = :pw");
			q.setParameter("un", input.username);
			q.setParameter("pw", input.password);

			long count = (long) q.getSingleResult();
			result.setSuccess(count > 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return result;
	}

	@POST
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
	@Path("createUser")
	@Produces(MediaType.APPLICATION_JSON)
	public String createUser(User user) {

		LOGGER.info("new createUser request (from: " + getClientIp() + ")");
		
		LOGGER.info("ID: " + user.getId());
		LOGGER.info("Username: " + user.getUsername());
		LOGGER.info("Password: " + user.getPassword());

		EntityManager em = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			dbHelper.persist(user);
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return "SUCCESS";
	}

	@POST
	@Path("getUser")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(final GetUserParam input) {

		LOGGER.info("new getUser for user ID " + input.id + " (from: " + getClientIp() + ")");

		User user = User.DummyUser();
		EntityManager em = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			em = dbHelper.getEntityManager();
			Query q = em.createQuery("SELECT u FROM User u WHERE u.id = :id");
			q.setParameter("id", input.id);

			user = (User) q.getSingleResult();
		} catch (NoResultException e) {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return user;
	}

	private String getClientIp() {
		return hsr.getRemoteAddr();
	}

	static class LoginResult {
		boolean success = false;
		String action;

		public LoginResult() {
		}

		public LoginResult(String action) {
			this.action = action;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public boolean getSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}
	}
}
