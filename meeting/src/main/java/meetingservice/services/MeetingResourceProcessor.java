package meetingservice.services;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import meetingservice.controllers.MeetingController;
import meetingservice.entities.File;
import meetingservice.entities.Meeting;


@Component
public class MeetingResourceProcessor implements ResourceProcessor<Resource<Meeting>> {
	
	MeetingResourceProcessor()
	{
		
	}
	
    @Override
    public Resource<Meeting> process(Resource<Meeting> meetingResource) {

        Meeting meeting = meetingResource.getContent();
        Resource<Meeting> linkBuilder = ControllerLinkBuilder.methodOn(MeetingController.class).getMeeting(meeting.getId());
        Link selfLink = ControllerLinkBuilder.linkTo(linkBuilder).withSelfRel();
        meetingResource.add(selfLink);
    	 
        Resources<Resource<File>> filesLinkBuilder = ControllerLinkBuilder.methodOn(MeetingController.class).getFiles(meeting.getId());        
    	Link filesLink = ControllerLinkBuilder.linkTo(filesLinkBuilder).withRel("files");
        meetingResource.add(filesLink);
        
        try{
        	Resource<File> filesUploadLinkBuilder = ControllerLinkBuilder.methodOn(MeetingController.class).uploadFile(meeting.getId(), null, null);        
        	Link filesUploadLink = ControllerLinkBuilder.linkTo(filesUploadLinkBuilder).withRel("fileUpload");
            meetingResource.add(filesUploadLink);
        }
        catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return meetingResource;
    }
}
