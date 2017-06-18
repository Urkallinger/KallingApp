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

import de.urkallinger.kallingapp.datastructure.News;
import de.urkallinger.kallingapp.datastructure.Role;
import de.urkallinger.kallingapp.datastructure.exceptions.ValidationException;
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;
import de.urkallinger.kallingapp.webservice.database.DbSelect;
import de.urkallinger.kallingapp.webservice.database.DbUpdate;
import de.urkallinger.kallingapp.webservice.rest.authentication.Secured;
import de.urkallinger.kallingapp.webservice.utils.ListUtils;

@Path("kallingapp/news")
public class NewsProvider {

private final static Logger LOGGER = LoggerFactory.getLogger(NewsProvider.class);
	
	@Context
	private HttpServletRequest request;
	
	@POST
	@Path("getAllNews")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNews(@Context SecurityContext ctx) {

		LOGGER.info(String.format("new getNews request (user: '%s', ip: %s)",
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			List<News> news;
			news = ListUtils.castList(News.class, new DbSelect("SELECT n FROM News n").getResultList());
			return Response.ok(news).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}

	@POST
	@Path("getNews")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNews(Param.Id input, @Context SecurityContext ctx) {

		LOGGER.info(String.format("new getNews request (user: '%s', ip: %s)", 
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			News news = (News) new DbSelect("SELECT n FROM News n WHERE n.id = :id")
					.addParam("id", input.getId())
					.getSingleResult();
			
			return Response.ok(news).build();
		} catch (NoResultException e) {
			String msg = String.format("No news found with id %d", input.getId());
			LOGGER.error(msg);
			return Response.status(Response.Status.BAD_REQUEST).entity(new Param.Message(msg)).build();
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Param.Message("An internal server error occurred."))
					.build();
		}
	}
	
	@POST
	@Path("createNews")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createNews(News news, @Context SecurityContext ctx) {

		LOGGER.info(String.format("new createNews request (user: '%s', ip: %s)", 
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			news.validate();
			DatabaseHelper dbHelper = DatabaseHelper.getInstance();
			dbHelper.persist(news);
			
			return Response.ok(new Param.Id(news.getId())).build(); 

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
	@Path("deleteNews")
	@Secured({Role.ADMIN, Role.USER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNews(Param.Id input, @Context SecurityContext ctx) {
		LOGGER.info(String.format("new deleteNews request (user: '%s', ip: %s)", 
				ctx.getUserPrincipal().getName(), request.getRemoteAddr()));

		try {
			DbUpdate update = new DbUpdate();
			update.beginTransaction();
			
			int rowCount = update.query("DELETE FROM News n WHERE n.id = :id")
					.addParam("id", input.getId())
					.executeUpdate();
			
			String msg;
			switch(rowCount) {
			case 0:
				update.commitTransation();
				
				msg = String.format("News could not be deleted. No News with id %d found.", input.getId());
				LOGGER.warn(msg);
				return Response.status(Response.Status.BAD_REQUEST).entity(new Param.Message(msg)).build();
			case 1:
				update.commitTransation();
				
				msg = String.format("News with id %d successfully deleted.", input.getId());
				LOGGER.info(msg);
				return Response.ok(new Param.Message(msg)).build();
			default:
				update.rollbackTransation();
				
				msg = String.format("Several news (%d) would have been deleted. A rollback was performed.", 
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
