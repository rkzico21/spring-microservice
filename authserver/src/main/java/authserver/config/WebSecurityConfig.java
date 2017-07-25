package authserver.config;


import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

//import com.bfwg.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import authserver.Service.JwtUserDetailService;
import authserver.security.auth.AuthenticationFailureHandler;
import authserver.security.auth.AuthenticationSuccessHandler;
import authserver.security.auth.JsonAuthenticationFilter;
import authserver.security.auth.RestAuthenticationEntryPoint;
import authserver.security.auth.TokenAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

    @Bean
    public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public JsonAuthenticationFilter JsonLoginAuthenticationFilter() throws Exception {
		return new JsonAuthenticationFilter("/auth/login",authenticationManager());
	}
    
    @Autowired
    private JwtUserDetailService jwtUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    //@Autowired
    //private LogoutSuccess logoutSuccess;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
     	auth.userDetailsService(jwtUserDetailsService);

        auth.inMemoryAuthentication()
         .withUser("admin").password("admin").roles("ADMIN")
         .and()
         .withUser("user").password("userPass").roles("USER");
 
    }

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
             http
                .csrf().disable() 
               . authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll().and()
                .authorizeRequests()
                .anyRequest()
                .authenticated().and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler).and()
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
                .exceptionHandling()
                .authenticationEntryPoint( restAuthenticationEntryPoint ).and()
                //.addFilterBefore(JsonLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class)
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
                .deleteCookies(TOKEN_COOKIE);

    }
}



