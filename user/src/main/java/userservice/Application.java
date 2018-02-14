package userservice;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import userservice.entities.RoleEntity;
import userservice.entities.UserEntity;
import userservice.repositories.UserRepository;
import userservice.repositories.RoleRepository;
import org.modelmapper.ModelMapper;

@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration
@EntityScan(basePackages = {"userservice.entities"})
@EnableJpaRepositories(basePackages = { "userservice.repositories" })
@ComponentScan(basePackages = {"userservice","userservice.entities", "userservice.repositories", "userservice.controllers", "userservice.config", "userservice.integration", "userservice.exceptions"})
@EnableSwagger2
public class Application {
    
	@Autowired
	UserRepository repository;
	
	@Autowired
	RoleRepository roleRepository;
	
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
   @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select() 
          .apis(RequestHandlerSelectors.basePackage("userservice.controllers"))
          .paths(PathSelectors.any())                          
          .build();                                           
    }
  
    
    @PostConstruct
    public void loadIntitialData() {
    	UserEntity admin = repository.findUserByName("admin");
    	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	//if(admin == null)
    	//{
    		RoleEntity roleAdmin = new RoleEntity(1L, "admin");
    		roleRepository.save(roleAdmin);
    		
    		admin = new UserEntity(1L, "admin", "admin", "admin@example.com", passwordEncoder.encode("admin"));
    		admin.getRoles().add(roleAdmin);
    		repository.save(admin);
    	//}
    	
    	UserEntity user = repository.findUserByName("user");
    	//if(user == null)
    	//{
    		RoleEntity roleUserList = new RoleEntity(2L, "user_list");
    		roleRepository.save(roleUserList);
    		
    		RoleEntity roleUserCreate = new RoleEntity(3L, "user_create");
    		roleRepository.save(roleUserCreate);
    		
    		RoleEntity roleUserUpdate= new RoleEntity(4L, "user_update");
    		roleRepository.save(roleUserUpdate);
    		
    		RoleEntity roleUserRead= new RoleEntity(5L, "user_read");
    		roleRepository.save(roleUserRead);
    		
    		RoleEntity roleTodoListRead = new RoleEntity(6L, "todolist_read");
    		roleRepository.save(roleTodoListRead);
    		
    		RoleEntity roleTodoListItemCreate = new RoleEntity(7L, "todolistItem_create");
    		roleRepository.save(roleTodoListItemCreate);
    		
    		RoleEntity roleTodoListItemDelete = new RoleEntity(8L, "todolistItem_delete");
    		roleRepository.save(roleTodoListItemDelete);
    		
    		user = new UserEntity(2L, "user", "user", "user@example.com", passwordEncoder.encode("user"));
    		user.getRoles().add(roleUserRead);
    		
    		user.getRoles().add(roleTodoListRead);
    		user.getRoles().add(roleTodoListItemCreate);
    		user.getRoles().add(roleTodoListItemDelete);
    		repository.save(user);
    	
    		
    	
    		
    		user = new UserEntity(3L, "user1", "user1", "user@example.com", passwordEncoder.encode("user1"));
    		user.getRoles().add(roleUserList);
    		user.getRoles().add(roleUserRead);
    		user.getRoles().add(roleUserCreate);
    		user.getRoles().add(roleUserUpdate);
    		user.getRoles().add(roleTodoListRead);
    		repository.save(user);
    }
 }
