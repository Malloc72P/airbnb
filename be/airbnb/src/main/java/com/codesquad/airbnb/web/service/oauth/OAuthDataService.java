package com.codesquad.airbnb.web.service.oauth;

import com.codesquad.airbnb.web.config.properties.GithubApi;
import com.codesquad.airbnb.web.config.properties.OAuthSecret;
import com.codesquad.airbnb.web.dto.OAuthLoginData;
import org.springframework.stereotype.Service;

@Service
public class OAuthDataService {
    private final GithubApi githubApi;
    private final OAuthSecret oauthSecret;

    public OAuthDataService(GithubApi githubApi, OAuthSecret oauthSecret) {
        this.githubApi = githubApi;
        this.oauthSecret = oauthSecret;
    }

    public String githubLoginUrl() {
        return githubApi.getLoginUrl() +
                "?" +
                oauthSecret.getClientIdKey() +
                "=" +
                oauthSecret.getClientIdValue() +
                "&" +
                githubApi.getScopeKey() +
                "=" +
                githubApi.getScopeValue();
    }

    public OAuthLoginData createLoginData() {
        return new OAuthLoginData(oauthSecret.getClientIdValue(), githubApi.getScopeValue());
    }
}
