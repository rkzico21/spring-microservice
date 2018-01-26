package messageservice.Dto;

import java.util.List;

public class MailMessage{

	private String id;
	
	private String subject;
	
	private List<String> participants;
	
	private String body;
	
	MailMessage() {}

    public MailMessage(String id, String subject, String body, List<String> participants) {
    	this.id = id;
    	this.subject = subject;
    	this.participants = participants;
    	this.body = body;
    }
    
    public String getId() {
       return this.id;
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
