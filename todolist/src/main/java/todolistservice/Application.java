package todolistservice;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.cache.interceptor.KeyGenerator;
import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.providers.xmemcached.XMemcachedConfiguration;
import com.google.code.ssm.spring.SSMCache;
import com.google.code.ssm.spring.SSMCacheManager;

@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration
@EntityScan(basePackages = {"todolistservice.entities"})
@EnableJpaRepositories(basePackages = { "todolistservice.repositories" })
@ComponentScan(basePackages = {"todolistservice.entities", "todolistservice.repositories", "todolistservice.controllers", "todolistservice.config", "todolistservice.exceptions"})
@EnableFeignClients
//@EnableCaching
 
@EnableAspectJAutoProxy
//@ImportResource("classpath:simplesm-context.xml")
public class Application extends CachingConfigurerSupport {
	
	private static final String LOCALHOST = "127.0.0.1:11211";

	private static final String DEFAULT_CACHE = "default";

	private static final String USER_TODOLIST_CACHE = "usertodolistcache";

	public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }
    
    
    @Bean
	public TodoListResourceProcessor todoListResourceProcessor() {
		return new TodoListResourceProcessor(
				
				
				);
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
    
    /*
    @Bean
    @Override
    public CacheManager cacheManager() {
        Set<SSMCache> ssmCacheSet = new HashSet<>();
        SSMCache deafaultCache = new SSMCache(defaultCache(), 300, true);
        SSMCache userTodolistCache = new SSMCache(createCache(USER_TODOLIST_CACHE), 300, true);

        ssmCacheSet.add(deafaultCache);
        ssmCacheSet.add(userTodolistCache);
        

        SSMCacheManager ssmCacheManager = new SSMCacheManager();
        ssmCacheManager.setCaches(ssmCacheSet);
        
        return ssmCacheManager;
    }

    @Bean
    @DependsOn("cacheBase")
    public CacheFactory cacheFactory() {
        return cacheFactory(DEFAULT_CACHE);
    }

    @Bean
    public Cache defaultCache() {
        try {
            return cacheFactory().getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Bean
    public KeyGenerator keyGenerator() {
      return new KeyGenerator() {
        @Override
        public Object generate(Object o, Method method, Object... params) {
          StringBuilder sb = new StringBuilder();
          sb.append(o.getClass().getName());
          sb.append(method.getName());
          for (Object param : params) {
            sb.append(param.toString());
          }
          return sb.toString();
        }
      };
    }
    
    private Cache createCache(final String cacheName)
    {
    	 try {
             return cacheFactory(cacheName).getObject();
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
    }
    
    
    private CacheFactory cacheFactory(final String cacheName) {
    	CacheFactory cacheFactory = new CacheFactory();
        cacheFactory.setCacheName(cacheName);
        cacheFactory.setCacheClientFactory(new MemcacheClientFactoryImpl());
      
   
        
        String server = LOCALHOST;

        XMemcachedConfiguration cacheConfiguration = createCacheConfiguration(server);
        cacheFactory.setAddressProvider(new DefaultAddressProvider(server));
        cacheFactory.setConfiguration(cacheConfiguration);

        return cacheFactory;
    }
    

    private XMemcachedConfiguration createCacheConfiguration(final String server) {
        XMemcachedConfiguration cacheConfiguration = new XMemcachedConfiguration();
        cacheConfiguration.setConsistentHashing(true);
        cacheConfiguration.setUseBinaryProtocol(true);
        return cacheConfiguration;
    }*/
}
