package com.codesquad.airbnb.web.service.oauth.github;

import com.codesquad.airbnb.web.config.properties.GithubApi;
import com.codesquad.airbnb.web.config.properties.ServerSecret;
import com.codesquad.airbnb.web.domain.User;
import com.codesquad.airbnb.web.dto.GithubProfile;
import com.codesquad.airbnb.web.dto.ReceivedAccessToken;
import com.codesquad.airbnb.web.dto.RequestAccessToken;
import com.codesquad.airbnb.web.service.oauth.ApiRequester;
import com.codesquad.airbnb.web.service.oauth.OauthApiRequester;
import com.codesquad.airbnb.web.service.oauth.OauthProfileConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GithubApiRequester implements OauthApiRequester {

    private final ServerSecret serverSecret;
    private final GithubApi githubApi;
    private final OauthProfileConverter oauthProfileConverter;
    private final ApiRequester apiRequester;

    public GithubApiRequester(ServerSecret serverSecret, GithubApi githubApi,
                              OauthProfileConverter oauthProfileConverter, ApiRequester apiRequester) {
        this.serverSecret = serverSecret;
        this.githubApi = githubApi;
        this.oauthProfileConverter = oauthProfileConverter;
        this.apiRequester = apiRequester;
    }

    @Override
    public String accessToken(String code) {
        RequestAccessToken requestAccessToken = RequestAccessToken.builder()
                .clientId(serverSecret.getClientIdValue())
                .clientSecret(serverSecret.getClientSecretValue())
                .code(code)
                .build();
        ReceivedAccessToken receivedAccessToken = apiRequester.callApi(githubApi.getAccessTokenUrl(), HttpMethod.POST,
                new HttpHeaders(), requestAccessToken, ReceivedAccessToken.class);
        return receivedAccessToken.getAccessToken();
    }

    @Override
    public User profile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        GithubProfile githubProfile = apiRequester.callApi(githubApi.getProfileUrl(), HttpMethod.GET, headers,
                HttpEntity.EMPTY, GithubProfile.class);
        return oauthProfileConverter.githubProfileToUser(githubProfile);
    }
}
