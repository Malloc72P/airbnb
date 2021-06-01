package com.codesquad.airbnb.web.config.resolver;

import com.codesquad.airbnb.web.config.annotation.UserAgent;
import com.codesquad.airbnb.web.constants.RegexConstants;
import com.codesquad.airbnb.web.domain.user.UserAgentEnum;
import com.codesquad.airbnb.web.exceptions.UnknownUserAgentException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserAgentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAgent.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String stringUserAgent = webRequest.getHeader("User-Agent");
        if (stringUserAgent == null) {
            throw new UnknownUserAgentException(UnknownUserAgentException.NO_USER_AGENT);
        }
        return extractUserAgent(stringUserAgent);
    }

    private UserAgentEnum extractUserAgent(String stringUserAgent) {
        UserAgentEnum userAgent = UserAgentEnum.FE;
        if (stringUserAgent.matches(RegexConstants.IOS_USER_AGENT_PATTERN)) {
            userAgent = UserAgentEnum.IOS;
        }
        return userAgent;
    }
}
