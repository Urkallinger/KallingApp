package de.urkallinger.kallingapp.webservice.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Param {
    public static class Message {
    	private String msg = "";
		
    	public Message() {}
    	
		public Message(String msg) {
			this.msg = msg;
		}

		@JsonProperty("msg")
		public String getMsg() {
			return msg;
		}

		@JsonProperty("msg")
		public void setMsg(String msg) {
			this.msg = msg;
		}
    }
	
    public static class Id {
    	private Long id = -1L;

    	public Id() {}
    	
    	public Id(long id) {
    		this.id = id;
		}
    	
    	@JsonProperty("id")
		public Long getId() {
			return id;
		}

    	@JsonProperty("id")
		public void setId(Long id) {
			this.id = id;
		}
    }
    
    public static class Credentials {
    	private String username;
    	private String password;
        
    	public Credentials() {
		}
    	
        public Credentials(String username, String password) {
        	this.username = username;
        	this.password = password;
		}

        @JsonProperty("username")
		public String getUsername() {
			return username;
		}

        @JsonProperty("username")
		public void setUsername(String username) {
			this.username = username;
		}

        @JsonProperty("password")
		public String getPassword() {
			return password;
		}

        @JsonProperty("password")
		public void setPassword(String password) {
			this.password = password;
		}
    }
}
