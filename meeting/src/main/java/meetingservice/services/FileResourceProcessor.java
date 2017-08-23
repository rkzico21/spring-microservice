package meetingservice.services;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import meetingservice.controllers.FileController;
import meetingservice.entities.File;


@Component
public class FileResourceProcessor implements ResourceProcessor<Resource<File>> {
	
	FileResourceProcessor()
	{
		
	}
	
    @Override
    public Resource<File> process(Resource<File> fileResource) {

        File file = fileResource.getContent();
        Resource<File> linkBuilder = ControllerLinkBuilder.methodOn(FileController.class).getFile(file.getId());
        Link selfLink = ControllerLinkBuilder.linkTo(linkBuilder).withSelfRel();
        fileResource.add(selfLink);
    	
        ResponseEntity controllerLinkBuilder1 = controllerLinkBuilder1 = ControllerLinkBuilder.methodOn(FileController.class).downloadFile(file.getId());
        Link contentLink = ControllerLinkBuilder.linkTo(controllerLinkBuilder1).withRel("content");
        fileResource.add(contentLink);
    	
    	
        
        return fileResource;
    }
}
