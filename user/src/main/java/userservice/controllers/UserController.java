package userservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import userservice.CustomJRDataSource;
import userservice.MessageSenderService;
import userservice.entities.User;
import userservice.exceptions.UserNotFoundException;
import userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   
   private final static String routingKey = "spring-boot";
   
   @Autowired
   UserService service;
   
   @Autowired
   MessageSenderService messageService;
	
   @RequestMapping(method = RequestMethod.GET)
   public Resources<UserResource> index() {
    
	   logger.info("Get all the users");
       Iterable<User> users = service.findAll();
    	
    	List<UserResource> userResourcest = new ArrayList<>();
    	
    	for( User user : users ) {
        	userResourcest.add(new UserResource(user));
        }
    	
		return new Resources<>(userResourcest);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value= "/{id}")
    public UserResource getUser(@PathVariable(value="id") Long id) throws UserNotFoundException{
        logger.info(String.format("Finding user with id: %d", id));
    	
    	User user = service.findOne(id);
    	if (user == null) 
    	{
    		logger.warn(String.format("No user found with id: %d", id));
    		throw new UserNotFoundException(id);
        }
    	
    	logger.info(String.format("Finding user with id: %d, User:%s", id, user));
    	return new UserResource(user);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResource registerUser(@Valid @RequestBody User entity) {
    	
        logger.info(String.format("Adding new user. User:%s", entity));
        User user = service.add(entity);
        messageService.SendMessage(routingKey, user);
        logger.info(String.format("New user created. User:%s", user));
        return new UserResource(user);
    }
    
    @RequestMapping(method = RequestMethod.GET, value= "/report")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	 //response.setContentType("application/pdf");
    	 response.setHeader("Content-Disposition", "inline; filename=file.pdf");
    	 response.setStatus(HttpServletResponse.SC_OK); 
    	 
    	 ByteArrayResource resources = null;
         
    	ClassPathResource resource = new ClassPathResource("reports/report1.jrxml");
    	InputStream reportInputStream = null;
		try {
			 //servletOutputStream = response.getOutputStream();
	         reportInputStream = resource.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	
    	JasperPrint jasperPrint;
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportInputStream);
			CustomJRDataSource<User> dataSource = new CustomJRDataSource<User>()
				.initBy(this.service.findAll().iterator());
			jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
		
			
			
		    byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
		    //JasperExportManager.
			
			
		    resources = new ByteArrayResource(bytes);
	        
		
			
		} catch (JRException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
			}
		finally {
	
		}
		
        
		
		
    	
		return ResponseEntity.ok()
                .contentLength(resources.contentLength())
                
                .header("Content-Disposition", "inline; filename=file.pdf")
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resources);
    }
 }


class UserResource extends ResourceSupport {

	private final User user;

	public UserResource(User user) {
		
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}
}
