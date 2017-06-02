package de.urkallinger.kallingapp.webservice.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {
	
	private DynDnsConfiguration dynDnsConfig;
	
	public Configuration() {
		this.dynDnsConfig = new DynDnsConfiguration(); 
	}

	public DynDnsConfiguration getDynDnsConfig() {
		return dynDnsConfig;
	}

	public void setDynDnsConfig(DynDnsConfiguration dynDnsConfig) {
		this.dynDnsConfig = dynDnsConfig;
	}
}
