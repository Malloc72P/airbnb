package com.codesquad.airbnb.web.controller;

import com.codesquad.airbnb.web.config.properties.OAuthSecret;
import com.codesquad.airbnb.web.constants.RegexConstants;
import com.codesquad.airbnb.web.domain.user.User;
import com.codesquad.airbnb.web.domain.user.UserAgentEnum;
import com.codesquad.airbnb.web.dto.OAuthLoginData;
import com.codesquad.airbnb.web.dto.UserWithToken;
import com.codesquad.airbnb.web.service.oauth.OAuthDataService;
import com.codesquad.airbnb.web.service.oauth.OauthApiRequester;
import com.codesquad.airbnb.web.service.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
                                @RequestHeader(value = "User-Agent") String stringUserAgent) throws IOException {
        UserAgentEnum userAgent = extractUserAgent(stringUserAgent);
        if (userAgent == UserAgentEnum.IOS) {
            return oauthDataService.createIosOAuthData();
        }
        response.sendRedirect(oauthDataService.githubFeLoginUrl());
        return oauthDataService.createIosOAuthData();
    }

    @GetMapping("/callback")
    public UserWithToken githubCallback(@RequestParam(value = "code") String code,
                                        @RequestHeader(value = "User-Agent") String stringUserAgent) {
        UserAgentEnum userAgent = extractUserAgent(stringUserAgent);
        String clientId = oAuthSecret.clientId(userAgent);
        String clientSecret = oAuthSecret.clientSecret(userAgent);
        String githubAccessToken = githubApiRequester.accessToken(code, clientId, clientSecret);
        User user = githubApiRequester.profile(githubAccessToken);
        return userService.processLogin(user);
    }

    private UserAgentEnum extractUserAgent(String stringUserAgent) {
        UserAgentEnum userAgent = UserAgentEnum.FE;
        if (stringUserAgent.matches(RegexConstants.IOS_USER_AGENT_PATTERN)) {
            userAgent = UserAgentEnum.IOS;
        }
        return userAgent;
    }
}
