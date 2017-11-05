package meetingservice.services;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import meetingservice.entities.Meeting;
import meetingservice.repositories.MeetingRepository;


public class MeetingServiceTest {

	@InjectMocks
    MeetingServiceImpl meetingService;
	
	 
	@Mock
	MeetingRepository meetingRepository;
	 
	List<Meeting> meetings;
	
	@Before
	public void setUp() {
    
	    MockitoAnnotations.initMocks(this);
	    
	    this.meetings = new  ArrayList<Meeting>();
		this.meetings.add(new Meeting(1L, "subject1", "location1"));
		this.meetings.add(new Meeting(2L, "subject2", "location2"));
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
		Meeting newMeeting = new Meeting(meeting.getId(), meeting.getSubject(), meeting.getLocation());
		newMeeting.setLocation("Updated location");
		newMeeting.setSubject("Updated subject");
		
	    meetingService.updateMeeting(meetings.get(0), newMeeting);
	    
	    Assert.assertEquals(newMeeting.getSubject(), meeting.getSubject());

	    Assert.assertEquals(newMeeting.getLocation(), meeting.getLocation());
	    
	    verify(meetingRepository, times(1)).save(meeting);
	}
    
    
	
}
