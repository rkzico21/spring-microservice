package userservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import userservice.entities.User;
import userservice.exceptions.UserNotFoundException;
import userservice.repositories.UserRepository;

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
public class UserController {
    
	@Autowired
	UserRepository repository;
	
   @RequestMapping("/user")
   public Resources<UserResource> index() {
    
    	Iterable<User> users = repository.findAll();
    	
    	List<UserResource> userResourcest = new ArrayList<>();
    	
    	for( User user : users ) {
        	userResourcest.add(new UserResource(user));
        }
    	
		return new Resources<>(userResourcest);
    }
    
    
    @RequestMapping("/user/{id}")
    public UserResource getUser(@PathVariable(value="id") Long id) throws UserNotFoundException{
    	User user = repository.findOne(id);
    	
    	if (user == null) 
    	{
    		throw new UserNotFoundException(id);
        }
    	
    	return new UserResource(user);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/user")
    public UserResource registerUser(@RequestBody User entity) {
    	User user = repository.save(entity);
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
