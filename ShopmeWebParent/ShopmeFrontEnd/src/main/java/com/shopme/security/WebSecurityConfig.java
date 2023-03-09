package com.shopme.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired private CustomerOAuth2UserService customerOAuth2Service;
	@Autowired private OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
	@Autowired private DatabaseLoginSuccessHandler databaseLoginHandler;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().
		antMatchers("/account_details","/update_account_details",
				"/cart", "/address_book/**", "/orders/**",
				"/checkout", "/place_order").authenticated()
		.anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/login")
		.usernameParameter("email")
		.successHandler(databaseLoginHandler)
		.permitAll()
		.and()
		.oauth2Login()
			.loginPage("/login")
			.userInfoEndpoint()
			.userService(customerOAuth2Service)
			.and()
			.successHandler(oauth2LoginSuccessHandler)
		.and()
		.logout().permitAll()
		.and()
		.rememberMe()
			.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
			.tokenValiditySeconds(14*24*60*60)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}
	@Bean
	
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

}
