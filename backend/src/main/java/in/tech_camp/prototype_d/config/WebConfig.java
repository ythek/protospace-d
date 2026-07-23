package in.tech_camp.prototype_d.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://localhost:8080/uploads/ファイル名 で uploads フォルダ内の画像を表示できるように公開
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}