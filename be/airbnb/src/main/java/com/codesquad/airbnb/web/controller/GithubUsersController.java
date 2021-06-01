package com.codesquad.airbnb.web.controller;

import com.codesquad.airbnb.web.config.annotation.UserAgent;
import com.codesquad.airbnb.web.config.properties.OAuthSecret;
import com.codesquad.airbnb.web.domain.user.User;
import com.codesquad.airbnb.web.domain.user.UserAgentEnum;
import com.codesquad.airbnb.web.dto.OAuthLoginData;
import com.codesquad.airbnb.web.dto.UserWithToken;
import com.codesquad.airbnb.web.service.oauth.OAuthDataService;
import com.codesquad.airbnb.web.service.oauth.OauthApiRequester;
import com.codesquad.airbnb.web.service.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequestMapping("/users/github")
@RestController
public class GithubUsersController {

    private final OAuthDataService oauthDataService;
    private final OauthApiRequester githubApiRequester;
    private final UserService userService;
    private final OAuthSecret oAuthSecret;

    public GithubUsersController(OAuthDataService oauthDataService,
                                 OauthApiRequester githubApiRequester,
                                 UserService userService,
                                 OAuthSecret oAuthSecret) {
        this.oauthDataService = oauthDataService;
        this.githubApiRequester = githubApiRequester;
        this.userService = userService;
        this.oAuthSecret = oAuthSecret;
    }

    @GetMapping("/login")
    public OAuthLoginData login(HttpServletResponse response,
                                @UserAgent UserAgentEnum userAgent) throws IOException {
        if (userAgent == UserAgentEnum.IOS) {
            return oauthDataService.createIosOAuthData();
        }
        response.sendRedirect(oauthDataService.githubFeLoginUrl());
        return oauthDataService.createIosOAuthData();
    }

    @GetMapping("/callback")
    public UserWithToken githubCallback(@RequestParam(value = "code") String code,
                                        @UserAgent UserAgentEnum userAgent) {
        String clientId = oAuthSecret.clientId(userAgent);
        String clientSecret = oAuthSecret.clientSecret(userAgent);
        String githubAccessToken = githubApiRequester.accessToken(code, clientId, clientSecret);
        User user = githubApiRequester.profile(githubAccessToken);
        return userService.processLogin(user);
    }
}
