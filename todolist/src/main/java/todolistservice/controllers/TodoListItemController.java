package todolistservice.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import todolistservice.entities.*;
import todolistservice.exceptions.TodolistItemNotFoundException;
import todolistservice.repositories.TodoListItemRepository;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

@RestController
public class TodoListItemController {
   
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   	
   @Autowired
   TodoListItemRepository repository;
	
   @RequestMapping(method = RequestMethod.GET, value="/todolistitem")
   public Resources<Resource<TodoListItem>> index(@RequestParam(value = "todolistid", required = false) Long todolistid) {
    
    	Iterable<TodoListItem> todoListItems = repository.findByTodolistId(todolistid);
    	
    	List<Resource<TodoListItem>> todoListItemResources = new ArrayList<>();
    	
    	for( TodoListItem todoListItem : todoListItems ) {
    		todoListItemResources.add(new Resource<TodoListItem>(todoListItem));
        }
    	
		return new Resources<>(todoListItemResources);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value="/todolistitem/{id}")
    public Resource<TodoListItem> getTodoList(@PathVariable(value="id") Long id) {
    	TodoListItem todoListItem = repository.findOne(id);
    	
    	if(todoListItem == null)
    		throw new TodolistItemNotFoundException(id);
    	
    	return new Resource<TodoListItem>(todoListItem);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/todolistitem")
    public Resource<TodoListItem> createTodoList(@RequestBody TodoListItem entity) {
    	logger.info("Incoming entity is %s", entity);
    	TodoListItem todoListItem = repository.save(entity);
    	return new Resource<TodoListItem>(todoListItem);
    }
    
    
    @RequestMapping(method = RequestMethod.DELETE, value="/todolistitem/{id}")
    public void removeTodoList(@PathVariable(value="id") Long id) {
    	repository.delete(id);
    }
}
