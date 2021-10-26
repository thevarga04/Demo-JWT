package demojwt.realm;

import demojwt.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {
  private static final long serialVersionUID = 1290991383295529993L;
  private static final Logger logger = LoggerFactory.getLogger( JwtUtility.class );
  
  @Value( "${jwt.secret}" )
  String secret;
  @Value( "${jwt.validity}" )
  long JWT_TOKEN_VALIDITY;
  @Autowired
  UserService userService;
  
  
  
  private Boolean isTokenNotExpired( String token ) {
    return getExpirationDateFromToken( token ).after( new Date() );
  }
  
  public String getUsernameFromToken( String token ) {
    return getClaimFromToken( token, Claims::getSubject );
  }
  
  public Date getExpirationDateFromToken( String token ) {
    return getClaimFromToken( token, Claims::getExpiration );
  }
  
  public <T> T getClaimFromToken( String token, Function<Claims, T> claim ) {
    Claims claims = getAllClaimsFromToken( token );
    return claim.apply( claims );
  }
  
  
  
  private Claims getAllClaimsFromToken( String token ) {
    return Jwts.parser().setSigningKey( secret ).parseClaimsJws( token ).getBody();
  }
  
  
  
  public String generateToken( String username ) {
    Map<String, Object> claims = new HashMap<>();
    
    return Jwts.builder()
      .setClaims( claims )
      .setSubject( username )
      .setIssuedAt( new Date() )
      .setExpiration( new Date( System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000 ) )
      .signWith( SignatureAlgorithm.HS512, secret )
      .compact();
  }
  
  public Boolean validateToken( String token, UserDetails userDetails ) {
    return isTokenNotExpired( token ) && userDetails.getUsername().equals( getUsernameFromToken( token ));
  }
  
  
  /**
   * My way ;-)
   */
  public boolean validate( String token ) {
    String username = getUsernameFromToken( token );
    User user = userService.getUserByUsername( username );
    if ( user == null ) {
      logger.info( "Invalid User" );
      return false;
    }
    
    return isTokenNotExpired( token );
  }
}