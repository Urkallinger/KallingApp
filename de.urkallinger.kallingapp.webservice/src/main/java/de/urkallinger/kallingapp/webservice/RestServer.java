package de.urkallinger.kallingapp.webservice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestServer {

	private final Logger LOGGER = LoggerFactory.getLogger(RestServer.class);
	private Server jettyServer;

	public void startServer() throws InterruptedException {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                DataProvider.class.getCanonicalName());

        try {
            jettyServer.start();
            LOGGER.info("Server-IP: " + getPublicIp());
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

	private String getPublicIp() throws Exception {
		try (InputStream is = new URL("http://checkip.amazonaws.com").openStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String ip = br.readLine();
			return ip;
		}
	}

	public static void main(String[] args) throws Exception {
		RestServer rs = new RestServer();
		rs.startServer();
	}
}