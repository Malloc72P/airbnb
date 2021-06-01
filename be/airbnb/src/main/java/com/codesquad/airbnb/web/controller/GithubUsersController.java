package com.codesquad.airbnb.web.controller;

import com.codesquad.airbnb.web.constants.RegexConstants;
import com.codesquad.airbnb.web.domain.user.User;
import com.codesquad.airbnb.web.dto.OAuthLoginData;
import com.codesquad.airbnb.web.dto.UserWithToken;
import com.codesquad.airbnb.web.service.oauth.OAuthDataService;
import com.codesquad.airbnb.web.service.oauth.OauthApiRequester;
import com.codesquad.airbnb.web.service.oauth.github.GithubApiRequester;
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

    public GithubUsersController(OAuthDataService oauthDataService, GithubApiRequester githubApiRequester, UserService userService) {
        this.oauthDataService = oauthDataService;
        this.githubApiRequester = githubApiRequester;
        this.userService = userService;
    }

    @GetMapping("/login")
    public OAuthLoginData login(HttpServletResponse response,
                                @RequestHeader(value = "User-Agent") String userAgent) throws IOException {
        if (userAgent.matches(RegexConstants.IOS_USER_AGENT_PATTERN)) {
            return oauthDataService.createLoginData();
        } else {
            response.sendRedirect(oauthDataService.githubLoginUrl());
        }
    }

    @GetMapping("/callback")
    public UserWithToken githubCallback(@RequestParam(value = "code") String code) {
        String githubAccessToken = githubApiRequester.accessToken(code);
        User user = githubApiRequester.profile(githubAccessToken);
        return userService.processLogin(user);
    }
}
