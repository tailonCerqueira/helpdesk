package com.uneb.helpdesk.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/cadastrar").permitAll()
					.antMatchers("/**")
					.hasAnyAuthority("ADMIN", "USER")
					.anyRequest()
				.authenticated()
			.and()
			.formLogin()
				.loginPage("/login").permitAll()
				.failureUrl("/login?errors=true")			
				.defaultSuccessUrl("/users")
				.usernameParameter("email")
				.passwordParameter("password")
			.and()
				.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/")
						.and()
						.exceptionHandling()
						.accessDeniedPage("/denied");
	}	
	
	@Override
	public void configure(WebSecurity webSecurity) throws Exception{
		webSecurity.ignoring()
		.antMatchers("/layout/**");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
				.usersByUsernameQuery("SELECT usr.email, usr.password, usr.active FROM users usr WHERE usr.email = ? and usr.active = 1")
				.authoritiesByUsernameQuery("SELECT usr.email, rl.name FROM users usr "
											+ "INNER JOIN users_roles usrr ON (usr.id = usrr.user_id) "
											+ "INNER JOIN roles rl ON (usrr.role_id = rl.id)"
											+ "WHERE usr.email = ?"
											+ " and   usr.active = 1")
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
	}
}

