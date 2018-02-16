package todolistservice.resourceprocessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import todolistservice.controllers.TodoListController;
import todolistservice.dtos.PageResult;
import todolistservice.entities.TodoListItem;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodolistPageResourceProcessor implements ResourceProcessor<ResourceWithEmbeddable<PageResult<TodoListItem>>> {

	@Autowired
	private TodoListResourceProcessor todoListResourceProcessor;
	
	@Autowired
	private final Provider<HttpServletRequest> request;
	
	@Override
	public ResourceWithEmbeddable<PageResult<TodoListItem>> process(ResourceWithEmbeddable<PageResult<TodoListItem>> resource) {
		
		PageResult<TodoListItem> result = resource.getContent();
		
	    
		Map<String, Object> parameters = new HashMap<>();
		
		for (Map.Entry<String, String[]> entry : 
			  request.get().getParameterMap().entrySet()) {
			  for (String value : entry.getValue()) {
				  parameters.put(entry.getKey(), value);
			  }
		}
		
		
	    
		Resource<PageResult<TodoListItem>> linkBuilder;
		try {
			linkBuilder = ControllerLinkBuilder.methodOn(TodoListController.class).index(null ,result.getPage(), result.getSize());
		
		Link selfLink = ControllerLinkBuilder.linkTo(linkBuilder).withSelfRel();
	    resource.add(selfLink.expand(parameters));
	    
	    if(result.getNextPage() != -1) {
	    	Resource<PageResult<TodoListItem>> nextLinkBuilder = ControllerLinkBuilder.methodOn(TodoListController.class).index(null, result.getNextPage(), result.getSize());
	    	Link nextLink = ControllerLinkBuilder.linkTo(nextLinkBuilder).withRel("next");
	    	resource.add(nextLink.expand(parameters));
	    }
	    
	    if(result.getPreviousPage() != -1) {
	    	Resource<PageResult<TodoListItem>> prevLinkBuilder = ControllerLinkBuilder.methodOn(TodoListController.class).index(null, result.getPreviousPage(), result.getSize());
	    	Link prevLink = ControllerLinkBuilder.linkTo(prevLinkBuilder).withRel("prev");
	    	resource.add(prevLink.expand(parameters));
	    }
	    
	    Resource<PageResult<TodoListItem>> firstLinkBuilder = ControllerLinkBuilder.methodOn(TodoListController.class).index(null, 0, result.getSize());
	    Link firstLink = ControllerLinkBuilder.linkTo(firstLinkBuilder).withRel("first");
	    resource.add(firstLink.expand(parameters));
	    
	    Resource<PageResult<TodoListItem>> lastLinkBuilder = ControllerLinkBuilder.methodOn(TodoListController.class).index(null, result.getTotalPages() - 1, result.getSize());
	    Link lastLink = ControllerLinkBuilder.linkTo(lastLinkBuilder).withRel("last");
	    resource.add(lastLink.expand(parameters));

	    List<ResourceSupport> todolistItemResources= new ArrayList<>();
	    for( TodoListItem item : result.getResults()) {
	    	todolistItemResources.add(todoListResourceProcessor.process(new Resource<TodoListItem>(item)));
	    }
	    
	    Resources<ResourceSupport> resources  = new Resources<>(todolistItemResources);
	   
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			
		}
	    
	    return resource;
	 }
}