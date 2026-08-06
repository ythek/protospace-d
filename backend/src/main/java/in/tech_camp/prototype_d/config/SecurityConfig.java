package in.tech_camp.prototype_d.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 環境変数 FRONTEND_URL を取得。
    @Value("${FRONTEND_URL}")
    // ローカルで動かす場合は以下をコメントインしてください。
    // @Value("http://localhost:3000")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"error\":\"Unauthorized\"}");
                        }))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // ★ 1. OPTIONS リクエストを全許可（CORSプリフライト対策）
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 既存の設定
                        .requestMatchers(HttpMethod.GET, "/css/**", "/images/**", "/uploads/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/prototypes", "/api/prototypes/{id:[0-9]+}", "/api/prototypes/{id:[0-9]+}/comments", "/api/users/{id:[0-9]+}","/api/prototypes/likes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/sign_up", "/api/users/sign_in").permitAll()
                        .anyRequest().authenticated())

                .formLogin(login -> login
                        .loginProcessingUrl("/api/users/sign_in")
                        .usernameParameter("email")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(
                                    "{\"error\":\"Invalid credentials\"}");
                        }))

                .logout(logout -> logout
                        .logoutUrl("/api/users/sign_out")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"success\":true}");
                        }));
        return http.build();
    }

    // CORSの設定を独立したBeanとして定義
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", frontendUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
                    "{\"id\":%d,\"email\":\"%s\",\"username\":\"%s\"}",
                    userDetails.getId(),
                    userDetails.getEmail(),
                    userDetails.getUsername()));
        };
    }
}
