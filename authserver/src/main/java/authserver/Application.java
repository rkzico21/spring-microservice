package authserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@SpringBootApplication
@EnableEurekaClient
public class Application {
   
	@Value("${jwt.expires_in}")
    private int EXPIRES_IN;
	
	 @Value("${jwt.refresh_token_expires_in}")
	 private int REFRESH_EXPIRES_IN;
	
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    
    @Bean
    public DefaultTokenServices defaultTokenServices() throws Exception {
    	
    	InMemoryClientDetailsServiceBuilder builder = new  ClientDetailsServiceBuilder<>().inMemory();
		builder.withClient("gateway")
		.secret("secret")
		.scopes("read", "write")
	    .autoApprove(true)
        .accessTokenValiditySeconds(EXPIRES_IN)
        .refreshTokenValiditySeconds(REFRESH_EXPIRES_IN)
        
        .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code");
	
		ClientDetailsService clientDetailService =	builder.build();
		
    	
    	DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setAccessTokenValiditySeconds(EXPIRES_IN); 
        defaultTokenServices.setRefreshTokenValiditySeconds(REFRESH_EXPIRES_IN);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setClientDetailsService(clientDetailService);
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        return defaultTokenServices;
    }
    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
        return converter;
    }
	
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

}
