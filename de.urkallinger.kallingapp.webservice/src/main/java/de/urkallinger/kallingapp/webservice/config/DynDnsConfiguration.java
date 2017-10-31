package de.urkallinger.kallingapp.webservice.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dyndns")
@XmlAccessorType(XmlAccessType.FIELD)

public class DynDnsConfiguration {

	private DynDnsProvider provider;
	private String hostName;
	private String hostIp;
	private String username;
	private String password;

	public DynDnsConfiguration() {
		this.provider = DynDnsProvider.NO_DYN_DNS;
		this.hostName = "";
		this.hostIp = "";
		this.username = "";	
		this.password = "";
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DynDnsProvider getProvider() {
		return provider;
	}

	public void setProvider(DynDnsProvider provider) {
		this.provider = provider;
	}
}
