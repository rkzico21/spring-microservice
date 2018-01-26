package authserver;


import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import authserver.entities.Permission;
import authserver.entities.User;
import authserver.repositories.RoleRepository;
import authserver.repositories.UserRepository;

@SpringBootApplication
@EnableEurekaClient
public class Application {
   
	@Autowired
	UserRepository repository;
	
	@Autowired
	RoleRepository roleRepository;
	
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
		builder.withClient("apiClient")
		.secret("secret")
		.scopes("read", "write")
	    .autoApprove(true)
        .accessTokenValiditySeconds(EXPIRES_IN)
        .refreshTokenValiditySeconds(REFRESH_EXPIRES_IN)
        .resourceIds("user")
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
	
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
    
    @PostConstruct
    public void loadIntitialData() {
    	User admin = repository.findUserByName("admin");
    	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	if(admin == null)
    	{
    		Permission roleAdmin = new Permission(1L, "admin");
    		roleRepository.save(roleAdmin);
    		
    		admin = new User(1L, "admin", "admin", "admin@example.com", passwordEncoder.encode("admin"));
    		admin.getRoles().add(roleAdmin);
    		repository.save(admin);
    	}
    	
    	User user = repository.findUserByName("user");
    	if(user == null)
    	{
    		Permission roleUser = new Permission(2L, "user");
    		roleRepository.save(roleUser);
    		
    		user = new User(2L, "user", "user", "user@example.com", passwordEncoder.encode("user"));
    		user.getRoles().add(roleUser);
    		repository.save(user);
    	}
    }
 }
