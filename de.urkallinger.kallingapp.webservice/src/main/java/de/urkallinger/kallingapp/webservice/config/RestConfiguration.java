package de.urkallinger.kallingapp.webservice.config;

import java.time.temporal.ChronoUnit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "rest")
@XmlAccessorType(XmlAccessType.FIELD)

public class RestConfiguration {

	@XmlTransient
	private final static Logger LOGGER = LoggerFactory.getLogger(RestConfiguration.class);
	
	private int tokenDurabilityPeriod;
	private String tokenDurabilityUnit;

	public RestConfiguration() {
		this.tokenDurabilityPeriod = 1;
		this.tokenDurabilityUnit = ChronoUnit.HOURS.toString().toUpperCase();
	}

	public int getTokenDurabilityPeriod() {
		return tokenDurabilityPeriod;
	}

	public void setTokenDurabilityPeriod(int tokenDurabilityPeriod) {
		this.tokenDurabilityPeriod = tokenDurabilityPeriod;
	}

	public ChronoUnit getTokenDurabilityUnit() {
		try {
			return ChronoUnit.valueOf(tokenDurabilityUnit);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Invalid token durability unit in configuration. switching to default: HOURS.");
			return ChronoUnit.HOURS;
		}
	}

	public void setTokenDurabilityUnit(ChronoUnit unit) {
		this.tokenDurabilityUnit = unit.toString().toUpperCase();
	}

}
