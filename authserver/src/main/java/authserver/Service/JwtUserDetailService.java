package authserver.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import authserver.entities.User;

@Service
public class JwtUserDetailService implements UserDetailsService {
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	 System.out.println("##########user name in user detail service ############### "+ username);
 		
    	
    	User user = (username.equals("admin") || username.equals("user")) ? new User() : null;
    	
    	if (user == null) {
            System.out.println("##########No user found with name ############### "+ username);
    		throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            user.setUsername(username);  
        	user.setFirstname(username);
        	user.setLastname("user");
        	user.setPassword("userPass");
        	return user;
        	}
    	}
}