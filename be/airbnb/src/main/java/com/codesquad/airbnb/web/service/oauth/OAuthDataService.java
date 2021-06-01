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

    public String githubFeLoginUrl() {
        return githubApi.getLoginUrl() +
                "?" +
                oauthSecret.getClientIdKey() +
                "=" +
                oauthSecret.getFeClientIdValue() +
                "&" +
                githubApi.getScopeKey() +
                "=" +
                githubApi.getScopeValue();
    }

    public OAuthLoginData createIosOAuthData() {
        return new OAuthLoginData(oauthSecret.getIosClientIdValue(), githubApi.getScopeValue());
    }
}
