package br.com.laps.aceite.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "br.com.laps.aceite.jwt")
public class JwtConfigProperties {
    private String accessSecret;
    private Long accessExpiresIn;
    private String refreshSecret;
    private Long refreshExpiresIn;
}
