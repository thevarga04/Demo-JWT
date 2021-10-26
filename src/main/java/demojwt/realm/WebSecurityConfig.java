package demojwt.realm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private static final Logger logger = LoggerFactory.getLogger( WebSecurityConfig.class );
  
  @Autowired
  UserDetailsServiceImpl userDetailsService;
  @Autowired
  JwtFilter jwtFilter;
  @Autowired
  AuthenticationEntryPointImpl authEntryPoint;
  
  
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }
  
  
  /**
   *  Spring Security - CORS Configuration
   */
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowCredentials( true );
    corsConfig.addAllowedOrigin( "*" );
    corsConfig.addAllowedHeader( "*" );
    corsConfig.addAllowedMethod( "*" );
    
    source.registerCorsConfiguration( "/**", corsConfig );
    return new CorsFilter( source );
  }
  
  
  
  @Override
  protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
    auth.userDetailsService( userDetailsService ).passwordEncoder( passwordEncoder() );
  }
  
  @Override
  protected void configure( HttpSecurity httpSecurity ) throws Exception {
    // Don't need CSRF for a demo
    httpSecurity.csrf().disable();
    
    // Public endpoints
    httpSecurity.authorizeRequests().antMatchers( "/api/realm/getToken", "/api/realm/addUser" ).permitAll();
  
    // Enable CORS
    httpSecurity.cors();
    
    // Everything else protected
    httpSecurity.authorizeRequests().anyRequest().authenticated();
    
    // JWT need stateless session management
    httpSecurity.sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS );
    
    // JWT Filter
    httpSecurity.addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class );
    
    // Neat exception header
    httpSecurity.exceptionHandling().authenticationEntryPoint( authEntryPoint );
  }
}