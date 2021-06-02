package com.codesquad.airbnb.web.config;

import com.codesquad.airbnb.web.config.interceptor.AuthInterceptor;
import com.codesquad.airbnb.web.config.resolver.CertifiedUserResolver;
import com.codesquad.airbnb.web.config.resolver.UserAgentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final CertifiedUserResolver certifiedUserResolver;
    private final UserAgentResolver userAgentResolver;

    public WebMvcConfig(AuthInterceptor authInterceptor, CertifiedUserResolver certifiedUserResolver, UserAgentResolver userAgentResolver) {
        this.authInterceptor = authInterceptor;
        this.certifiedUserResolver = certifiedUserResolver;
        this.userAgentResolver = userAgentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/rooms/{roomId}/reservations/**")
                .addPathPatterns("/reservations/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(certifiedUserResolver);
        resolvers.add(userAgentResolver);
    }
}
