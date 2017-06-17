package de.urkallinger.kallingapp.datastructure;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token extends DataObject<Token> {

	private static final long serialVersionUID = 1L;

	@JsonProperty("token")
	private String token;
    
    @OneToOne
    private User user;

    @JsonProperty("user")
    public User getUser() {
    	return user;
    }
    
    @JsonProperty("creationDate")
    private Date creationDate = new Date();
    
    @JsonProperty("user")
    public Token setUser(User user) {
    	this.user = user;
        return this;
    }
    
    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public Token setToken(String token) {
        this.token = token;
        return this;
    }

    @JsonProperty("creationDate")
    public Date getCreationDate() {
		return creationDate;
	}

    @JsonProperty("creationDate")
	public Token setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	@Override
	protected Token getThis() {
		return this;
	}
	
    @Override
    public String toString() {
    	return String.format("%s: %s", user.getUsername(), token);
    }
}
