package userservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import userservice.entities.User;
import userservice.exceptions.UserNotFoundException;
import userservice.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
   @Autowired
   UserRepository repository;
	
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
    
    
    @RequestMapping("/{id}")
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
    public UserResource registerUser(@RequestBody User entity) {
    	
        logger.info(String.format("Adding new user. User:%s", entity));
        User user = repository.save(entity);
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
