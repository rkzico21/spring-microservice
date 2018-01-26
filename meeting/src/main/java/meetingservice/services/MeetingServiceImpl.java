package meetingservice.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import meetingservice.entities.Meeting;
import meetingservice.entities.MeetingMessage;
import meetingservice.entities.Participant;
import meetingservice.repositories.MeetingRepository;
import meetingservice.repositories.ParticipantRepository;


@Service("meetingService")
@Repository
public class MeetingServiceImpl implements MeetingService{
 
	private final static String routingKey = "email-queue";
	
	@Autowired
    private MeetingRepository repository;
	
	@Autowired
    private ParticipantRepository participantRepository;
	
	@Autowired
    private MessageSenderService messageSender;

	@Override
	public Iterable<Meeting> getMeetings() {
		 Iterable<Meeting> meetings = repository.findAll();
    	 return meetings;
	}

	@Override
	public Meeting add(Meeting entity) {
		Meeting meeting = repository.save(entity);
		
		
		return meeting;
	}

	@Override
	public Meeting findOne(Long id) {
		return repository.findOne(id);
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public Meeting updateMeeting(Meeting currentMeeting, Meeting newMeeting) {
		currentMeeting.setSubject(newMeeting.getSubject());
		currentMeeting.setLocation(newMeeting.getLocation());
		return repository.save(currentMeeting);
	}

	@Override
	public Iterable<Participant> updateParticipants(long meetingId, List<Participant> participants) {
		Meeting meeting = repository.findOne(meetingId);
		
	    Set<Participant> currentParticipants = meeting.getParticipants();
	    List<String> emails = currentParticipants.stream().map(Participant::getEmail).collect(Collectors.toList());
	    participants.removeIf(p->emails.contains(p.getEmail()));
	    
	    if(participants.size() > 0)
	    {
	    	meeting.getParticipants().addAll(participants);
	    	meeting =  repository.save(meeting);
	    	this.sendMessage(meeting, participants);
	    }
	    
	    return meeting.getParticipants();
	}
	
	
	@Override
	public Iterable<Participant> findParticipants(long meetingId) {
		Meeting meeting = repository.findOne(meetingId);
	    return meeting.getParticipants();
	}
	
	
	private void sendMessage(Meeting meeting, List<Participant> participants) {
		
		String body = meeting.getDescription();
		List<String> emails = participants.stream().map(Participant::getEmail).collect(Collectors.toList());
	    MeetingMessage meetingMessage = new MeetingMessage(meeting.getId(), meeting.getSubject(), body, emails);
		messageSender.SendMessage(routingKey, meetingMessage);
	}
	
	
}
