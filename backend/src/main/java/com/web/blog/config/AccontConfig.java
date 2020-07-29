package com.web.blog.config;

import com.web.blog.common.LoggingFilter;
import com.web.blog.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class AccontConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  AccountService accountService;

  public SecurityExpressionHandler expressionHandler() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

    DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);

    return handler;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception{
    http.addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class);

    http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .mvcMatchers("/","/account/**","/signup").permitAll()
            .mvcMatchers("/admin").hasRole("ADMIN")
            .mvcMatchers("/user","/username").hasRole("USER")
            .anyRequest().authenticated()
            .expressionHandler(expressionHandler());
    http.formLogin()
            .loginPage("/login")
            .permitAll();
    http.rememberMe()
            .userDetailsService(accountService)
            .key("remember-me-sample");
    http.httpBasic();
    http.cors().and();


    http.logout()
            .logoutSuccessUrl("/");

    http.exceptionHandling()
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
              String username = principal.getUsername();
              System.out.println(username + " is denied to access " + request.getRequestURI());
              response.sendRedirect("/access-denied");
            });

    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

  }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // - (3)
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
