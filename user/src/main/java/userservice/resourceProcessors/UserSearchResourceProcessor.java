package userservice.resourceProcessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import userservice.controllers.UserController;
import userservice.dtos.SearchResult;
import userservice.dtos.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserSearchResourceProcessor implements ResourceProcessor<ResourceWithEmbeddable<SearchResult<User>>> {

	@Autowired
	private UserResourceProcessor userResourceProcessor;
	
	private final Provider<HttpServletRequest> request;
	
	@Override
	public ResourceWithEmbeddable<SearchResult<User>> process(ResourceWithEmbeddable<SearchResult<User>> resource) {
		
		SearchResult<User> result = resource.getContent();
		
	    
		Map<String, Object> parameters = new HashMap<>();
		
		for (Map.Entry<String, String[]> entry : 
			  request.get().getParameterMap().entrySet()) {
			  for (String value : entry.getValue()) {
				  parameters.put(entry.getKey(), value);
			  }
		}
		
		parameters.remove("page");
		parameters.remove("size");
	    
		Resource<SearchResult<User>> linkBuilder = ControllerLinkBuilder.methodOn(UserController.class).searchUser(null, null, null ,result.getPage(), result.getSize());
		Link selfLink = ControllerLinkBuilder.linkTo(linkBuilder).withSelfRel();
	    resource.add(selfLink.expand(parameters));
	    
	    if(result.getNextPage() != -1) {
	    	Resource<SearchResult<User>> nextLinkBuilder = ControllerLinkBuilder.methodOn(UserController.class).searchUser(null, null, null, result.getNextPage(), result.getSize());
	    	Link nextLink = ControllerLinkBuilder.linkTo(nextLinkBuilder).withRel("next");
	    	resource.add(nextLink.expand(parameters));
	    }
	    
	    if(result.getPreviousPage() != -1) {
	    	Resource<SearchResult<User>> prevLinkBuilder = ControllerLinkBuilder.methodOn(UserController.class).searchUser(null, null, null, result.getPreviousPage(), result.getSize());
	    	Link prevLink = ControllerLinkBuilder.linkTo(prevLinkBuilder).withRel("prev");
	    	resource.add(prevLink.expand(parameters));
	    }
	    
	    Resource<SearchResult<User>> firstLinkBuilder = ControllerLinkBuilder.methodOn(UserController.class).searchUser(null, null, null, 0, result.getSize());
	    Link firstLink = ControllerLinkBuilder.linkTo(firstLinkBuilder).withRel("first");
	    resource.add(firstLink.expand(parameters));
	    
	    Resource<SearchResult<User>> lastLinkBuilder = ControllerLinkBuilder.methodOn(UserController.class).searchUser(null, null, null, result.getTotalPages() - 1, result.getSize());
	    Link lastLink = ControllerLinkBuilder.linkTo(lastLinkBuilder).withRel("last");
	    resource.add(lastLink.expand(parameters));

	    List<ResourceSupport> userResources= new ArrayList<>();
	    for( User user : result.getResults()) {
	    	userResources.add(userResourceProcessor.process(new Resource<User>(user)));
	    }
	    
	    Resources<ResourceSupport> resources  = new Resources<>(userResources);
	   
	 
	    
	    return resource;
	 }
}