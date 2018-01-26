package todolistservice.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import todolistservice.TodoListResourceProcessor;
import todolistservice.User;
import todolistservice.UserServiceClient;
import todolistservice.entities.*;
import todolistservice.exceptions.TodolistItemNotFoundException;
import todolistservice.repositories.TodolistService;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class TodoListController {
    
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    TodolistService service;
	
	@Autowired
	TokenStore tokenStore;

	
	@Autowired
    private UserServiceClient userServiceClient;
	
	@Autowired
    TodoListResourceProcessor resourceProcessor;
	
	@RequestMapping(method = RequestMethod.GET, value="/todolist/item")
	public Resources<Resource<TodoListItem>> index(@RequestParam(value = "userid", required = false) Long userId) throws Throwable {
        
	    //VerifyUser(userId);
	   
    	Iterable<TodoListItem> items = service.findByUserId(userId);
    	List<Resource<TodoListItem>> resources = new ArrayList<>();
    	
    	for( TodoListItem item : items ) {
    		Resource<TodoListItem> resource = new Resource<TodoListItem>(item);
    		
    		resources.add(resourceProcessor.process(resource));
        }
    	
    	return new Resources<>(resources);
    }
	
	@RequestMapping(method = RequestMethod.GET, value="/todolist/item/{id}")
    public Resource<TodoListItem> getItem(@PathVariable(value="id") Long id) throws Throwable {
    	
    	TodoListItem item = service.findOne(id); 
    	if(item == null) {
    		 throw new TodolistItemNotFoundException(id);
    	}
    	
    	return resourceProcessor.process(new Resource<TodoListItem>(item));
    }
	
    
	@RequestMapping(method = RequestMethod.POST, value="/todolist/item")
    public Resource<TodoListItem> addItem(@RequestBody TodoListItem entity, HttpServletResponse response) throws Throwable {
    	//VerifyUser(entity.getUserId());
    	TodoListItem todoList = service.add(entity); 
    	
    	Resource<TodoListItem> resource = resourceProcessor.process(new Resource<TodoListItem>(todoList));
    	response.setStatus(HttpServletResponse.SC_CREATED);
    	response.addHeader("Location", resource.getLink("self").getHref());
    	return resource;
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE, value="/todolist/item/{id}")
    public void removeItem(@PathVariable(value="id") Long id) {
    	logger.info("deleting item with id: %d", id);
    	service.delete(id);
    }
    
    private void VerifyUser(Long userId) throws Throwable {
    	
    	logger.info(String.format("Verifying user with user id: %d", userId)); 
    	if(userId == 0) return;
    	try {
    		
    		ResponseEntity<User> user = userServiceClient.getUser(getAuthenticationToken(), userId);
    		logger.info(user.getStatusCode().toString());
    	} 
        catch (com.netflix.hystrix.exception.HystrixRuntimeException ex)
    	{
        	logger.error("Could not verify user with id " + userId, ex);
      	  	Throwable cause = ex.getCause();
    	    throw cause;
    	}
    	catch (Throwable ex)
    	{
    	   throw ex;
    	}
    }
    
    private String getAuthenticationToken() {
    	
    	String token = "";
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication!= null && authentication.isAuthenticated() && requestAttributes instanceof ServletRequestAttributes) {
                javax.servlet.http.HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
                token =  request.getHeader("Authorization");
        }
        
        return token;
    }
    
}


