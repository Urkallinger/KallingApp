package de.urkallinger.kallingapp.datastructure;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends DataObject<User> {

	private static final long serialVersionUID = 1L;

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;

	@JsonProperty("email")
	private String email;

	@JsonProperty("role")
	private Role role;

	@JsonProperty("username")
	@Column(unique = true)
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

	public boolean isValid() {
		return username != null && !username.isEmpty() && password != null && !password.isEmpty() && email != null
				&& !email.isEmpty();
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
