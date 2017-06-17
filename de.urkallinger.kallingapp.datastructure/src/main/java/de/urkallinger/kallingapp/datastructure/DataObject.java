package de.urkallinger.kallingapp.datastructure;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.urkallinger.kallingapp.datastructure.annotations.Required;
import de.urkallinger.kallingapp.datastructure.exceptions.ValidationException;

@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id"
})
public abstract class DataObject<T extends DataObject<T>> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
    protected long id;

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public T setId(long id) {
        this.id = id;
        return getThis();
    }
    
    /**
     * Diese Methode überprüft alle Felder die mit @Required gekennzeichnet sind, ob diese ungleich <code>null</code>
     * sind. Handelt es sich bei dem Feld um ein Objekt vom Typ String, so wird zusätzlich geprüft, ob dieser leer ist.
     * 
     * @throws ValidationException
     */
    public void validate() throws ValidationException {
        for (Field f : getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
	            if(f.getAnnotation(Required.class) != null) {
	            	if(f.get(this) == null) {
	            		String msg = String.format("Field '%s' is required and cannot be null.", f.getName());
	    				throw new ValidationException(msg);
	    			}
	            	if(f.getType().isAssignableFrom(String.class)) {
	            		if(((String) f.get(this)).isEmpty()) {
	            			String msg = String.format("Field '%s' is required and cannot be empty.", f.getName());
		    				throw new ValidationException(msg);
	            		}
	            	}
	            }
            } catch (IllegalAccessException e) {
            	e.printStackTrace();
            	String msg = String.format("An error occurred while validation field '%s'.", f.getName());
				throw new ValidationException(msg);
            }
        }
    }
    
    protected abstract T getThis();
}
