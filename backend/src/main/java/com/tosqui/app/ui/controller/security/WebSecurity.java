package com.tosqui.app.ui.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.tosqui.app.auth.AuthEntryPointJwt;
import com.tosqui.app.auth.AuthTokenFilter;
import com.tosqui.app.service.UserService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	
	private final UserService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired UserService userService;
	@Autowired private AuthenticationFailureHandler authenticationFailureHandler;
	@Autowired private AuthEntryPointJwt unauthorizedHandler;

		
	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder ) { 
		this.userDetailsService=userDetailsService; 
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.cors().configurationSource(corsConfigurationSource())
        .and()
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
		.and()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/api/auth/signin").permitAll()
		.antMatchers(HttpMethod.POST,"/api/users").permitAll()
		.antMatchers(HttpMethod.GET,"/api/users/verify/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.failureHandler(authenticationFailureHandler)
		.and()
		.logout().permitAll().deleteCookies("JSESSIONID")
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	}
	
	
	
	
	public void configure (AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	
    @EventListener
    public void authSuccessEventListener(AuthenticationSuccessEvent successEvent){
    	userService.resetUserFailedCounterOnValidLogin(successEvent.getAuthentication().getName());		
    }
    
    @EventListener
    public void authFailedEventListener(AbstractAuthenticationFailureEvent failureEvent){
    	userService.incrementUserFailedCounterOnValidLogin(failureEvent.getAuthentication().getName());		
    }
    
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
   	      return super.authenticationManagerBean();
   	  }
    
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	

	 @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.addAllowedOrigin("*");
	        configuration.addAllowedHeader("*");
	        configuration.addAllowedMethod("*");
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }

	
   
}
	
	


