package todolistservice.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.code.ssm.api.ReadThroughSingleCache;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import todolistservice.TodoListResourceProcessor;
import todolistservice.User;
import todolistservice.UserServiceClient;
import todolistservice.entities.*;
import todolistservice.exceptions.TodolistNotFoundException;
import todolistservice.exceptions.UserNotFoundException;
import todolistservice.repositories.TodoListRepository;
import todolistservice.repositories.TodolistService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
	@RequestMapping(method = RequestMethod.GET, value="/todolist")
	public Resources<Resource<TodoList>> index(@RequestParam(value = "userid", required = false) Long userId) throws Throwable {
        
	    VerifyUser(userId);
	   
    	Iterable<TodoList> todoListCollection = service.findByUserId(userId);
    	List<Resource<TodoList>> todoListResources = new ArrayList<>();
    	
    	for( TodoList todoList : todoListCollection ) {
    		Resource<TodoList> todolistResource = new Resource<TodoList>(todoList);
    		
    		todoListResources.add(resourceProcessor.process(todolistResource));
        }
    	
       
    	
		return new Resources<>(todoListResources);
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/todolist/{id}")
    public Resource<TodoList> getTodoList(@PathVariable(value="id") Long id) {
    	TodoList todoList = service.findOne(id);
    	
    	if(todoList == null)
    		throw new TodolistNotFoundException(id);
    	
    	return resourceProcessor.process(new Resource<TodoList>(todoList));
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/todolist")
    public Resource<TodoList> createTodoList(@RequestBody TodoList entity, HttpServletResponse response) throws Throwable {
    	VerifyUser(entity.getUserId());
    	TodoList todoList = service.add(entity); 
    	
    	Resource<TodoList> resource = resourceProcessor.process(new Resource<TodoList>(todoList));
    	response.setStatus(HttpServletResponse.SC_CREATED);
    	response.addHeader("Location", resource.getLink("self").getHref());
    	return resource;
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE, value="/todolist/{id}")
    public void removeTodoList(@PathVariable(value="id") Long id) {
    	logger.info("deleting todo list with id: %d", id);
    	service.delete(id);
    }
    
    private void VerifyUser(Long userId) throws Throwable {
    	
    	logger.info(String.format("Verifying user with user id: %d", userId)); 
    	if(userId == 0) return;
    	try {
    		
    		User user = userServiceClient.getUser(getAuthorizationToken(), userId);
    	} catch (Exception ex)
    	{
    		logger.error(ex.getMessage());
    		throw new UserNotFoundException(userId);
    	}
    }
    
    private String getAuthorizationToken() {
    	
    	String token = "";
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication!= null && authentication.isAuthenticated() && requestAttributes instanceof ServletRequestAttributes) {
                javax.servlet.http.HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
                token =  request.getHeader("Authorization");
        }
        
        System.out.println(token);
        
        return token;
    }
    
}

class TodoListResource extends ResourceSupport {

	private final TodoList todoList;

	public TodoListResource(TodoList todoList) {
		this.todoList = todoList;
	}

	public TodoList getCart() {
		return todoList;
	}
}


