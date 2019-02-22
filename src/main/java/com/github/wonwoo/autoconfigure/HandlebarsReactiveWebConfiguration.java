package com.github.wonwoo.autoconfigure;

import com.github.jknack.handlebars.Handlebars;
import com.github.wonwoo.HandlebarsProperties;
import com.github.wonwoo.reactive.HandlebarsViewResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnWebApplication(type = Type.REACTIVE)
class HandlebarsReactiveWebConfiguration {

    private final HandlebarsProperties handlebars;

    HandlebarsReactiveWebConfiguration(HandlebarsProperties handlebars) {
        this.handlebars = handlebars;
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlebarsViewResolver handlebarsViewResolver(Handlebars handlebars) {
        HandlebarsViewResolver resolver = new HandlebarsViewResolver(handlebars);
        resolver.setPrefix(this.handlebars.getPrefix());
        resolver.setSuffix(this.handlebars.getSuffix());
        resolver.setViewNames(this.handlebars.getViewNames());
        resolver.setRequestContextAttribute(this.handlebars.getRequestContextAttribute());
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return resolver;
    }
}
