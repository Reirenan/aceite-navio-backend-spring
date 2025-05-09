package br.com.treinaweb.twjobs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class Config {

    @Value("${spring.datasource.driver-class-name}")
    String dbDriverClassName;

    @Value("${spring.jpa.database-platform}")
    String dbPlatform;

    @Value("${spring.datasource.url}")
    String dbUrl;

    @Value("${spring.datasource.username}")
    String dbUsername;

    @Value("${spring.datasource.password}")
    String dbPassword;

    @Value("${spring.servlet.multipart.max-file-size}")
    String maxFileSize;

    @Value("${spring.servlet.multipart.max-request-size}")
    String maxRequestSize;

    @Value("${spring.mail.host}")
    String mailHost;

    @Value("${spring.mail.port}")
    String mailPort;

    @Value("${spring.mail.username}")
    String mailUsername;

    @Value("${spring.mail.password}")
    String mailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    String mailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    String mailStartTls;

    @Value("${spring.jpa.show-sql}")
    String jpaShowSql;

    @Value("${spring.jpa.properties.hibernate.event.merge.entity_copy_observer}")
    String jpaMergeEntityCopyObserver;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    String jpaHibernateDdlAuto;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    String jpaFormatSql;

    @Value("${spring.data.web.pageable.one-indexed-parameters}")
    String pageableOneIndexedParameters;

    @Value("${spring.data.web.pageable.default-page-size}")
    String pageableDefaultPageSize;

    @Value("${spring.data.web.pageable.max-page-size}")
    String pageableMaxPageSize;

    @Value("${jwt.access.secret}")
    String jwtAccessSecret;

    @Value("${jwt.access.expires-in}")
    String jwtAccessExpiresIn;

    @Value("${jwt.refresh.secret}")
    String jwtRefreshSecret;

    @Value("${jwt.refresh.expires-in}")
    String jwtRefreshExpiresIn;

    @Value("${contato.disco.raiz}")
    String discoRaiz;

    @Value("${contato.disco.diretorio-fotos}")
    String discoDiretorioFotos;

}
