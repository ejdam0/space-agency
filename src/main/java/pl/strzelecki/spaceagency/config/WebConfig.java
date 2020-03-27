package pl.strzelecki.spaceagency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "pl.strzelecki.spaceagency.controller")
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public InternalResourceViewResolver resolver() {
        return new InternalResourceViewResolver();
    }
}