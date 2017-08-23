package meetingservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "meeting")
public class Meeting{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
	
	@NotEmpty(message="Subject is missing.")
	private String subject;
	
	private String location;

	Meeting() {}

    public Meeting(Long id, String subject, String location) {
        this.id = id;
    	this.subject = subject;
    	this.location = location;
    }

    
    public Long getId() {
        return id;
    }

    public String getSubject() {
    	return this.subject;
    }
    
    public String getLocation() {
    	return this.location;
    }
    
    @Override
    public String toString() {
        return String.format("Id:%d Subject:%s location:%s", this.id, this.subject, this.location);
    }

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
}
