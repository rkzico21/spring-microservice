package todolistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration
@EntityScan(basePackages = {"todolistservice.entities"})
@EnableJpaRepositories(basePackages = { "todolistservice.repositories" })
@ComponentScan(basePackages = {"todolistservice.entities", "todolistservice.repositories", "todolistservice.controllers", "todolistservice.config", "todolistservice.exceptions", "todolistservice.resourceprocessors"})
@EnableFeignClients
public class Application extends CachingConfigurerSupport {
	public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }
}
