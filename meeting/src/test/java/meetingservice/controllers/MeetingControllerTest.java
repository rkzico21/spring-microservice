package meetingservice.controllers;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import meetingservice.entities.File;
import meetingservice.entities.Meeting;
import meetingservice.exceptions.ExceptionHandlers;
import meetingservice.services.FileResourceProcessor;
import meetingservice.services.FileService;
import meetingservice.services.MeetingResourceProcessor;
import meetingservice.services.MeetingService;

@RunWith(SpringRunner.class)
//@SpringBootTest({"server.port:0", "eureka.client.enabled:false"}) //helpful for integration tests
@AutoConfigureMockMvc
public class MeetingControllerTest {

	@Mock
    MeetingService meetingService;
	
	@Mock
	FileService fileService;
	 
	@Mock
	MeetingResourceProcessor resourceProcessor;
	 
	@Mock
	FileResourceProcessor fileResourceProcessor;
	
	@InjectMocks
    MeetingController controllerUnderTest;
	
	List<Meeting> meetings;
	
	
	ObjectMapper objectMapper;
	
	private MockMvc mvc;
	
	@Before
	public void setUp() {
    
	    MockitoAnnotations.initMocks(this);
	    
	    objectMapper = new ObjectMapper();
	    this.mvc = MockMvcBuilders.standaloneSetup(controllerUnderTest)
				  .setControllerAdvice(new ExceptionHandlers())
				  .build();
		 this.meetings = new  ArrayList<Meeting>();
		 this.meetings.add(new Meeting(1L, "subject1", "location1"));
		 this.meetings.add(new Meeting(2L, "subject2", "location2"));
		 
	}
	
	@Test
    public void getMeetingWithExistingId() throws Exception {
		
		when(meetingService.findOne(1L)).thenReturn(meetings.get(0));
		Resource<Meeting> meeting1Resource = new Resource<Meeting>(meetings.get(0));
    	when(resourceProcessor.process(meeting1Resource)).thenReturn(meeting1Resource);
		
    	mvc.perform(MockMvcRequestBuilders.get("/meeting/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject", is(meetings.get(0).getSubject())))
                .andExpect(jsonPath("$.location", is(meetings.get(0).getLocation())));
    }
	
	
	@Test
    public void getMeetingWithNonExistingId() throws Exception {
		when(meetingService.findOne(1L)).thenReturn(null);
		mvc.perform(MockMvcRequestBuilders.get("/meeting/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void getMeetings() throws Exception {
    	when(meetingService.getMeetings()).thenReturn(meetings);
    	
    	Resource<Meeting> meeting1Resource = new Resource<Meeting>(meetings.get(0));
    	when(resourceProcessor.process(meeting1Resource)).thenReturn(meeting1Resource);
    	
    	Resource<Meeting> meeting2Resource = new Resource<Meeting>(meetings.get(1));
    	when(resourceProcessor.process(meeting2Resource)).thenReturn(meeting2Resource);
    	
        mvc.perform(MockMvcRequestBuilders.get("/meeting").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].subject", is(meetings.get(0).getSubject())))
        	    .andExpect(jsonPath("$.content[0].location", is(meetings.get(0).getLocation())))
        	    .andExpect(jsonPath("$.content[1].subject", is(meetings.get(1).getSubject())))
        	    .andExpect(jsonPath("$.content[1].location", is(meetings.get(1).getLocation())));
        
    }
    
    @Test
    public void createMeeting() throws Exception {
    	
    	Meeting meeting = new Meeting(3L, "subject", "location");
    	
    	when(meetingService.add(Mockito.any(Meeting.class))).thenReturn(meeting);
    	
    	Link link = new Link("http:/locahosst/meeting/3");
    	link.withRel("self");
    	
    	Resource<Meeting> meetingResource = new Resource<Meeting>(meeting);
    	meetingResource.add(link);
    	
    	when(resourceProcessor.process(Mockito.any(Resource.class))).thenReturn(meetingResource);
    	
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/meeting")
    			.content(objectMapper.writeValueAsString(meeting))
    			.contentType("application/json")
    			.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isCreated())
    			.andExpect(jsonPath("$.subject", is(meeting.getSubject())))
    			.andExpect(jsonPath("$.location", is(meeting.getLocation())))
    			.andReturn();
    	
    	Assert.assertEquals( link.getHref(), result.getResponse().getHeader("Location"));
        
    }
    
    @Test
    public void createMeetingWithNotSupportedContentType() throws Exception {
    	Meeting meeting = new Meeting(3L, "subject", "location");
    	mvc.perform(MockMvcRequestBuilders.post("/meeting")
    			.content(objectMapper.writeValueAsString(meeting))
    			.contentType("form/urlencode")
    			.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isUnsupportedMediaType());
        
    }
    
    @Test
    public void deleteMeeting() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.delete("/meeting/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(meetingService, times(1)).delete(1L);
    }
    
    @Test
    public void getFilesTest() throws Exception {
    	
    	List<File> files = new ArrayList<File>();
    	files.add(new File(1L,"Filename1","","ContentType", 1L));
    	files.add(new File(2L, "FileName2", "", "ContentType", 1L));
    	
    	when(meetingService.findOne(meetings.get(0).getId())).thenReturn(meetings.get(0));
    	when(fileService.getFiles(1L)).thenReturn(files);
    	
    	Resource<File> fileResource1 = new Resource<File>(files.get(0));
    	when(fileResourceProcessor.process(fileResource1)).thenReturn(fileResource1);
    	
    	Resource<File> fileResource2 = new Resource<File>(files.get(1));
    	when(fileResourceProcessor.process(fileResource2)).thenReturn(fileResource2);
    	
    	mvc.perform(MockMvcRequestBuilders.get("/meeting/1/files")
    			.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.content", hasSize(files.size())))
                .andExpect(jsonPath("$.content[0].name", is(files.get(0).getName())))
        	    .andExpect(jsonPath("$.content[0].contentType", is(files.get(0).getContentType())))
        	    .andExpect(jsonPath("$.content[1].name", is(files.get(1).getName())))
        	    .andExpect(jsonPath("$.content[1].contentType", is(files.get(1).getContentType())));;
        
    }
    
    @Test
    public void uploadFileTest() throws Exception {
    	
    	File file = new File(1L,"Filename1","","ContentType", 1L);
    	
    	
    	MockMultipartFile multipartFile = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    	when(meetingService.findOne(meetings.get(0).getId())).thenReturn(meetings.get(0));
    	when(fileService.Store(1L, multipartFile)).thenReturn(file);
    	
    	Link link = new Link("http:/localhost/files/1");
    	link.withRel("self");
    	
    	Resource<File> fileResource = new Resource<File>(file);
    	fileResource.add(link);
    	
    	when(fileResourceProcessor.process(Mockito.any(Resource.class))).thenReturn(fileResource);
    	
       MvcResult result = mvc.perform(MockMvcRequestBuilders.fileUpload("/meeting/1/files")
    			.file(multipartFile)
    		    .accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isCreated())
        		.andReturn();
       
       
       Assert.assertEquals( link.getHref(), result.getResponse().getHeader("Location"));
    }
    
    
    
	
}
