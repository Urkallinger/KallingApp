package de.urkallinger.kallingapp.webservice;

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
    public static class LoginData {
        @XmlElement public String username;
        @XmlElement public String password;
    }
}
