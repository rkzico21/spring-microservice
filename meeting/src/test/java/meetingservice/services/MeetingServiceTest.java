package meetingservice.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import meetingservice.entities.Meeting;
import meetingservice.entities.MeetingMessage;
import meetingservice.entities.Participant;
import meetingservice.repositories.MeetingRepository;


public class MeetingServiceTest {

	@InjectMocks
    MeetingServiceImpl meetingService;
	
	@Mock
	MeetingRepository meetingRepository;
	
	@Mock
	MessageSenderService meesageSenderService;
	 
	List<Meeting> meetings;
	
	@Before
	public void setUp() {
    
	    MockitoAnnotations.initMocks(this);
	    
	    this.meetings = new  ArrayList<Meeting>();
	    this.meetings.add(new Meeting(1L, "subject1", "location1", "description1", "01-01-2018 05:20:00 pm", new HashSet<Participant>(0)));
		this.meetings.add(new Meeting(2L, "subject2", "location2", "description2", "10-02-2018 05:20:00 pm"));
	}
	
		
	@Test
    public void getMeetingsTest()  {
	    
		when(meetingRepository.findAll()).thenReturn(meetings);
	    Iterable<Meeting> actuals = meetingService.getMeetings();
	    
	    List<Meeting> actualList = new ArrayList<>();
	    for (Meeting meeting : actuals)
	    {       actualList.add(meeting);
	       
	    }
	    
	    Assert.assertArrayEquals(meetings.toArray(), actualList.toArray());
	    
	    //verify(meetingRepository, times(1)).findAll();
	}
	
	@Test
    public void deleteMeetingsTest()  {
	    
		Long id = 1L;
		meetingService.delete(id);
	    verify(meetingRepository, times(1)).delete(id);
	
	}
	
	@Test
    public void findOneTest()  {
	    
		Long id = 1L;
		
		when(meetingRepository.findOne(id)).thenReturn(meetings.get(0));
	    Meeting actual = meetingService.findOne(id);
	    Assert.assertEquals(meetings.get(0), actual);
	    
	}
	
	
	@Test
    public void updateMeetingTest()  {
		
		Meeting meeting = meetings.get(0);
		Meeting newMeeting = new Meeting(meeting.getId(), meeting.getSubject(), meeting.getLocation(), meeting.getDescription(), meeting.getDateTime());
		newMeeting.setLocation("Updated location");
		newMeeting.setSubject("Updated subject");
		
	    meetingService.updateMeeting(meetings.get(0), newMeeting);
	    
	    Assert.assertEquals(newMeeting.getSubject(), meeting.getSubject());

	    Assert.assertEquals(newMeeting.getLocation(), meeting.getLocation());
	    
	    verify(meetingRepository, times(1)).save(meeting);
	}
	
	@Test
	public void updateParticipantsTest() {
	
		Meeting meeting = meetings.get(0);
		meeting.getParticipants().add(new Participant(1L, 1L, "name", "position", "email1@example.com", "organization"));
		meeting.getParticipants().add(new Participant(2L, 2L, "name", "position", "email2@example.com", "organization"));
		when(meetingRepository.findOne(meeting.getId())).thenReturn(meeting);
		
		List<Participant> participantsToAdd = new ArrayList<Participant>();
		
		participantsToAdd.add(new Participant(3L, 3L, "name", "position", "email3@example.com", "organization"));
		participantsToAdd.add(new Participant(4L, 3L, "name", "position", "email2@example.com", "organization"));
		
		when(meetingRepository.save(meeting)).thenReturn(meeting);
		
		Iterable<Participant> participants = meetingService.updateParticipants(meeting.getId(), participantsToAdd);
		verify(meetingRepository, times(1)).save(meeting);
		verify(meesageSenderService, times(1)).SendMessage(any(String.class), any(MeetingMessage.class));
	    
		Assert.assertEquals(3, meeting.getParticipants().size());
		
	}
	
	
    
    
	
}
