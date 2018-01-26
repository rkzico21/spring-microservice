package meetingservice.controllers;

import org.springframework.web.bind.annotation.RestController;
import meetingservice.entities.File;
import meetingservice.exceptions.FileNotFoundException;
import meetingservice.services.FileResourceProcessor;
import meetingservice.services.FileService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;



@RestController
@RequestMapping("/file")
public class FileController {
    
   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   @Autowired
   FileService fileService;
   
   @Autowired
   FileResourceProcessor resourceProcessor;
   
   
   @RequestMapping(method = RequestMethod.GET, value="/{id}")
   @ResponseStatus(HttpStatus.OK)
   public Resource<File> getFile(@PathVariable(value="id") Long id) {
		 	
		    logger.info("Find file with id " + id.toString());
		    File file = fileService.findOne(id);
	    	
	    	if(file == null) {
	    		logger.warn("No file found with id " + id.toString());
			    throw new FileNotFoundException(id);
	    	}
	    	
	    	logger.debug("Found file with id " + id.toString() + ". Data: " + file.toString());
		    
	    	return resourceProcessor.process(new Resource<File>(file));
	}
   
   
   @RequestMapping(method = RequestMethod.GET, value="/{id}/content")
   @ResponseStatus(HttpStatus.OK)
   public ResponseEntity downloadFile(@PathVariable(value="id") Long id) {
		 	
		    logger.info("Find file with id " + id.toString());
		    File file = fileService.findOne(id);
	    	
	    	if(file == null) {
	    		logger.warn("No file found with id " + id.toString());
			    throw new FileNotFoundException(id);
	    	}
	    	
	    	
	    	ByteArrayResource resource = null;
	        try {
	            resource = new ByteArrayResource(fileService.ReadFile(file));
	        } catch (IOException e) {
	            logger.error("there was an error getting the file bytes ", e);
	        }

	        return ResponseEntity.ok()
	                .contentLength(resource.contentLength())
	                .header("Content-Disposition","attachment; filename=" + file.getName())
	                //this line doesn't seem to work as I set the file format in the controller request mapping
	                .contentType(MediaType.parseMediaType(file.getContentType()))
	                .body(resource);
	    	
	    	
	 }
 }

