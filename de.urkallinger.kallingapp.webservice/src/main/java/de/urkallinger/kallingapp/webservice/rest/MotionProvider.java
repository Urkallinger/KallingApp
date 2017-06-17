package de.urkallinger.kallingapp.webservice.rest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
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
import de.urkallinger.kallingapp.datastructure.Role;
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;
import de.urkallinger.kallingapp.webservice.rest.authentication.Secured;
import de.urkallinger.kallingapp.webservice.utils.ListUtils;

@Path("kallingapp/motions")
public class MotionProvider {

private final static Logger LOGGER = LoggerFactory.getLogger(MotionProvider.class);
	
	@Context
	private HttpServletRequest request;
	
	@POST
	@Secured({Role.ADMIN, Role.USER})
	@Path("getMotions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Motion> getMotions() {

		LOGGER.info(String.format("new getMotions request (from: %s)", request.getRemoteAddr()));

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
	@Secured({Role.ADMIN, Role.USER})
	@Path("createMotion")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMotion(Motion motion) {

		LOGGER.info(String.format("new createUser request (from: %s)", request.getRemoteAddr()));

		try {
			if (motion.isValid()) {
				return Response.ok().build(); 
			} else {
				LOGGER.error("not valid.");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return Response.status(Response.Status.BAD_REQUEST).build();
	}
}
