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
                        corsConfiguration.setAllowedOrigins(List.of(
                            "http://localhost:3000"
                        ));
                        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        return corsConfiguration;
                    })
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // GETリクエスト（閲覧系）はログイン不要で許可
                        .requestMatchers(HttpMethod.GET, "/css/**", "/images/**", "/users/sign_up", "/users/login", "/tweets/{id:[0-9]+}", "/users/{id:[0-9]+}", "/tweets/search", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tweets/**").permitAll()

                        // プロトタイプ閲覧（GETのみ）を未ログインで許可
                        .requestMatchers(HttpMethod.GET, "/api/prototypes/**").permitAll()

                        // ユーザー登録（POST）を許可
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()

                        // 上記以外（プロトタイプの新規投稿 POST など）は要ログイン
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}