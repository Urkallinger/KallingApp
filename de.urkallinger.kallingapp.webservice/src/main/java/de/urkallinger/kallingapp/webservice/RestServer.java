package de.urkallinger.kallingapp.webservice;

import java.io.UnsupportedEncodingException;

import javax.persistence.PersistenceException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.datastructure.Role;
import de.urkallinger.kallingapp.datastructure.User;
import de.urkallinger.kallingapp.webservice.config.Configuration;
import de.urkallinger.kallingapp.webservice.config.ConfigurationManager;
import de.urkallinger.kallingapp.webservice.config.DynDnsConfiguration;
import de.urkallinger.kallingapp.webservice.database.DatabaseHelper;
import de.urkallinger.kallingapp.webservice.database.DbSelect;
import de.urkallinger.kallingapp.webservice.rest.Authentication;
import de.urkallinger.kallingapp.webservice.rest.MotionProvider;
import de.urkallinger.kallingapp.webservice.rest.UserProvider;
import de.urkallinger.kallingapp.webservice.rest.authentication.AuthenticationFilter;
import de.urkallinger.kallingapp.webservice.rest.authentication.Secured;
import de.urkallinger.kallingapp.webservice.rest.authorization.AuthorizationFilter;
import de.urkallinger.kallingapp.webservice.utils.DynDnsUpdater;
import de.urkallinger.kallingapp.webservice.utils.HashBuilder;
import de.urkallinger.kallingapp.webservice.utils.NoDynDnsUpdater;
import de.urkallinger.kallingapp.webservice.utils.NoIpDynDnsUpdater;
import de.urkallinger.kallingapp.webservice.utils.WebUtils;

public class RestServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(RestServer.class);
	private Server jettyServer;

	public RestServer() {
		LOGGER.info("log file path: " + System.getProperty("java.io.tmpdir"));
	}

	public void startServer() throws InterruptedException {
		ConfigurationManager.createOrUpdateConfiguration();

		int port = ConfigurationManager.loadConfiguration().getRestConfig().getServerPort();

		 updateDynDns();

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		jettyServer = new Server(port);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				String.join(",", UserProvider.class.getCanonicalName(), MotionProvider.class.getCanonicalName(),
						Authentication.class.getCanonicalName(), Secured.class.getCanonicalName(),
						AuthenticationFilter.class.getCanonicalName(), AuthorizationFilter.class.getCanonicalName()));

		try {
			createDefaultUser();
			jettyServer.start();
			LOGGER.info(String.format("Server-Address: %s:%d", WebUtils.getPublicIp(), port));
			jettyServer.join();
		} catch (InterruptedException e) {
			LOGGER.warn("Rest server got interrupted.", e);
		} catch (PersistenceException e) {
			LOGGER.error("Rest server could not connect to the database.", e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("An error occured while creating the default user.", e);
		} catch (Exception e) {
			LOGGER.error("An error occurred while starting server.", e);
		} finally {
			LOGGER.info("shutdown server.");
			destroy();
		}
	}

	/**
	 * Creates the default user, if the User table contains no users.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private void createDefaultUser() throws UnsupportedEncodingException {
		DatabaseHelper db = DatabaseHelper.getInstance();
		long userCount = (long) new DbSelect("SELECT COUNT(u) FROM User u").getSingleResult();
		if (userCount == 0) {
			User u = new User();
			u.setUsername("admin");
			u.setPassword("test");
			u.setRole(Role.ADMIN);
			u.setEmail("test@test.de");
			u.setPassword(HashBuilder.sha512(u.getPassword(), Authentication.SALT));
			db.persist(u);
		}
		db.getEntityManager().close();
	}

	private void destroy() {
		while (jettyServer.isStopping()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		if (jettyServer.isStopped()) {
			jettyServer.destroy();
		}
	}

	public void stopServer() throws Exception {
		jettyServer.stop();
	}

	public boolean isStopped() {
		return jettyServer.isStopped();
	}

	private void updateDynDns() throws InterruptedException {
		LOGGER.info("update dynDNS.");
		Thread t = new Thread(getDynDnsUpdater());
		t.setDaemon(true);
		t.start();
		t.join(5000);
	}

	private DynDnsUpdater getDynDnsUpdater() {
		Configuration cfg = ConfigurationManager.loadConfiguration();
		DynDnsConfiguration dnsCfg = cfg.getDynDnsConfig();
		switch(dnsCfg.getProvider()) {
		case NO_IP:
		return new NoIpDynDnsUpdater();
		case NO_DYN_DNS:
			return new NoDynDnsUpdater();
		}
		
		return null;
	}

	public static void main(String[] args) throws Exception {
		RestServer rs = new RestServer();
		rs.startServer();
	}
}