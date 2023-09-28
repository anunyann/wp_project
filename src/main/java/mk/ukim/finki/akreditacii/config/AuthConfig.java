package mk.ukim.finki.akreditacii.config;

import mk.ukim.finki.akreditacii.model.AppRole;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

public class AuthConfig {

    public HttpSecurity authorize(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers("/admin/**", "/api/**", "/build/**").hasAnyRole(
                                AppRole.ADMIN.name()
                        )
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(LogoutConfigurer::permitAll);
    }

}
