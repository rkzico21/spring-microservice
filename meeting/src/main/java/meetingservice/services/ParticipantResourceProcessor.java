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
import meetingservice.entities.Participant;


@Component
public class ParticipantResourceProcessor implements ResourceProcessor<Resource<Participant>> {
	
	ParticipantResourceProcessor()
	{
		
	}
	
	@Override
    public Resource<Participant> process(Resource<Participant> participantResource) {

        Participant participant = participantResource.getContent();
        return participantResource;
    }
}
