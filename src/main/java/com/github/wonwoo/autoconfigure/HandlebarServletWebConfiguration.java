package com.github.wonwoo.autoconfigure;

import com.github.jknack.handlebars.Handlebars;
import com.github.wonwoo.HandlebarsProperties;
import com.github.wonwoo.web.HandlebarsViewResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
class HandlebarServletWebConfiguration {

    private final HandlebarsProperties handlebars;

    HandlebarServletWebConfiguration(HandlebarsProperties handlebars) {
        this.handlebars = handlebars;
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlebarsViewResolver handlebarsViewResolver(Handlebars handlebars) {
        HandlebarsViewResolver resolver = new HandlebarsViewResolver(handlebars);
        this.handlebars.applyToMvcViewResolver(resolver);
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return resolver;
    }

}
