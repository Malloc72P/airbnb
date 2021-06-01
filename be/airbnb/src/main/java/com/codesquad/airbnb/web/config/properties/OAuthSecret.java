package com.codesquad.airbnb.web.config.properties;

import com.codesquad.airbnb.web.domain.user.UserAgentEnum;
import com.codesquad.airbnb.web.exceptions.UnknownUserAgentException;
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

    public String clientId(UserAgentEnum userAgent) {
        switch (userAgent) {
            case IOS:
                return iosClientIdValue;
            case FE:
                return feClientIdValue;
        }
        throw new UnknownUserAgentException(UnknownUserAgentException.UNKNOWN_USER_AGENT);
    }

    public String clientSecret(UserAgentEnum userAgent) {
        switch (userAgent) {
            case IOS:
                return iosClientSecretValue;
            case FE:
                return feClientSecretValue;
        }
        throw new UnknownUserAgentException(UnknownUserAgentException.UNKNOWN_USER_AGENT);
    }
}
