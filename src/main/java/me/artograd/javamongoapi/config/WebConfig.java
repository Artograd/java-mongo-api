package me.artograd.javamongoapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:3000") // Allows CORS requests from the specified origin
        		.allowedOrigins("*")
                //.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        		.allowedMethods("*")
                .allowedHeaders("*");
                //.allowCredentials(true);
    }*/
}
