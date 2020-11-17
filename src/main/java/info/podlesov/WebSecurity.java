package info.podlesov;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * Configures basic auth to everything except swagger-ui, h2 console and GET requests
 */

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
  @Autowired
  @Qualifier(value = "simple_auth")
  UserDetailsService userDetailsService;
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/swagger-ui/**", "/h2-console/**").permitAll()
            .antMatchers(HttpMethod.GET, "/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic().realmName("Blog Realm");
    http.headers().frameOptions().disable();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
  }
}
