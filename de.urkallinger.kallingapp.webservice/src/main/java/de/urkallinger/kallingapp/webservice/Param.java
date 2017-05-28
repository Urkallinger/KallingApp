package de.urkallinger.kallingapp.webservice;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class Param {
    @XmlRootElement
    public static class GetUserParam {
        @XmlElement public Long id;
    }
    
    @XmlRootElement
    public static class LoginParam {
        @XmlElement public String username;
        @XmlElement public String password;
    }
}
