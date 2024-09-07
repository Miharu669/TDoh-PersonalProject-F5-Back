package dev.doel.TDoh.config;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;




@Component
public class BasicAuthEntryPoint extends BasicAuthenticationEntryPoint {
    
   @Override
public void commence(HttpServletRequest request, HttpServletResponse response,
                     AuthenticationException authException) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    
    response.setHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
    
    response.getWriter().println("HTTP Status 401 - " + authException.getMessage());
}

    @Override
    public void afterPropertiesSet() {
        setRealmName("Spring Digital Academy");
        super.afterPropertiesSet();
    }
}
