package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addCorsMapping(CorsRegistry registry) {
        // 모든 URI에 대해 모든 도메인은 접근을 허용한다.
        registry.addMapping("/**")
                //	.allowedOriginPatterns("http://**", "https://**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(false)
                .exposedHeaders("Authorization", "Content-Disposition");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("file:/home/rpm/upload/files/");
//				.addResourceLocations("file:/Users/yoonsik/dev/rpm/upload/files/");


        registry.addResourceHandler("/upload/files/**")
                .addResourceLocations("file:/home/rpm/upload/files/");
//				.addResourceLocations("file:/Users/yoonsik/dev/rpm/upload/files/");
    }

    @Bean
    MappingJackson2JsonView jsonView() {
        return new MappingJackson2JsonView();
    }

}
