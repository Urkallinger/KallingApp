package de.urkallinger.kallingapp.webservice.config;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationManager {

	private final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);
	
	private static final String CONFIG_XML = "config.xml";
	
	private ConfigurationManager() {
	}
	
	public static boolean configurationExists() {
		return new File(CONFIG_XML).exists();
	}
	
	public static Configuration loadConfiguration() {
		File cfgFile = new File(CONFIG_XML);
		Configuration config = null;
		if (cfgFile.exists()) {
			try {
				JAXBContext context = JAXBContext.newInstance(Configuration.class);
				Unmarshaller m = context.createUnmarshaller();
				config = (Configuration) m.unmarshal(cfgFile);
			} catch (JAXBException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		if(config == null) config = new Configuration();
		return config;
	}
	
	public static void saveConfiguration(Configuration cfg) {
		File cfgFile = new File(CONFIG_XML);
		try {
			if(!cfgFile.exists()) cfgFile.createNewFile();
			
			JAXBContext context = JAXBContext.newInstance(Configuration.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(cfg, cfgFile);
		} catch (JAXBException | IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public static void createNewConfiguration() {
		saveConfiguration(new Configuration());
	}
	
	public static void createOrUpdateConfiguration() {
		if(!configurationExists()) {
			createNewConfiguration();
		} else {
			Configuration cfg = loadConfiguration();
			saveConfiguration(cfg);
		}
	}
}
