package de.urkallinger.kallingapp.webservice.rest;

import java.util.List;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.datastructure.Motion;
import de.urkallinger.kallingapp.datastructure.Role;
import de.urkallinger.kallingapp.datastructure.exceptions.ValidationException;
import de.urkallinger.kallingapp.datastructure.params.Param;
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;
import de.urkallinger.kallingapp.webservice.database.DbSelect;
import de.urkallinger.kallingapp.webservice.database.DbUpdate;
import de.urkallinger.kallingapp.webservice.rest.authentication.Secured;
import de.urkallinger.kallingapp.webservice.utils.ListUtils;

@Path("kallingapp/motions")
public class MotionProvider {

private final static Logger LOGGER = LoggerFactory.getLogger(MotionProvider.class);
	
	@Context
	private HttpServletRequest request;
	
	@POST
	@Path("getMotions")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMotions(@Context SecurityContext ctx) {

		LOGGER.info(String.format("new getMotions request (user: '%s', ip: %s)",
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			List<Motion> motions;
			motions = ListUtils.castList(Motion.class, new DbSelect("SELECT m FROM Motion m").getResultList());
			return Response.ok(motions).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}

	@POST
	@Path("getMotion")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMotion(Param.Id input, @Context SecurityContext ctx) {

		LOGGER.info(String.format("new getMotion request (user: '%s', ip: %s)", 
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			Motion motion = (Motion) new DbSelect("SELECT m FROM Motion m WHERE m.id = :id")
					.addParam("id", input.getId())
					.getSingleResult();
			
			return Response.ok(motion).build();
		} catch (NoResultException e) {
			String msg = String.format("No motion found with id %d", input.getId());
			LOGGER.error(msg);
			return Response.status(Response.Status.BAD_REQUEST).entity(new Param.Message(msg)).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}
	
	@POST
	@Path("createMotion")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMotion(Motion motion, @Context SecurityContext ctx) {

		LOGGER.info(String.format("new createMotion request (user: '%s', ip: %s)", 
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			motion.validate();
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			dbHelper.persist(motion);
			
			return Response.ok(new Param.Id(motion.getId())).build(); 

		} catch (ValidationException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(new Param.Message(e.getMessage())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}
	
	@DELETE
	@Path("deleteMotion")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMotion(Param.Id input, @Context SecurityContext ctx) {
		LOGGER.info(String.format("new deleteMotion request (user: '%s', ip: %s)", 
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			DbUpdate update = new DbUpdate();
			update.beginTransaction();
			
			int rowCount = update.query("DELETE FROM Motion m WHERE m.id = :id")
					.addParam("id", input.getId())
					.executeUpdate();
			
			String msg;
			switch(rowCount) {
			case 0:
				update.commitTransation();
				
				msg = String.format("Motion could not be deleted. No motion with id %d found.", input.getId());
				LOGGER.warn(msg);
				return Response.status(Response.Status.BAD_REQUEST).entity(new Param.Message(msg)).build();
			case 1:
				update.commitTransation();
				
				msg = String.format("Motion with id %d successfully deleted.", input.getId());
				LOGGER.info(msg);
				return Response.ok(new Param.Message(msg)).build();
			default:
				update.rollbackTransation();
				
				msg = String.format("Several motions (%d) would have been deleted. A rollback was performed.", 
						rowCount);
				LOGGER.error(msg);
				return Response.status(Response.Status.BAD_REQUEST).entity(new Param.Message(msg)).build();
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}
}
