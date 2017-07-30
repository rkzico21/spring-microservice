package authserver.security.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.databind.ObjectMapper;

import authserver.entities.User;
import authserver.entities.UserTokenState;
import authserver.security.TokenHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

	@Value("${app.user_cookie}")
	private String USER_COOKIE;

	@Autowired
	TokenHelper tokenHelper;

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
			
			token = generateOauth2AccessToken(authentication.getName(), "");
			String value = token.getValue();
			
			UserTokenState userTokenState = new UserTokenState(value, EXPIRES_IN);
			String jwtResponse = objectMapper.writeValueAsString( userTokenState );
			response.setContentType("application/json");
			response.getWriter().write( jwtResponse );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private OAuth2AccessToken generateOauth2AccessToken(String user, String password) throws Exception {
		
		
		InMemoryClientDetailsServiceBuilder builder = new  ClientDetailsServiceBuilder<>().inMemory();
		
		builder.withClient("gateway")
		.secret("secret")
		.scopes("read", "write")
	    .autoApprove(true)
        .accessTokenValiditySeconds(600)
        .refreshTokenValiditySeconds(600)
        .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code");
	
		ClientDetailsService clientDetailService =	builder.build();
		
		HashMap<String, String> authorizationParameters = new HashMap<String, String>();
		    authorizationParameters.put("scope", "read");
		    authorizationParameters.put("username", "user");
		    authorizationParameters.put("client_id", "gateway");
		    authorizationParameters.put("grant", "password");
		    
		    
		   
		    Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		    
		    Set<String> responseType = new HashSet<String>();
		    responseType.add("password");

		    Set<String> scopes = new HashSet<String>();
		    scopes.add("read");
		    scopes.add("write");

		    OAuth2Request authorizationRequest = new OAuth2Request(
		            authorizationParameters, "gateway",
		            authorities, true,scopes, null, "",
		            responseType, null);

		    User userPrincipal = new User();DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
	        defaultTokenServices.setTokenStore(tokenStore());
	        defaultTokenServices.setSupportRefreshToken(true);
	        defaultTokenServices.setAccessTokenValiditySeconds(600);
	        defaultTokenServices.setAccessTokenValiditySeconds(600);
	        defaultTokenServices.setReuseRefreshToken(true);
	        defaultTokenServices.setClientDetailsService(clientDetailService);
	        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
	    	
		    userPrincipal.setUsername(user);
		    userPrincipal.setPassword(password);
		    

		    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);

		    OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
		    authenticationRequest.setAuthenticated(true);

		    //AuthorizationServerTokenServices tokenServices = endPoints.getTokenServices();
			OAuth2AccessToken accessToken = defaultTokenServices.createAccessToken(authenticationRequest);	
		    return accessToken;
	}
	
	
    
	public JwtAccessTokenConverter accessTokenConverter() {
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
       // converter.setSigningKey("123");
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
        return converter;
    }
	
	
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
   
    public ClientDetailsServiceConfigurer clientDetailsServiceConfigurer(ClientDetailsServiceBuilder<?> clientDetailsServiceBuilder) throws Exception {
    	
    	ClientDetailsServiceConfigurer clientDetailsServiceConfigurer = new ClientDetailsServiceConfigurer(clientDetailsServiceBuilder);
    	return clientDetailsServiceConfigurer;
    }
    
    
  }
