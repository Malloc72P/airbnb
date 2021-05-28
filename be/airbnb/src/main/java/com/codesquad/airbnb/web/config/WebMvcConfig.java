package com.codesquad.airbnb.web.config;

import com.codesquad.airbnb.web.config.interceptor.AuthInterceptor;
import com.codesquad.airbnb.web.config.resolver.CertifiedUserResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final CertifiedUserResolver certifiedUserResolver;

    public WebMvcConfig(AuthInterceptor authInterceptor, CertifiedUserResolver certifiedUserResolver) {
        this.authInterceptor = authInterceptor;
        this.certifiedUserResolver = certifiedUserResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/rooms/{roomId}/reservations");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(certifiedUserResolver);
    }


}
