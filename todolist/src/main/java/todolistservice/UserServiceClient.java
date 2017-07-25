package todolistservice;
 
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




@FeignClient("userservice")
public interface UserServiceClient {
 
	@RequestMapping(method = RequestMethod.GET , value = "/user/{id}")
    User getUser(@RequestHeader("Authorization") String authorizationToken, @PathVariable("id") Long id) throws todolistservice.exceptions.UserNotFoundException;
}

