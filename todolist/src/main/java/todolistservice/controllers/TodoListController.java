package todolistservice.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import todolistservice.resourceprocessors.ResourceWithEmbeddable;
import todolistservice.resourceprocessors.TodoListResourceProcessor;
import todolistservice.resourceprocessors.TodolistPageResourceProcessor;
import todolistservice.User;
import todolistservice.UserServiceClient;
import todolistservice.dtos.PageResult;
import todolistservice.entities.*;
import todolistservice.exceptions.TodolistItemNotFoundException;
import todolistservice.repositories.TodolistService;
import static todolistservice.resourceprocessors.ResourceWithEmbeddable.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
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
@ExposesResourceFor(TodoListItem.class)
public class TodoListController  implements ResourceProcessor<RepositoryLinksResource> {
    
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    TodolistService service;
	
	@Autowired
	TokenStore tokenStore;

	
	@Autowired
    private UserServiceClient userServiceClient;
	
	@Autowired
    TodoListResourceProcessor resourceProcessor;
	
	@Autowired
	TodolistPageResourceProcessor todolistPageResourceProcessor;
	
	@RequestMapping(method = RequestMethod.GET, value="/todolist/item")
	public Resource<PageResult<TodoListItem>> index(@RequestParam(value = "userid", required = false) Long userId,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
    		@RequestParam(value = "size", required = false, defaultValue = "20") Integer size) throws Throwable {
        
	    VerifyUser(userId);
	   
    	PageResult<TodoListItem> result = service.findByUserId(userId, page, size);
    	List<Resource<TodoListItem>> resources = new ArrayList<>();
    	
    	for( TodoListItem item : result.getResults() ) {
    		Resource<TodoListItem> resource = new Resource<TodoListItem>(item);
    		
    		resources.add(resourceProcessor.process(resource));
        }
    	
    	
    	ResourceWithEmbeddable<PageResult<TodoListItem>> todolistPageResource = embeddedRes(result, resWrapper(resources, "items"));
    	return todolistPageResourceProcessor.process(todolistPageResource);
    	
    	
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

	@Override
	public RepositoryLinksResource process(RepositoryLinksResource arg0) {
		try {
			arg0.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TodoListController.class).index(null, null, null)).withRel("todolist"));
		} catch (Throwable e) {
			logger.error(e.getMessage());
		}
		
		return arg0;
	}
    
}


