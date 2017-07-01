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

import de.urkallinger.kallingapp.datastructure.annotations.Required;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "title",
        "description",
        "creator",
        "creationDate"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Motion extends DataObject<Motion> {
	
	private static final long serialVersionUID = 1L;
	
	@Required
	@Column(nullable=false)
	@JsonProperty("title")
    private String title;
	
	@Required
	@Lob
    @Column
    @JsonProperty("description")
    private String description;
    
	@OneToOne
	@JsonProperty("creator")
    private User creator;
    
	@Column(nullable=false)
	@JsonProperty("creationDate")
    private Date creationDate;

    public Motion() {}

    public Motion(String title, String description, User creator, Date creationDate) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.creationDate = creationDate;
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

    @JsonProperty("creator")
    public User getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public Motion setCreator(User creator) {
        this.creator = creator;
        return this;
    }

    @JsonProperty("creationDate")
    public Date getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creationDate")
    public Motion setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }
    
    @Override
    protected Motion getThis() {
    	return this;
	}
}
