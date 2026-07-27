package in.tech_camp.prototype_d.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors
                .configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    return corsConfiguration;
                })
            )
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                // 静的ファイルやエラーページ
                .requestMatchers(HttpMethod.GET, "/css/**", "/images/**", "/error").permitAll()
                // GETリクエスト（閲覧系）を許可
                .requestMatchers(HttpMethod.GET, "/users/sign_up", "/users/sign_in", "/tweets/{id:[0-9]+}", "/users/{id:[0-9]+}", "/tweets/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tweets/**", "/api/prototypes/**").permitAll()
                // ユーザー登録・ログイン（POST）を許可
                .requestMatchers(HttpMethod.POST, "/user", "/api/users/**", "/api/sign_in").permitAll()
                // 上記以外のAP/ページは認証が必要
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"error\":\"Unauthorized\"}");
                })
            )
            .formLogin(login -> login
                .loginProcessingUrl("/api/sign_in")
                .usernameParameter("email")
                .successHandler(authenticationSuccessHandler())
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"error\":\"Invalid credentials\"}");
                })
            )
            .logout(logout -> logout
                .logoutUrl("/api/sign_out")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\":true}");
                })
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.format(
                "{\"id\":%d,\"email\":\"%s\"}",
                userDetails.getId(),
                userDetails.getEmail()
            ));
        };
    }
}

