// package dev.doel.TDoh.config;

// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

//     @Value("${google.success-redirect-url}")
//     private String successRedirectUrl;

//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                         Authentication authentication) throws IOException, ServletException {
       
//         response.sendRedirect(successRedirectUrl);
//     }
// }
