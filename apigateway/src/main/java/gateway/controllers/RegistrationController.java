package gateway.controllers;

import org.springframework.web.bind.annotation.RestController;

import gateway.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;



@RestController
@RequestMapping("/user/register")
public class RegistrationController {
    
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   
   @PreAuthorize("hasAuthority('admin')")
   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void registerUser(@RequestBody User entity) {
    	logger.info(String.format("Adding new user. User:%s", entity));
        
    }
}
    
    
    