package demojwt.realm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
  
  Optional<User> getById( int id );
  Optional<User> getByUsername( String username );
  
  int deleteUserById( int id );
  int deleteUserByUsername( String username );
}