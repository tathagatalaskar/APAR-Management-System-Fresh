package in.gov.nic.apar.config;

import in.gov.nic.apar.security.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.*;

@Configuration @EnableMethodSecurity
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain chain(HttpSecurity http, JwtFilter jwt) throws Exception { return http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/health","/auth/**","/v1/meta/**").permitAll().requestMatchers(HttpMethod.OPTIONS,"/**").permitAll().anyRequest().authenticated()).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build(); }
 @Bean CorsConfigurationSource cors(@Value("${app.cors-origins}") String origins){ CorsConfiguration c=new CorsConfiguration(); c.setAllowedOrigins(Arrays.stream(origins.split(",")).map(String::trim).toList()); c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS")); c.setAllowedHeaders(List.of("Authorization","Content-Type")); c.setAllowCredentials(false); UrlBasedCorsConfigurationSource s=new UrlBasedCorsConfigurationSource(); s.registerCorsConfiguration("/**",c); return s; }
}
