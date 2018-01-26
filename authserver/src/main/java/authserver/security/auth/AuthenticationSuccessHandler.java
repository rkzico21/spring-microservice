package authserver.security.auth;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.databind.ObjectMapper;

import authserver.entities.User;
import authserver.entities.UserPrincipal;
import authserver.entities.UserTokenState;
import authserver.repositories.UserRepository;
import authserver.security.TokenHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Value("${jwt.refresh_token_expires_in}")
    private int REFRESH_EXPIRES_IN;
    
    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

	@Value("${app.user_cookie}")
	private String USER_COOKIE;

	@Autowired
	TokenHelper tokenHelper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DefaultTokenServices defaultTokenServices;
	
	@Autowired
	ObjectMapper objectMapper;
	
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";
    

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication ) throws IOException, ServletException {
		
		
		clearAuthenticationAttributes(request);
		
		
		OAuth2AccessToken token;
		try {
			
			//token = generateOauth2AccessToken(authentication.getName(), "");
			token = generateOauth2AccessToken(authentication);
			String jwtResponse = objectMapper.writeValueAsString( token );
			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			
			
			//NOTE: Use cookie if necessary. 
			response.getWriter().write( jwtResponse );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private OAuth2AccessToken generateOauth2AccessToken(Authentication authentication) throws Exception {
		
	//private OAuth2AccessToken generateOauth2AccessToken(String userName, String password) throws Exception {
		HashMap<String, String> requestParameters = new HashMap<String, String>();
		requestParameters.put("scope", "read");
		requestParameters.put("username", authentication.getName());
		requestParameters.put("client_id", "apiClient");
		requestParameters.put("grant", "password");
		    
		    
		   
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		    
		    Set<String> responseType = new HashSet<String>();
		    responseType.add("password");

		    Set<String> scopes = new HashSet<String>();
		    //scopes.add("read");
		    //scopes.add("write");

		    OAuth2Request authorizationRequest = new OAuth2Request(
		    		requestParameters, "apiClient",
		            authorities, true,scopes, null, "",
		            responseType, null);

		    
		    User user = userRepository.findUserByName(authentication.getName());
		    UserPrincipal userPrincipal = new UserPrincipal(user);
	        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);

		    OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
		    authenticationRequest.setAuthenticated(true);
		    

		    OAuth2AccessToken accessToken = defaultTokenServices.createAccessToken(authenticationRequest);
		   return accessToken;
	}
}
