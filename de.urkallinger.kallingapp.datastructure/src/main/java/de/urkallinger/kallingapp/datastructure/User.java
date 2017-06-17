package de.urkallinger.kallingapp.datastructure;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.urkallinger.kallingapp.datastructure.annotations.Required;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends DataObject<User> {

	private static final long serialVersionUID = 1L;

	@Required
	@Column(unique=true, nullable=false)
	@JsonProperty("username")
	private String username;

	@Required
	@Column(nullable=false)
	@JsonProperty("password")
	private String password;

	@JsonProperty("email")
	private String email;

	@Required
	@Column(nullable=false)
	@JsonProperty("role")
	private Role role;

	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	@JsonProperty("username")
	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	@JsonProperty("password")
	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	@JsonProperty("role")
	public Role getRole() {
		return role;
	}

	@JsonProperty("role")
	public User setRole(Role role) {
		this.role = role;
		return this;
	}

	@Override
	protected User getThis() {
		return this;
	}

	@Override
	public String toString() {
		return username;
	}
}
