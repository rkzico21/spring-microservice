package meetingservice.entities;

import java.util.List;

public class MeetingMessage{

	private Long id;
	
	private String subject;
	
	private List<String> participants;
	
	private String body;
	
	MeetingMessage() {}

    public MeetingMessage(Long id, String subject, String body, List<String> participants) {
    	this.id = id;
    	this.subject = subject;
    	this.participants = participants;
    	this.body = body;
    }
    
    public String getId() {
       return String.format("Meeting_%d", this.id);
    }
    
    public String getSubject() {
        return this.subject;
     }
    
    public List<String> getParticipants() {
        return this.participants;
     }
    
    public String getBody() {
        return this.body;
    }
 }
