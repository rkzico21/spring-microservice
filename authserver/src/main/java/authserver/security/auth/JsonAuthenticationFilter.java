package authserver.security.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import authserver.entities.User;

public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter  {
    private boolean postOnly;
    
    public JsonAuthenticationFilter(String url ,AuthenticationManager authManager) {
    	 super(new AntPathRequestMatcher(url));
    	 setAuthenticationManager(authManager);
      }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    if (postOnly && !request.getMethod().equals("POST")) {
        throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }
    
    
    try {
		 UsernamePasswordAuthenticationToken authRequest  = getUserNamePasswordAuthenticationToken(request);
		 //setDetails(request, authRequest);
		 return this.getAuthenticationManager().authenticate(authRequest);
		} catch (Throwable t) {
			
		}
		
	throw new AuthenticationServiceException("Authentication failed");
   }
    
    private UsernamePasswordAuthenticationToken getUserNamePasswordAuthenticationToken(HttpServletRequest request)  throws IOException{
        User user = null;

    	try {
    		user = new ObjectMapper().readValue(request.getInputStream(), User.class);
    		System.out.println("User name is "+ user.getUsername());
    		System.out.println("Password is "+ user.getPassword());
    	} catch(Throwable t){		
            throw new IOException(t.getMessage(), t);
        }

        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }
 }