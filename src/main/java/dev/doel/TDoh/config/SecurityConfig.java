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
import dev.doel.TDoh.auth.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api-endpoint}")
    private String endpoint;

    @Value("#{'${cors.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    private final JpaUserDetailsService jpaUserDetailsService;
    private final BasicAuthEntryPoint basicAuthEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService, BasicAuthEntryPoint basicAuthEntryPoint,
                          CustomOAuth2UserService customOAuth2UserService,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.basicAuthEntryPoint = basicAuthEntryPoint;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .logout(out -> out
                .logoutUrl(endpoint + "/logout")
                .deleteCookies("TDOH")
                .logoutSuccessUrl("/")) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, endpoint + "/register").permitAll()
                .requestMatchers(
                    "/oauth2/authorization/google",
                    "/login",
                    "/login/oauth2/code/google",
                    "/login/success",
                    "/login/failure").permitAll()
                .anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/google") 
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)) 
                .successHandler(oAuth2LoginSuccessHandler) 
                .failureUrl(endpoint + "/auth/login/failure") 
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect(endpoint + "/auth/login/failure");
                }))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(basicAuthEntryPoint)) 
            .userDetailsService(jpaUserDetailsService)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
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
