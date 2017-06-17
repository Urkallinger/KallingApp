package de.urkallinger.kallingapp.datastructure;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "title",
        "description",
        "createdBy",
        "createdAt"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Motion extends DataObject<Motion> {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("title")
    private String title;
	@Lob
    @Column
    @JsonProperty("description")
    private String description;
    @JsonProperty("createdBy")
    @OneToOne
    private User createdBy;
    @JsonProperty("createdAt")
    private Date createdAt;

    public Motion() {}

    public Motion(String title, String description, User createdBy, Date createdAt) {
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }


    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public Motion setTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public Motion setDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("createdBy")
    public User getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public Motion setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @JsonProperty("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public Motion setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    @Override
    protected Motion getThis() {
    	return this;
	}
}
