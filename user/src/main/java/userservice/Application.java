package userservice;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration
@EntityScan(basePackages = {"userservice.entities"})
@EnableJpaRepositories(basePackages = { "userservice.repositories" })
@ComponentScan(basePackages = {"userservice.entities", "userservice.repositories", "userservice.controllers", "userservice.config", "userservice.exceptions"})
public class Application {
    
	
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
     }
    
    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("*").allowedHeaders("*");
            }
        };
    }*/
    
    @Bean
    public Filter loggingFilter(){
    	CommonsRequestLoggingFilter  f = new CommonsRequestLoggingFilter() ;

    	f.setIncludeClientInfo(true);
        f.setIncludePayload(true);
        f.setIncludeQueryString(true);
        f.setIncludeHeaders(true);
        
        f.setBeforeMessagePrefix("BEFORE REQUEST  [");
        f.setAfterMessagePrefix("AFTER REQUEST    [");
        f.setAfterMessageSuffix("]\n");
        return f;
    }
}
