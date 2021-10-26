package demojwt.realm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JwtFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger( JwtFilter.class );
  
  @Autowired
  JwtUtility jwtUtility;
  
  
  
  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    // Get authorization header and validate it
    String header = request.getHeader( HttpHeaders.AUTHORIZATION );           // "Authorization"
    if ( isEmpty( header ) || ! header.startsWith( "Bearer " ) ) {
      logger.info( "Invalid authorization" );
      filterChain.doFilter( request, response );
      return;
    }
    
    
    // Get JWT and validate it
    String token = header.substring( 7 ).trim();
    if ( ! jwtUtility.validate( token ) ) {
      filterChain.doFilter( request, response );
      return;
    }
    
    
    // Set authorization
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken( null, null, new ArrayList<>() );
    SecurityContextHolder.getContext().setAuthentication( auth );
    filterChain.doFilter( request, response );
  }
}