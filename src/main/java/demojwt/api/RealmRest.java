package demojwt.api;

import demojwt.realm.JwtUtility;
import demojwt.realm.User;
import demojwt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/realm" )
public class RealmRest {
  private static final Logger logger = LoggerFactory.getLogger( RealmRest.class );
  
  @Autowired
  UserService userService;
  @Autowired
  AuthenticationManager am;
  @Autowired
  UserDetailsService userDetailsService;
  @Autowired
  BCryptPasswordEncoder passwordEncoder;
  @Autowired
  JwtUtility jwtUtility;

  
  
  /**
   * Interface to add/create users in DB as JSON, not a FormData.
   */
  @Profile( value = "default" )
  @PostMapping( value = "/addUser" )
  public ResponseEntity<User> addUser( @RequestBody User user ) {
    if ( user == null || user.getUsername() == null || user.getPassword() == null )
      return new ResponseEntity<>( null, HttpStatus.NOT_ACCEPTABLE );
    
    User savedUser = userService.save( user );
    logger.info( "userService.save: \n{}", savedUser );
    if ( savedUser.getId() > 0 )
      return ResponseEntity.ok( savedUser );
    else
      return new ResponseEntity<>( null, HttpStatus.NOT_IMPLEMENTED );
  }
  
  
  
  /**
   * JWT Token Provider with a redundant authentications.
   */
  @PostMapping( value = "/getToken")
  public ResponseEntity<String> getToken( @RequestBody User userDto ) {
    if ( ! authenticateUser( userDto ) )
      return new ResponseEntity<>( null, HttpStatus.NOT_ACCEPTABLE );
  
    if ( ! passwordEncoderMatches( userDto ) )
      return new ResponseEntity<>( null, HttpStatus.NOT_ACCEPTABLE );
    
    
    logger.info( "Generating token ..." );
    return ResponseEntity.ok( jwtUtility.generateToken( userDto.getUsername() ) );
  }
  
  
  /**
   * Authentication using UserDetailsServiceImpl
   */
  private boolean authenticateUser( User userDto ) {
    logger.info( "Trying am.authenticate ..." );
    try {
      am.authenticate( new UsernamePasswordAuthenticationToken(
        userDto.getUsername(),
        userDto.getPassword()
      ));
    } catch ( DisabledException | BadCredentialsException e ) {
      logger.info( e.getMessage() );
      return false;
    }
    return true;
  }
  
  
  /**
   * Does not need UserDetailsServiceImpl, but need an existing user in DB
   */
  private boolean passwordEncoderMatches( User userDto ) {
    User user = userService.getUserByUsername( userDto.getUsername() );
    logger.info( "userService.getUserByUsername: \n{}", user );
    if ( user == null )
      return false;
    
    logger.info( "Trying passwordEncoder.matches ..." );
    return passwordEncoder.matches( userDto.getPassword(), user.getPassword() );
  }
}