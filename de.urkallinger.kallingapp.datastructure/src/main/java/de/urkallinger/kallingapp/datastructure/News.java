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
        "message",
        "creator",
        "creationDate"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class News extends DataObject<News> {
	
	private static final long serialVersionUID = 1L;
	
	@Required
	@Column(nullable=false)
	@JsonProperty("title")
    private String title;
	
	@Lob
    @Column
    @JsonProperty("message")
    private String message;
    
	@Required
	@OneToOne
	@JsonProperty("creator")
    private User creator;
    
	@Required
	@Column(nullable=false)
	@JsonProperty("creationDate")
    private Date creationDate;

    public News() {}

    public News(String title, String message, User creator, Date creationDate) {
        this.title = title;
        this.message = message;
        this.creator = creator;
        this.creationDate = creationDate;
    }


    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public News setTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public News setMessage(String message) {
        this.message = message;
        return this;
    }

    @JsonProperty("creator")
    public User getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public News setCreator(User creator) {
        this.creator = creator;
        return this;
    }

    @JsonProperty("creationDate")
    public Date getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creationDate")
    public News setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }
    
    @Override
    protected News getThis() {
    	return this;
	}
}
