package com.veronica.pack.Securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.veronica.pack.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class Securityconfig extends WebSecurityConfigurerAdapter{
	@Autowired 
	private UserServiceImpl userServiceImpl;
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider aunthenticationProvider()
	{
		DaoAuthenticationProvider auth=new DaoAuthenticationProvider();
		auth.setUserDetailsService(userServiceImpl);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.authenticationProvider(aunthenticationProvider());
		
		
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests().antMatchers(
				"/registration**",
				"/jss**",
				"/css/**",
				"/OTP**",
				"/resendOTP",
				"/img/**").permitAll()
		.antMatchers("/homepage**").permitAll()
		.antMatchers("/uploadsong").hasAuthority("ROLE_ADMIN")
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
	      .defaultSuccessUrl("/player",true)
		.permitAll()
		.and()
		.logout()
		.invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login?logout")
		.permitAll();
	}
}