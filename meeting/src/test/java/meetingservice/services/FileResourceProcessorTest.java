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

import meetingservice.entities.File;
import meetingservice.entities.Meeting;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class FileResourceProcessorTest {

	@Autowired
    private WebApplicationContext context;
	
	@InjectMocks
	private FileResourceProcessor fileResourceProcessor;
	 
	
	private MockMvc mvc;

	
	@Before
	public void setUp() {
    
		MockitoAnnotations.initMocks(this);
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
		
	@Test
    public void processTest()  {
		File file = new File(1L, "FileName", "pathtofile", "contenttype",  1L);
		Resource<File> resource = new Resource<File>(file);
		
		Resource<File> fileResource = fileResourceProcessor.process(resource);
	    
		Assert.assertEquals(file, fileResource.getContent());
		
		Assert.assertEquals(2, fileResource.getLinks().size());
		
		//Test if links are created and populated
		Link selfLink = fileResource.getLink("self");
	    Assert.assertNotNull(selfLink);
	    Assert.assertNotNull(selfLink.getHref());
		
		Link filesLink = fileResource.getLink("content");
		Assert.assertNotNull(filesLink);
	    Assert.assertNotNull(filesLink.getHref());
	}
}
