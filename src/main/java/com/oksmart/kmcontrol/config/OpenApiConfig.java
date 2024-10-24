package com.oksmart.kmcontrol.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "API KM Control",
                version = "1.0",
                description = "API para controle de quilometragem de veículos",
                contact = @Contact(name = "Seu Nome", email = "seuemail@example.com"),
                license = @License(name = "Licença", url = "URL da licença")
        )
)
public class OpenApiConfig {
}
