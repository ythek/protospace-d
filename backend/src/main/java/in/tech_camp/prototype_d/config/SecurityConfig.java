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
import java.util.List;
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
                        // GETリクエスト（閲覧系）は全員に許可
                        .requestMatchers(HttpMethod.GET, "/css/**", "/images/**", "/users/sign_up", "/users/login", "/tweets/{id:[0-9]+}", "/users/{id:[0-9]+}", "/tweets/search", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tweets/**").permitAll()
                        
                        // ★★★ ここを変更！プロトタイプのGET（閲覧）のみ許可する ★★★
                        .requestMatchers(HttpMethod.GET, "/api/prototypes/**").permitAll()

                        // POSTリクエストの個別許可（ユーザー作成など）
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        
                        // ★ 上記以外のすべてのリクエスト（POST /api/prototypes 等の新規投稿）は認証（要ログイン）が必要
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}