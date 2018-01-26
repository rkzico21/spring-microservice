package userservice.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import userservice.MessageSenderService;
import userservice.dtos.User;
import userservice.exceptions.UserNotFoundException;
import userservice.integration.UserResourceProcessor;
import userservice.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   
   @Autowired
   UserService service;
   
   @Autowired
   MessageSenderService messageService;
   
   @Autowired
   UserResourceProcessor userResourceProcessor;
   
   @PreAuthorize("hasAuthority('admin') or hasAuthority('user_list')")
   @RequestMapping(method = RequestMethod.GET)
   public Resources<Resource<User>> index() {
    
	   logger.info("Get all the users");
       Iterable<User> users = service.findAll();
       
       List<Resource<User>> userResourcest = new ArrayList<>();
    
       for( User user : users ) {
        	userResourcest.add(userResourceProcessor.process(new Resource<User>(user)));
        }
    	
		return new Resources<>(userResourcest);
    }
   
   @PreAuthorize("hasAuthority('admin') or hasAuthority('user_read')")
   @RequestMapping(method = RequestMethod.GET, value= "/{id}")
   public Resource<User> getUser(@PathVariable(value="id") Long id) throws UserNotFoundException{
        logger.info(String.format("Finding user with id: %d", id));
    	
    	User user = service.findOne(id);
    	if (user == null) 
    	{
    		logger.warn(String.format("No user found with id: %d", id));
    		throw new UserNotFoundException(id);
        }
    	
    	logger.debug(String.format("Finding user with id: %d, User:%s", id, user));
    	return userResourceProcessor.process(new Resource<User>(user));
    }
    
    @PreAuthorize("hasAuthority('admin') or hasAuthority('user_create')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<User> registerUser(@Valid @RequestBody User userdto) {
    	
        logger.info(String.format("Adding new user. User:%s", userdto));
        User user = service.add(userdto);
       
        logger.info(String.format("New user created. User:%s", user));
        return userResourceProcessor.process(new Resource<User>(user));
    }
    
    @PreAuthorize("hasAuthority('admin') or hasAuthority('user_list')")
    @RequestMapping(method = RequestMethod.GET, value= "/search")
    @ResponseStatus(HttpStatus.OK)
    public Resources<Resource<User>> searchUser(@RequestParam(value = "fullName", required = false) String query) {
    	
    	logger.info("Get all the users");
    	
    	Iterable<User> users = service.search(query);
     	List<Resource<User>> userResources = new ArrayList<>();
     	
     	for( User user : users ) {
         	userResources.add(userResourceProcessor.process(new Resource<User>(user)));
         }
     	
 		return new Resources<>(userResources); 
    }
   
    
    /*@RequestMapping(method = RequestMethod.GET, value= "/report")
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
    }*/
 }
