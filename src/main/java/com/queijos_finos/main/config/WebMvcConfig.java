package com.queijos_finos.main.config;

import com.queijos_finos.main.middleware.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Autowired
    public WebMvcConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] patterns = {
                "/dashboard",
                "/propriedade",
                "/propriedade/**",
                "/cursos",
                "/cursos/**",
                "/contratos",
                "/contratos/**",
                "/fornecedores",
                "/fornecedores/**",
                "/agenda",
                "/agenda/**",
                "/usuarios",
                "/usuarios/**",
                "/agendaAndExpiringContracts",
                "/dataInsight",
                "/propriedadesDTO",
                "/propriedadesDTO/**",
                "/dataPointYear",
                "/dataPoint"
        };

        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns(patterns);
    }
}
