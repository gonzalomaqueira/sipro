package uy.edu.ude.sipro.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

//@EnableWebSecurity
//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    	.csrf().disable() // CSRF handled by Vaadin
        .exceptionHandling()
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login2"))
            .accessDeniedPage("/accessDenied")
        .and()
        .authorizeRequests()
            // allow Vaadin URLs without authentication
            .regexMatchers("/frontend/.*", "/VAADIN/.*", "/login.*", "/accessDenied.*").permitAll()
            .regexMatchers(HttpMethod.POST, "/\\?v-r=.*").permitAll()
            // deny other URLs until authenticated
            .antMatchers("/**").fullyAuthenticated();
}
}
