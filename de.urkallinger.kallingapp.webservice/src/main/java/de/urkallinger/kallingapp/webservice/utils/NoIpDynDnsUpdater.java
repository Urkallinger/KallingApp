package de.urkallinger.kallingapp.webservice.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.urkallinger.kallingapp.webservice.config.Configuration;
import de.urkallinger.kallingapp.webservice.config.ConfigurationManager;
import de.urkallinger.kallingapp.webservice.config.DynDnsConfiguration;
import de.urkallinger.kallingapp.webservice.exceptions.InvalidConfigurationException;

public class NoIpDynDnsUpdater implements Runnable {

	private final static Logger LOGGER = LoggerFactory.getLogger(NoIpDynDnsUpdater.class);

	void updateDynDNS() throws Exception {
		Configuration cfg = ConfigurationManager.loadConfiguration();
		DynDnsConfiguration dnsCfg = cfg.getDynDnsConfig();
		String hostIp = WebUtils.getPublicIp();

		try {
			validateConfiguration(dnsCfg);
			
			if(dnsCfg.getHostIp().equals(hostIp)) {
				LOGGER.info("IP address is current, no update necessary.");
				return;
			}
			
			// Encode username and password
			String userpassword = dnsCfg.getUsername() + ":" + dnsCfg.getPassword();
			String encodedAuthorization = new String(Base64.getEncoder().encode(userpassword.getBytes()));

			// Connect to DynDNS
			String spec = String.format("http://%s:%s@dynupdate.no-ip.com/nic/update?hostname=%s&myip=%s", 
					dnsCfg.getUsername(), dnsCfg.getPassword(), dnsCfg.getHostName(), hostIp);
			
			URL url = new URL(spec);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "DynDnsUpdater");
			connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

			// Execute GET
			int responseCode = connection.getResponseCode();
			String responseMsg = connection.getResponseMessage();
			LOGGER.info(String.format("response from dynDNS server %d:%s", responseCode, responseMsg));

			// Update configuration
			dnsCfg.setHostIp(hostIp);
			cfg.setDynDnsConfig(dnsCfg);
			ConfigurationManager.saveConfiguration(cfg);
			
			// Print feedback
			InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
			BufferedReader buff = new BufferedReader(in);

			String response = buff.lines().filter(l -> l != null && !l.isEmpty()).collect(Collectors.joining());
			interpretNoIpResponse(response);

			connection.disconnect();
		} catch (InvalidConfigurationException e) {
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	private void interpretNoIpResponse(String response) {
		if (response.contains("good")) {
			LOGGER.info("DNS hostname update successful.");
			return;
		}
		if (response.contains("nochg")) {
			LOGGER.info("IP address is current, no update performed.");
			return;
		}
		if (response.contains("nohost")) {
			LOGGER.error("Hostname supplied does not exist under specified account, "
					+ "client exit and require user to enter new login credentials "
					+ "before performing an additional request.");
			return;
		}
		if (response.contains("badauth")) {
			LOGGER.error("Invalid username password combination");
			return;
		}
		if (response.contains("badagent")) {
			LOGGER.error("Client disabled. Client should exit and not perform any more "
					+ "updates without user intervention.");
			return;
		}
		if (response.contains("!donator")) {
			LOGGER.error("An update request was sent including a feature that is not "
					+ "available to that particular user such as offline options.");
			return;
		}
		if (response.contains("abuse")) {
			LOGGER.error("Username is blocked due to abuse. Either for not following "
					+ "our update specifications or disabled due to violation of the "
					+ "No-IP terms of service.");
			return;
		}
		if (response.contains("911")) {
			LOGGER.error("A fatal error on our side such as a database outage. "
					+ "Retry the update no sooner than 30 minutes.");
			return;
		}
	}
	
	private void validateConfiguration(DynDnsConfiguration cfg) throws Exception {
		if(cfg.getUsername() == null || "".equals(cfg.getUsername())) {
			throw new InvalidConfigurationException("DynDNS update failed. No username defined in configuration.");
		}
		if(cfg.getPassword() == null || "".equals(cfg.getPassword())) {
			throw new InvalidConfigurationException("DynDNS update failed. No password defined in configuration.");
		}
		if(cfg.getHostName() == null || "".equals(cfg.getHostName())) {
			throw new InvalidConfigurationException("DynDNS update failed. No hostname defined in configuration.");
		}
	}

	@Override
	public void run() {
		try {
			updateDynDNS();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
