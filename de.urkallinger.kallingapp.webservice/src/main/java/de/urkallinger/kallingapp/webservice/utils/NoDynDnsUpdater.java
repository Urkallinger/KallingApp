package de.urkallinger.kallingapp.webservice.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoDynDnsUpdater implements DynDnsUpdater {

	private final static Logger LOGGER = LoggerFactory.getLogger(NoDynDnsUpdater.class);
	
	@Override
	public void updateDynDns() throws IOException {
	}

	@Override
	public void run() {
		LOGGER.info("No DynDNS has been configured.");
	}

}
