package demojwt.realm;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter @Setter @ToString
@Table( name = "users" )
public class User {
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  int id;
  @NotNull
  String username;
  String name;
  String surname;
  @NotNull
  String password;
  boolean valid;
  
  
  
  public User(){}
  public User( String username, String password ) {
    this.username = username;
    this.password = password;
    this.valid = true;
  }
  public User( String username, String name, String surname, String password, boolean valid ) {
    this.username = username;
    this.name = name;
    this.surname = surname;
    this.password = password;
    this.valid = valid;
  }
  public User( int id, String username, String name, String surname, String password, boolean valid ) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.surname = surname;
    this.password = password;
    this.valid = valid;
  }
  
  
  
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;
    User user = (User) o;
    return id == user.id;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash( username );
  }
}