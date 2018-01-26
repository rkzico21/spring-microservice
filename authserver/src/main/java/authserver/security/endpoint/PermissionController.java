package authserver.security.endpoint;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import authserver.entities.Permission;
import authserver.repositories.UserRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

@RestController
public class PermissionController {
    
    @Autowired
    UserRepository repository;
	
    @RequestMapping(value="/permission", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public Resources<Resource<Permission>> getPermission(@RequestParam("userName") String userName)  {
      Set<Permission> roles =repository.findUserByName(userName).getRoles();
      List<Resource<Permission>> resources = roles.stream().map(role->new Resource<Permission>(role)).collect(Collectors.toList());
      return new Resources<>(resources);  
   }
	 
    
}
