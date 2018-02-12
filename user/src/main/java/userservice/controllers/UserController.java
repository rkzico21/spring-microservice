package userservice.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import static userservice.resourceProcessors.ResourceWithEmbeddable.*;

import userservice.resourceProcessors.ResourceWithEmbeddable;
import userservice.resourceProcessors.UserResourceProcessor;
import userservice.resourceProcessors.UserSearchResourceProcessor;
import userservice.dtos.SearchResult;
import userservice.dtos.User;
import userservice.dtos.UserSearchQuery;
import userservice.exceptions.UserNotFoundException;


import userservice.services.UserService;

@RestController
@RequestMapping("/user")
@Api(value = "user", tags = "User API")
@ExposesResourceFor(User.class)
public class UserController  implements ResourceProcessor<RepositoryLinksResource>{
    
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   
   @Autowired
   private UserService service;
  
   @Autowired
   private UserResourceProcessor userResourceProcessor;
   
   @Autowired
   private UserSearchResourceProcessor userSearchResourceProcessor;
   
   
   @ApiOperation(nickname="getUsers", value="getUsers" ,tags = "Get User")
   @PreAuthorize("hasAuthority('admin') or hasAuthority('user_list')")
   @RequestMapping(method = RequestMethod.GET)
   public Resources<Resource<User>> index() {
    
	   logger.info("Get all the users");
       Iterable<User> users = service.findAll();
       
       List<Resource<User>> userResourceList = new ArrayList<>();
    
       for( User user : users ) {
    	   userResourceList.add(userResourceProcessor.process(new Resource<User>(user)));
        }
    	
		Resources<Resource<User>> userResources =  new Resources<>(userResourceList);
		userResources.add(ControllerLinkBuilder.linkTo(UserController.class).withSelfRel());
		
		return userResources;
    }
   
   
   
   @PreAuthorize("hasAuthority('admin') or hasAuthority('user_read')")
   @RequestMapping(method = RequestMethod.GET, value= "/{id:\\d+}")
   public Resource<User> getUser(@PathVariable("id") Long id) throws UserNotFoundException{
        logger.info(String.format("Finding user with id: %d", id));
    	
    	User user = service.findOne(id);
    	if (user == null) 
    	{
    		logger.warn(String.format("No user found with id: %d", id));
    		throw new UserNotFoundException("id", id.toString());
        }
    	
    	logger.debug(String.format("Finding user with id: %d, User:%s", id, user));
    	return userResourceProcessor.process(new Resource<User>(user));
    }
   
   
   
   @PreAuthorize("hasAuthority('admin') or hasAuthority('user_read')")
   @RequestMapping(method = RequestMethod.GET, params = "name")
   public Resource<User> getUserByName(@RequestParam(value = "name", required = false) String name) throws UserNotFoundException{
        logger.info(String.format("Finding user with name: %s", name));
    	
    	User user = service.findByName(name);
    	if (user == null) 
    	{
    		logger.warn(String.format("No user found with name: %s", name));
    		throw new UserNotFoundException("name", name);
        }
    	
    	logger.debug(String.format("Finding user with name: %s, User:%s", name, user));
    	return userResourceProcessor.process(new Resource<User>(user));
    }
    
    
   
    @PreAuthorize("hasAuthority('admin') or hasAuthority('user_create')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<User> registerUser(@Valid @RequestBody User userdto) {
    	
        logger.info(String.format("Adding new user. User:%s", userdto));
        User user = service.add(userdto);
       
        logger.info(String.format("New user created. User:%s", user));
        return userResourceProcessor.process(new Resource<User>(user));
    }
    
    
    
    @PreAuthorize("hasAuthority('admin') or hasAuthority('user_list')")
    @RequestMapping(method = RequestMethod.GET, value= "/search")
    @ResponseStatus(HttpStatus.OK)
    public Resource<SearchResult<User>> searchUser(@RequestParam(value = "fullname", required = false) String fullName,
    		@RequestParam(value = "department", required = false) String department,
    		@RequestParam(value = "designation", required = false) String designation,
    		@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
    		@RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
    	
    	
    	UserSearchQuery query = new UserSearchQuery(fullName, department, designation, page, size);
    	logger.info(String.format("Get users with parameter %s", query.toString()));
    	SearchResult<User> userSearchResult = service.search(query);
    	
    	List<ResourceSupport> userResources= new ArrayList<>();
	    for( User user : userSearchResult.getResults()) {
	    	userResources.add(userResourceProcessor.process(new Resource<User>(user)));
	    }
    	
	    ResourceWithEmbeddable<SearchResult<User>> userSearchResultResource = embeddedRes(userSearchResult, resWrapper(userResources, "users"));
    	return userSearchResourceProcessor.process(userSearchResultResource);
    }
    
    
   

	@Override
	public RepositoryLinksResource process(RepositoryLinksResource repositoryLinksResource) {
		
		repositoryLinksResource.add(ControllerLinkBuilder.linkTo(UserController.class).withRel("users"));
		repositoryLinksResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).getUser(null)).withRel("userById"));
		repositoryLinksResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).getUserByName(null)).withRel("userByName"));
		repositoryLinksResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).searchUser(null, null, null, null, null)).withRel("search"));
		return repositoryLinksResource;
	}
}
