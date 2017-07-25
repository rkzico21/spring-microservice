package gateway;


import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import java.util.stream.Stream;


@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    
    /*@Bean
    UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer(SpringClientFactory springClientFactory) {
        return template -> {
            AccessTokenProviderChain accessTokenProviderChain = Stream
                    .of(
                            new AuthorizationCodeAccessTokenProvider(),
                            new ImplicitAccessTokenProvider(),
                            new ResourceOwnerPasswordAccessTokenProvider(),
                            new ClientCredentialsAccessTokenProvider())
                    .peek(tp -> tp.setRequestFactory(new RibbonClientHttpRequestFactory(springClientFactory)))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), AccessTokenProviderChain::new));
            template.setAccessTokenProvider(accessTokenProviderChain);
        };
    }*/
    
    
  }
