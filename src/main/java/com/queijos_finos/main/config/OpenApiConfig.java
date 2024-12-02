package com.queijos_finos.main.config;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Queijos Finos API")
                        .version("1.0")
                        .description("Documentação da API do projeto Queijos Finos")
                        .contact(new Contact()
                                .name("Antony Henrique Bresolin, Arthur Dalpiaz Tessele, João Paulo Basso Haupt, Gabriel Antônio Xander, Samuel Andrei Horn Thomas")
                                .email("antonybresolin1@gmail.com")
                                .url("https://github.com/orgs/Os-Cupinxa"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Organização do projeto no GitHub")
                        .url("https://github.com/orgs/Os-Cupinxa"));
    }
}
