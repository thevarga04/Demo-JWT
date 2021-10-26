package demojwt.realm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepo userRepo;
  
  
  
  @Override
  public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
    Optional<User> user = userRepo.getByUsername( username );
    if ( user.isEmpty() ) throw new UsernameNotFoundException( username );
    
    // TODO: Roles (users & roles of ManyToMany relationship) ...
    return new org.springframework.security.core.userdetails
      .User( user.get().getUsername(), user.get().getPassword(), new ArrayList<>() );
  }
}