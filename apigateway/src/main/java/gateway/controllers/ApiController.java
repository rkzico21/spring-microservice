package gateway.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gateway.domain.ApiResource;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@RestController
public class ApiController {

	@RequestMapping("/api")
	public Resource<ApiResource> index() {
		Resource<ApiResource> apiResource = new Resource<ApiResource>(new ApiResource()); 
		Link  userServiceLink = ControllerLinkBuilder.linkTo(ApiController.class).slash("api").slash("userservice").withRel("user_service");
		apiResource.add(userServiceLink);
		
		Link  todolistServicelink = ControllerLinkBuilder.linkTo(ApiController.class).slash("api").slash("todolistservice").withRel("todolist_service");
		apiResource.add(todolistServicelink);
		
		Link  meetingServicelink = ControllerLinkBuilder.linkTo(ApiController.class).slash("api").slash("meetingservice").withRel("meeting_service");
		apiResource.add(meetingServicelink);
		
		return apiResource;
	}
	
	
	
}


