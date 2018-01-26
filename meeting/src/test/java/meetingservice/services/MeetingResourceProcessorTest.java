package meetingservice.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import meetingservice.entities.Meeting;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class MeetingResourceProcessorTest {

	@Autowired
    private WebApplicationContext context;
	
	@InjectMocks
	private MeetingResourceProcessor meetingResourceProcessor;
	 
	
	private MockMvc mvc;

	
	@Before
	public void setUp() {
    
		MockitoAnnotations.initMocks(this);
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
		
	@Test
    public void processTest()  {
		Meeting meeting = new Meeting(1L, "subject", "location", "description2", "10-02-2018 05:20:00 pm");
		Resource<Meeting> resource = new Resource<Meeting>(meeting);
		
		Resource<Meeting> meetingResource =	meetingResourceProcessor.process(resource);
	    
		Assert.assertEquals(meeting, meetingResource.getContent());
		
		Assert.assertEquals(4, meetingResource.getLinks().size());
		
		//Test if links are created and populated
		Link selfLink = meetingResource.getLink("self");
	    Assert.assertNotNull(selfLink);
	    Assert.assertNotNull(selfLink.getHref());
		
		Link filesLink = meetingResource.getLink("files");
		Assert.assertNotNull(filesLink);
	    Assert.assertNotNull(filesLink.getHref());
	    
	    Link filesUploadLink = meetingResource.getLink("fileUpload");
	    Assert.assertNotNull(filesUploadLink);
	    Assert.assertNotNull(filesUploadLink.getHref());
			    
	    Link participantsLink = meetingResource.getLink("participants");
		Assert.assertNotNull(participantsLink);
	    Assert.assertNotNull(participantsLink.getHref());
	
	}
}
