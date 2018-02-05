package meetingservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableResourceServer
public class MeetingConfiguration extends ResourceServerConfigurerAdapter {
	
	@Autowired
	TokenStore tokenStore;

	@Autowired
	JwtAccessTokenConverter tokenConverter;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
			http
			  .cors().and()
			  .csrf().disable()
			  .authorizeRequests()
			  .antMatchers("/").permitAll()
			  .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			  .antMatchers("/**")
              .authenticated(); //requires for authentication
              //.permitAll();
			
	}
	
	
	 @Bean
     CorsConfigurationSource corsConfigurationSource() {
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.addAllowedOrigin("*");
			configuration.setAllowCredentials(true);
			configuration.addAllowedMethod("*");
			configuration.addAllowedHeader("*");
			
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", configuration);
			return source;
		}

     @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
         resources.resourceId("meeting").tokenStore(tokenStore);;
    }
 }
