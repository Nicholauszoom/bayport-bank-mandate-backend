package com.example.Project.Management.bayport.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Handler for primeicons
        registry.addResourceHandler("/assets/primeicons/**")
                .addResourceLocations("classpath:/static/assets/primeicons/")
                .setCachePeriod(86400);
    }
}