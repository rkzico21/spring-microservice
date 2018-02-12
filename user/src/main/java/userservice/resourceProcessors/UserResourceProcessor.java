package userservice.resourceProcessors;


import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;
import userservice.controllers.UserController;
import userservice.dtos.User;
import userservice.integration.TodolistIntegration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserResourceProcessor implements ResourceProcessor<Resource<User>> {

	private static final String X_FORWARDED_HOST = "X-Forwarded-Host";
	private static final String X_FORWARDED_PREFIX = "X-Forwarded-Prefix";
	
	private final TodolistIntegration todolistIntegration;
	
	private final Provider<HttpServletRequest> request;

	@Override
	public Resource<User> process(Resource<User> resource) {

		User user = resource.getContent();
		
		Resource<User> linkBuilder = ControllerLinkBuilder.methodOn(UserController.class).getUser(user.getId());
	    Link selfLink = ControllerLinkBuilder.linkTo(linkBuilder).withSelfRel();
	    resource.add(selfLink);
		
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("userid", String.format("%d", user.getId()));
		
		String host = this.request.get().getHeader(X_FORWARDED_HOST);
		String prefix = this.request.get().getHeader(X_FORWARDED_PREFIX);
		if(prefix != null)
		   prefix = prefix.replace("user", "todolist");
		
		Link link = this.todolistIntegration.getTodolistsByUserLink(parameters, host, prefix);
		if (link != null) {
			resource.add(link.withRel("todolist"));
		}

		return resource;
	}
}