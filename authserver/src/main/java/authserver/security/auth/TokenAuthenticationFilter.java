package authserver.security.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import authserver.security.TokenHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fan.jin on 2016-10-19.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    UserDetailsService userDetailsService;

    /*
     * The below paths will get ignored by the filter
     */
    public static final String ROOT_MATCHER = "/";
    public static final String LOGIN_MATCHER = "/auth/login";
    public static final String LOGOUT_MATCHER = "/auth/logout";

    private List<String> pathsToSkip = Arrays.asList(
            ROOT_MATCHER,
            LOGIN_MATCHER,
            LOGOUT_MATCHER
    );

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {


        String authToken = tokenHelper.getToken(request);
       // if (authToken != null && !skipPathRequest(request, pathsToSkip)) {
        //System.out.println("auth token is" + authToken);
        
        
        if (authToken != null) {
                
        // get username from token
            try {
                String username = tokenHelper.getUsernameFromToken(authToken);
                System.out.println("User name is " + username);
                
                // get user
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // create authentication
                TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                authentication.setToken(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // SecurityContextHolder.getContext().setAuthentication(new AnonAuthentication());
            }
        } else {
            
        }

        chain.doFilter(request, response);
    }

   

}