package de.urkallinger.kallingapp.webservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.webservice.config.ConfigurationManager;
import de.urkallinger.kallingapp.webservice.utils.WebUtils;

public class RestServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(RestServer.class);
	private Server jettyServer;

	public void startServer() throws InterruptedException {
		ConfigurationManager.createOrUpdateConfiguration();
		
		updateDynDns();
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        jettyServer = new Server(8081);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                DataProvider.class.getCanonicalName());

        try {
            jettyServer.start();
            LOGGER.info("Server-IP: " + WebUtils.getPublicIp());
            jettyServer.join();
        } catch (InterruptedException e) {
        	LOGGER.warn("rest server got interrupted", e);
		} catch (Exception e) {
        	LOGGER.error("an error occurred while starting server", e);
        }
        finally {
        	destroy();
        }
	}
	
	private void destroy() {
		while(jettyServer.isStopping()) {
			try { Thread.sleep(100); } catch(Exception e) {}
		}
		if(jettyServer.isStopped()) {
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
		LOGGER.info("update dynDNS");
		Thread t = new Thread(new NoIpDynDnsUpdater());
		t.setDaemon(true);
		t.start();
		t.join(5000);
	}
	
	public static void main(String[] args) throws Exception {
		RestServer rs = new RestServer();
		rs.startServer();
	}
}