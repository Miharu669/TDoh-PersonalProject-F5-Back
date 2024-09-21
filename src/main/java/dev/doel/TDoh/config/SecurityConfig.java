package dev.doel.TDoh.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import dev.doel.TDoh.encryptations.base64.Base64Encoder;
import dev.doel.TDoh.security.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api-endpoint}")
    String endpoint;
    
    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    private final JpaUserDetailsService jpaUserDetailsService;
    private final BasicAuthEntryPoint basicAuthEntryPoint;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService, BasicAuthEntryPoint authEntryPoint) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.basicAuthEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .logout(out -> out
                .logoutUrl(endpoint + "/logout")
                .deleteCookies("TDOH"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, endpoint + "/register").permitAll()
                .requestMatchers(HttpMethod.GET, endpoint + "/login").authenticated()
                .anyRequest().authenticated())
            .userDetailsService(jpaUserDetailsService)
            .httpBasic(basic -> basic.authenticationEntryPoint(basicAuthEntryPoint))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/google") 
                .defaultSuccessUrl(endpoint + "/login/success", true)
                .failureUrl(endpoint + "/login/failure"));

        http.headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin())
            .addHeaderWriter((request, response) -> {
                response.addHeader("Content-Security-Policy", "script-src 'self' https://apis.google.com  https://accounts.google.com");
            }));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With", "Access-Control-Allow-Origin"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));  
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Base64Encoder base64Encoder() {
        return new Base64Encoder();
    }
}
