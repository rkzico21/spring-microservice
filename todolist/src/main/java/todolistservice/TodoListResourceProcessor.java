package todolistservice;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import todolistservice.controllers.TodoListController;
import todolistservice.entities.TodoListItem;

@Component
public class TodoListResourceProcessor implements ResourceProcessor<Resource<TodoListItem>> {
	
	TodoListResourceProcessor()
	{
		
	}
	
    @Override
    public Resource<TodoListItem> process(Resource<TodoListItem> resource) {

        TodoListItem item = resource.getContent();
        try {
        	Resource<TodoListItem> linkBuilder;
    		linkBuilder = ControllerLinkBuilder.methodOn(TodoListController.class).getItem(item.getId());
			Link selfLink = ControllerLinkBuilder.linkTo(linkBuilder).withSelfRel();
	        resource.add(selfLink);
		} catch (Throwable e) {
		
		}
        return resource;
    }
}
