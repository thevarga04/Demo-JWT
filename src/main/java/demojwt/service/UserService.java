package demojwt.service;

import demojwt.realm.User;
import demojwt.realm.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  UserRepo userRepo;
  @Autowired
  BCryptPasswordEncoder passwordEncoder;
  
  
  
  public User save ( User user ) {
    user.setPassword( passwordEncoder.encode( user.getPassword() ) );
    return userRepo.save( user );
  }
  
  
  public User getUserByUsername( String username ) {
    return userRepo.getByUsername( username ).orElse( null );
  }
}