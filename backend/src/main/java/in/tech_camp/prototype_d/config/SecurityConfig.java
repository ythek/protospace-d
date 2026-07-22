package in.tech_camp.prototype_d.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.List; // ★追加
import org.springframework.web.cors.CorsConfiguration; // ★追加

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
                        // ★ 3001番ポートや 127.0.0.1 からのアクセスも許可
                        corsConfiguration.setAllowedOrigins(List.of(
                            "http://localhost:3000", 
                            "http://localhost:3001",
                            "http://127.0.0.1:3000"
                        ));
                        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        return corsConfiguration;
                    })
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // ここに記述されたGETリクエストは許可されます（ログイン不要です）
                        .requestMatchers(HttpMethod.GET, "/css/**", "/images/**", "/users/sign_up", "/users/login", "/tweets/{id:[0-9]+}", "/users/{id:[0-9]+}", "/tweets/search", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tweets/**").permitAll()

                        // ★★★ ここを追加！プロトタイプ一覧・詳細のGETリクエストを全員に許可 ★★★
                        .requestMatchers("/api/prototypes/**").permitAll()
                        // ここに記述されたPOSTリクエストは許可されます（ログイン不要です）
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        // 上記以外のリクエストは認証されたユーザーのみ許可されます（要ログイン）
                        .anyRequest().authenticated()
                );                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      

        return http.build();
    }
}