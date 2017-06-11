package de.urkallinger.kallingapp.webservice.rest;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class Param {
    @XmlRootElement
    public static class UserId {
        @XmlElement public Long id = -1L;
    }
    
    @XmlRootElement
    public static class LoginResult {
    	@XmlElement public boolean success = false;
    }
    
    @XmlRootElement
    public static class Credentials implements Serializable {
		private static final long serialVersionUID = 3384089763072709079L;
		@XmlElement public String username;
        @XmlElement public String password;
    }
}
