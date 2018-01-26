package meetingservice.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "participant")
public class Participant{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
	private Long participantId;
	
	private String name;
	
	private String position;
	
	private String email;
	
	private String organization;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
	private Set<Meeting> meetings = new HashSet<Meeting>(0);

	Participant() {}

    public Participant(Long id, Long participantId, String name, String position, String email, String organization) {
       this.id = id;
       this.participantId = participantId;
       this.name = name;
       this.position = position;
       this.email = email;
       this.organization = organization;
    }
    
    public Participant(Long id, Long participantId, String name, String position, String email, String organization, Set<Meeting> meetings) {
        this(id, participantId, name, position, email, organization);
        this.meetings = meetings;
     }

    public Long getId() {
        return id;
    }

    public Long getParticipantId() {
    	return this.participantId;
    }
    
    public String getName() {
    	return this.name;
    }

    public String getEmail() {
		return this.email;
	}
    
    public String getPosition() {
    	return this.position;
    }
    
    public String getOrganization() {
    	return this.organization;
    }
    
   
	public Set<Meeting> getMeetings() {
		return this.meetings;
	}
    
    @Override
    public String toString() {
        return String.format("Id:%d, Participant Id:%d,  Name:%s, email:%s", this.id, this.participantId, this.name, this.email);
    }
 }
