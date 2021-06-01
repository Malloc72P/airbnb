package com.codesquad.airbnb.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "secret")
@PropertySource(value = "classpath:dev_secret.properties")
public class OAuthSecret {
    private String clientIdKey;
    private String clientSecretKey;

    private String iosClientIdValue;
    private String iosClientSecretValue;

    private String feClientIdValue;
    private String feClientSecretValue;
}
