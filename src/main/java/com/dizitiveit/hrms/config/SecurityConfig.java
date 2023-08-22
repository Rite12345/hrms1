package com.dizitiveit.hrms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dizitiveit.hrms.service.MyUserDetailsService;
import com.dizitiveit.hrms.util.JwtRequestFilter;
import com.google.common.collect.ImmutableList;

@Configuration

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	 @Bean 
	  public PasswordEncoder passwordEncoder() {
	  
	  PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	  
	  return bCryptPasswordEncoder; 
	  }
	  
	 @Bean 
	  @Override  
	  public AuthenticationManager authenticationManagerBean() throws Exception
	  {
		  return super.authenticationManagerBean(); 
		  }
	  
	  @Autowired 
	  MyUserDetailsService userDetailsService;
	  
	  @Autowired 
	  private JwtRequestFilter jwtRequestFilter;
	  
	  @Override 
	  protected void configure(AuthenticationManagerBuilder auth) throws
	  Exception {
		  auth.userDetailsService(userDetailsService);
	  
	  }
	  
	
	  
	  @Override protected void configure(HttpSecurity http) throws Exception {
	  http.cors().and().csrf().disable(); 
	  http.headers()
	  .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "*")) 
	  .and()
	  .authorizeRequests() 
	  .antMatchers("/employee/**","/company/**","/branch/**","/designation/**","/department/**","/roles/**","/payRollPdf/**","/images/**")
	  .permitAll() 
	  .anyRequest() 
	  .authenticated()
	  .and()
	  .exceptionHandling().and() .sessionManagement()
	  .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	  
	  http.addFilterBefore(jwtRequestFilter,
	  UsernamePasswordAuthenticationFilter.class);
	  
	  } 
	  
	  @Bean
	  public CorsConfigurationSource corsConfigurationSource() {
	      final CorsConfiguration configuration = new CorsConfiguration();
	    //  configuration.setAllowedOrigins(ImmutableList.of("*"));
	      configuration.setAllowedMethods(ImmutableList.of("HEAD",
	              "GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS"));
	      configuration.setAllowedOriginPatterns(ImmutableList.of("*"));
	      // setAllowCredentials(true) is important, otherwise:
	      // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
	      configuration.setAllowCredentials(true);
	      // setAllowedHeaders is important! Without it, OPTIONS preflight request
	       //will fail with 403 Invalid CORS request
	      configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
	      final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	      source.registerCorsConfiguration("/**", configuration);
	      return source;
	  }
	
}
