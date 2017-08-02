package gateway.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   @Override
    protected void configure(HttpSecurity http) throws Exception {	
    	    
    	http
    	.authorizeRequests()
    	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().and()
    	.logout().and()
    	.csrf().disable();
    }
 }


