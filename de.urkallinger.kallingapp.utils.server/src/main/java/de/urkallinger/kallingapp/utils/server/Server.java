package de.urkallinger.kallingapp.utils.server;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.webservice.RestServer;

public class Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

	private static Server instance = new Server();

	private RestServer server = new RestServer();
	private Optional<Thread> serverThread = Optional.empty();

	private Server() {
		// Singleton
	}

	public static Server getInstance() {
		return instance;
	}

	public void start() {
		if(isServerThreadAlive()) {
			LOGGER.warn("Server is already started");
			return;
		}

		Runnable run = () -> {
			try {
				LOGGER.info("start rest server");
				server.startServer();
			} catch (Exception e) {
				LOGGER.error("failed to start rest server", e);
			}
		};
		serverThread = Optional.of(new Thread(run));
		serverThread.get().setDaemon(true);
		serverThread.get().start();
	}

	public void stop() {
		if(!isServerThreadAlive()) return;
		
		Runnable run = () -> {
			LOGGER.info("stop rest server");
			try {
				server.stopServer();
			} catch (Exception e) {
				LOGGER.error("failed to stop rest server", e);
			}
		};

		Thread t = new Thread(run);
		t.setDaemon(true);
		t.start();
	}

	public boolean isServerThreadAlive() {
		if (serverThread.isPresent()) {
			return serverThread.get().isAlive();
		}

		return false;
	}

	public void restart() {
		if(isServerThreadAlive()) {
			stop();
			while(!server.isStopped()) {
				try { Thread.sleep(100); } catch(Exception e) {}
			}
			while(serverThread.get().isAlive()) {
				try { Thread.sleep(100); } catch(Exception e) {}
			}
			start();
		}
	}
}
