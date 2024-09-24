import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000") // Mapeie suas rotas API
                .allowedOrigins("http://localhost:3000") // Altere conforme necessário
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowCredentials(true);
    }
}
