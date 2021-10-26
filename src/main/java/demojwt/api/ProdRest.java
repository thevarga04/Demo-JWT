package demojwt.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/" )
public class ProdRest {
  private static final Logger logger = LoggerFactory.getLogger( ProdRest.class );
  

  
  @GetMapping( value = "/protected" )
  public ResponseEntity<String> getProtectedResource() {
    return ResponseEntity.ok( "Very good!" );
  }
  
  
}