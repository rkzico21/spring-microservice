package authserver.security.endpoint;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import authserver.Service.JwtUserDetailService;
import authserver.repositories.UserRepository;


/**
 * RefreshTokenEndpoint
 * 
 */
@RestController
public class RefreshTokenEndpoint {
    
    
    @Autowired 
    private JwtUserDetailService service;
    
    @Autowired
    private DefaultTokenServices defaultTokenServices;
    
    @Autowired
    private TokenStore tokenStore;
	 
    /*
     * For api client
     */
    @RequestMapping(value="/token", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE })
    public OAuth2AccessToken refreshToken(@RequestParam("refresh_token") String refreshToken) throws IOException, ServletException {

    	String grantType = "refresh_token";
    	String clientId = "apiClient";
    	String refreshTokenValue = refreshToken;
    	
    	OAuth2RefreshToken refreshToken1 = tokenStore.readRefreshToken(refreshTokenValue);
		OAuth2Authentication autehntication = tokenStore.readAuthenticationForRefreshToken(refreshToken1);
		OAuth2AccessToken token = null;
		try{
    	Set<String> scopes = new HashSet<String>();
	   
    	HashMap<String, String> requestParameters = new HashMap<String, String>();
		requestParameters.put("scope", "read");
		requestParameters.put("username", autehntication.getUserAuthentication().getName());
		requestParameters.put("client_id", clientId);
		requestParameters.put("grant", "password");
	   
		TokenRequest tokenRequest = new TokenRequest(requestParameters, clientId, scopes, grantType);
    	
    	token = defaultTokenServices.refreshAccessToken(refreshTokenValue, tokenRequest);
    	
		}
		catch(Exception ex){
			//ex.printStackTrace();
			//Todo: write log
			
		}
    	return token;
    	
    }
    
    /*
     * For web client
     */
    
    @RequestMapping(value="/token", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public OAuth2AccessToken getRefreshedToken(@CookieValue("refresh_token") String refreshToken) throws IOException, ServletException {

    	return refreshToken(refreshToken);
    }
}
