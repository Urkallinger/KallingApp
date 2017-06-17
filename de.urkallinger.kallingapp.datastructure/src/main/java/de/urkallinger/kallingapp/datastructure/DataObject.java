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
     * 
     * @return <code>true</code>, wenn alle als @Required gekennzeichneten Felder einen Wert enhalten,
     *         sonst <code>false</code>
     */
    public boolean isValid() {
        for (Field f : getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
	            if(f.getAnnotation(Required.class) != null) {
	            	if(f.get(this) == null) {
	    				return false; 
	    			}
	            	if(f.getType().isAssignableFrom(String.class)) {
	            		if(((String) f.get(this)).isEmpty()) {
	            			return false;
	            		}
	            	}
	            }
            } catch (IllegalAccessException e) {
            	e.printStackTrace();
            	return false;
            }
        }
        
        return true;
    }
    
    protected abstract T getThis();
}
