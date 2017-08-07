package userservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import userservice.MessageSenderService;
import userservice.entities.User;
import userservice.exceptions.UserNotFoundException;
import userservice.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   
   private final static String routingKey = "spring-boot";
   
   @Autowired
   UserRepository repository;
   
   @Autowired
   MessageSenderService messageService;
	
   @RequestMapping(method = RequestMethod.GET)
   public Resources<UserResource> index() {
    
	   logger.info("Get all the users");
       Iterable<User> users = repository.findAll();
    	
    	List<UserResource> userResourcest = new ArrayList<>();
    	
    	for( User user : users ) {
        	userResourcest.add(new UserResource(user));
        }
    	
		return new Resources<>(userResourcest);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value= "/{id}")
    public UserResource getUser(@PathVariable(value="id") Long id) throws UserNotFoundException{
        logger.info(String.format("Finding user with id: %d", id));
    	
    	User user = repository.findOne(id);
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
        User user = repository.save(entity);
        messageService.SendMessage(routingKey, user);
        logger.info(String.format("New user created. User:%s", user));
        return new UserResource(user);
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
