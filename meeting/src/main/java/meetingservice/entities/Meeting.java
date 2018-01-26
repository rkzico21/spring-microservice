package meetingservice.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

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

	private String description;
	
	private String dateTime;
	
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "meeting_participant", 
        joinColumns = { @JoinColumn(name = "meeting_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "participant_id") }
    )
	private Set<Participant> participants = new HashSet<Participant>(0);
	
	Meeting() {}

    public Meeting(Long id, String subject, String location, String description, String dateTime) {
        this.id = id;
    	this.subject = subject;
    	this.location = location;
    	this.description = description;
    	this.dateTime = dateTime;
    }
    
    public Meeting(Long id, String subject, String location, String description, String dateTime, Set<Participant> participants) {
        this(id, subject, location, description, dateTime);
        this.participants = participants;
    }

    
    public Long getId() {
        return this.id;
    }

    public String getSubject() {
    	return this.subject;
    }
    
    public String getLocation() {
    	return this.location;
    }
    
    public String getDateTime() {
    	return this.dateTime;
    }
    
    public String getDescription() {
    	return this.description;
    }
    
    
    
	public Set<Participant> getParticipants() {
		return this.participants;
	}
    
    
    @Override
    public String toString() {
        return String.format("Id:%d, Subject:%s, Location:%s, Description:%s, Datetime:%s", this.id, this.subject, this.location, 
        		this.description, this.dateTime);
    }

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
}
