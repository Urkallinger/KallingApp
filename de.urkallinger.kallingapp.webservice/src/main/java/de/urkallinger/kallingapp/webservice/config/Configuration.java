package de.urkallinger.kallingapp.webservice.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {
	
	private DynDnsConfiguration dynDnsConfig;
	private RestConfiguration restConfig;
	
	public Configuration() {
		this.dynDnsConfig = new DynDnsConfiguration();
		this.restConfig = new RestConfiguration();
	}

	public DynDnsConfiguration getDynDnsConfig() {
		return dynDnsConfig;
	}

	public void setDynDnsConfig(DynDnsConfiguration dynDnsConfig) {
		this.dynDnsConfig = dynDnsConfig;
	}

	public RestConfiguration getRestConfig() {
		return restConfig;
	}

	public void setRestConfig(RestConfiguration restConfig) {
		this.restConfig = restConfig;
	}
}
