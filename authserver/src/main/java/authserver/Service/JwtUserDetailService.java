package authserver.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import authserver.entities.User;
import authserver.entities.UserPrincipal;
import authserver.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtUserDetailService implements UserDetailsService {
    
	@Autowired
    private UserRepository userRepository;
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        
        System.out.println("here");
       
        return new UserPrincipal(user);
    }
}