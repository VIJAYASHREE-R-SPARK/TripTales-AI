
package com.triptales.backend.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(
                        "https://*.vercel.app",
                        "http://localhost:5173",
                        "http://localhost:5174"

                )
                .allowedMethods(
                        "GET", "POST", "PUT",
                        "DELETE", "OPTIONS", "PATCH"
                )
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = Paths.get("uploads")
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}

