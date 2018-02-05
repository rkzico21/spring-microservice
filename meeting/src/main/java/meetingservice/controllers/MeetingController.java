package meetingservice.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import meetingservice.entities.File;
import meetingservice.entities.Meeting;
import meetingservice.entities.Participant;
import meetingservice.exceptions.MeetingNotFoundException;
import meetingservice.services.FileResourceProcessor;
import meetingservice.services.FileService;
import meetingservice.services.MeetingResourceProcessor;
import meetingservice.services.MeetingService;
import meetingservice.services.ParticipantResourceProcessor;

@RestController
@RequestMapping(value="/meeting")
@ExposesResourceFor(Meeting.class)
public class MeetingController implements ResourceProcessor<RepositoryLinksResource>{
	
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	 @Autowired
	 MeetingService service;
	 
	 @Autowired
	 FileService fileService;
	 
	 @Autowired
	 MeetingResourceProcessor resourceProcessor;
	 
	 @Autowired
	 FileResourceProcessor fileResourceProcessor;
	 
	 @Autowired
	 ParticipantResourceProcessor participantResourceProcessor;
	 
	 //anyone
	 @RequestMapping(method = RequestMethod.GET)
	 @ResponseStatus(HttpStatus.OK)
	 public Resources<Resource<Meeting>> getMeetings() {
	    
		    logger.debug("Get meetings");
	    	Iterable<Meeting> meetings = service.getMeetings();
	    	
	    
	    	List<Resource<Meeting>> meetingResources = new ArrayList<>();
	    	
	    	for( Meeting meeting : meetings ) {
	    		meetingResources.add(resourceProcessor.process(new Resource<Meeting>(meeting)));
	    	}
	    	
	        logger.debug("Found: " +meetingResources.size() + " meeting(s)");
	    	
			return new Resources<>(meetingResources);
	  }
	 
	 //participant or owner or admin
	 @RequestMapping(method = RequestMethod.GET, value="/{id}")
	 @ResponseStatus(HttpStatus.OK)
	 public Resource<Meeting> getMeeting(@PathVariable(value="id") Long id) {
		 	
		    Meeting meeting = findMeeting(id);
	    	return resourceProcessor.process(new Resource<Meeting>(meeting));
	    }
	    
	    @RequestMapping(method = RequestMethod.POST)
	    @ResponseStatus(HttpStatus.CREATED)
	    public Resource<Meeting> createMeeting(@Valid @RequestBody Meeting entity, HttpServletResponse response) throws Throwable {
	    	Meeting meeting = service.add(entity); 
	    	Resource<Meeting> resource = resourceProcessor.process(new Resource<Meeting>(meeting));
	    	response.setStatus(HttpServletResponse.SC_CREATED);
	    	response.addHeader("Location", resource.getLink("self").getHref());
	    	
	    	return resource;
	    }
	    
	    //only owner
	    @RequestMapping(method = RequestMethod.PUT, value="/{id}")
	    @ResponseStatus(HttpStatus.OK)
	    public Resource<Meeting> updateMeeting(@PathVariable(value="id") Long id, @Valid @RequestBody Meeting entity) throws Throwable {
	    	Meeting meeting = findMeeting(id);

		    meeting = service.updateMeeting(meeting, entity);
	    	Resource<Meeting> resource = resourceProcessor.process(new Resource<Meeting>(meeting));
	    	return resource;
	    }
	    
	    //participants
	    @RequestMapping(method = RequestMethod.POST, value="/{id}/files")
	    @ResponseStatus(HttpStatus.CREATED)
	    public Resource<File> uploadFile(@PathVariable(value="id") Long id, @RequestPart("file") MultipartFile uploadedFile, HttpServletResponse response) throws Throwable {
	    
	    	Meeting meeting = findMeeting(id);
	    	
	        File file = fileService.Store(id, uploadedFile);
	        
	        Resource<File> resource = fileResourceProcessor.process(new Resource<File>(file));
	    	response.setStatus(HttpServletResponse.SC_CREATED);
	    	response.addHeader("Location", resource.getLink("self").getHref());
	    	return resource;
	    }
	    
	    @RequestMapping(method = RequestMethod.GET, value="/{id}/files")
	    @ResponseStatus(HttpStatus.OK)
	    public Resources<Resource<File>> getFiles(@PathVariable(value="id") Long id) {
	    
	    	Meeting meeting = findMeeting(id);
	    	
	        Iterable<File> files = fileService.getFiles(id);
	        List<Resource<File>> fileResources = new ArrayList<>();
	    	
	    	for( File file : files ) {
	    		fileResources.add(fileResourceProcessor.process(new Resource<File>(file)));
	    	}
	    	
	        logger.debug("Found: " +fileResources.size() + " file(s)");
	    	
			return new Resources<>(fileResources);
	    }
	    
	    @RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	    @ResponseStatus(HttpStatus.NO_CONTENT)
	    public void removeMeeting(@PathVariable(value="id") Long id) {
	    	logger.debug(String.format("deleting meeting with id: %d", id));
	    	service.delete(id);
	    }
	    
	    @RequestMapping(method = RequestMethod.GET, value="/{id}/participants")
	    @ResponseStatus(HttpStatus.OK)
	    public Resources<Resource<Participant>> getParticipants(@PathVariable(value="id") Long id) {
	    
	    	Meeting meeting = findMeeting(id);
			
	    	List<Resource<Participant>> participantResources = new ArrayList<>();
	    	Iterable<Participant> participants = service.findParticipants(id);
	    	
	    	for( Participant participant : participants ) {
	    		logger.debug(participant.toString());
		    	participantResources.add(participantResourceProcessor.process(new Resource<Participant>(participant)));
	    	}
	    	
	        logger.debug(participantResources.size() + " participant(s)");
	    	
			return new Resources<>(participantResources);
	    }
	    
	    //only owner or authorized user
	    @RequestMapping(method = RequestMethod.PUT, value="/{id}/participants")
	    @ResponseStatus(HttpStatus.OK)
	    public Resources<Resource<Participant>> updateParticipants(@PathVariable(value="id") Long id, @RequestBody List<Participant> participants) {
	    
	    	Meeting meeting = findMeeting(id);
			
	    	Iterable<Participant> updatedParticipants = service.updateParticipants(id, participants);
	    	List<Resource<Participant>> participantResources = new ArrayList<>();
	    	 
	    	for( Participant participant : updatedParticipants ) {
	    		logger.debug(participant.toString());
		    	participantResources.add(participantResourceProcessor.process(new Resource<Participant>(participant)));
	    	}
	    	
	        logger.debug(participantResources.size() + " participant(s)");
	    	
			return new Resources<>(participantResources);
	    }
	    
	   
	    private Meeting findMeeting(Long id) {
	    	logger.info("Find meeting with id " + id.toString());
		    Meeting meeting = service.findOne(id);
	    	
	    	if(meeting == null) {
	    		logger.warn("No meeting found with id " + id.toString());
			    throw new MeetingNotFoundException(id);
	    	}
	    	
	    	logger.debug("Found meeting with id " + id.toString() + ". Data: " + meeting.toString());
	    	
	    	return meeting;
	    }

		@Override
		public RepositoryLinksResource process(RepositoryLinksResource arg0) {
			arg0.add(ControllerLinkBuilder.linkTo(MeetingController.class).withRel("meetings"));
			return arg0;
		}
}
