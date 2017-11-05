package authserver.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import authserver.Service.JwtUserDetailService;
import authserver.security.auth.AuthenticationFailureHandler;
import authserver.security.auth.AuthenticationSuccessHandler;
import authserver.security.auth.RestAuthenticationEntryPoint;
import authserver.security.auth.TokenAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	 public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/login";
	 public static final String TOKEN_REFRESH_ENTRY_POINT = "/token";
	

    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

    @Bean
    public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
        return new TokenAuthenticationFilter();
    }

    @Autowired
    private JwtUserDetailService userDetailService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    //@Autowired
    //private LogoutSuccess logoutSuccess;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
     	//auth.userDetailsService(jwtUserDetailsService);

     	auth.authenticationProvider(authenticationProvider());
     	/*auth.inMemoryAuthentication()
         .withUser("admin").password("admin").roles("ADMIN")
         .and()
         .withUser("user").password("userPass").roles("USER");
 */
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
          = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }
     
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
             http
                .cors().and()
                .csrf().disable() 
               . authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()
                .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll().and()
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
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies(TOKEN_COOKIE);

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
}



