package todolistservice;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import todolistservice.controllers.TodoListController;
import todolistservice.controllers.TodoListItemController;
import todolistservice.entities.TodoList;
import todolistservice.entities.TodoListItem;

@Component
public class TodoListResourceProcessor implements ResourceProcessor<Resource<TodoList>> {
	
	TodoListResourceProcessor()
	{
		
	}
	
    @Override
    public Resource<TodoList> process(Resource<TodoList> todoListResource) {

        TodoList todoList = todoListResource.getContent();
        Resource<TodoList> linkBuilder = ControllerLinkBuilder.methodOn(TodoListController.class).getTodoList(todoList.getId());
        Link selfLink = ControllerLinkBuilder.linkTo(linkBuilder).withSelfRel();
        todoListResource.add(selfLink);
    	 
        Resources<Resource<TodoListItem>> itemsLinkBuilder = ControllerLinkBuilder.methodOn(TodoListItemController.class).index(todoList.getId());        
        Link itemsLink = ControllerLinkBuilder.linkTo(itemsLinkBuilder).withRel("items");
        todoListResource.add(itemsLink);
        
        /*URI userUri = this.profilesClient.getProfileUri(todoList);

        if (null != userUri) {
            Link userLink = new Link(userUri.toString(), "user");
            todoListResource.add(userLink);
        }*/
        return todoListResource;
    }
}
