package demojwt.realm;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
  private static final long serialVersionUID = 7706245432076904430L;
  
  
  @Override
  public void commence( HttpServletRequest request, HttpServletResponse response, AuthenticationException excep ) throws IOException, ServletException {
    response.sendError( HttpServletResponse.SC_UNAUTHORIZED, excep.getMessage() );
  }
}