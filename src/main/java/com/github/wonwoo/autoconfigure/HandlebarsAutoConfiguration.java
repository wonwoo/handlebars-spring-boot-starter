package com.github.wonwoo.autoconfigure;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.wonwoo.HandlebarsProperties;
import com.github.wonwoo.SpringTemplateLoader;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;

@Configuration
@ConditionalOnClass(Handlebars.class)
@EnableConfigurationProperties(HandlebarsProperties.class)
@Import({ HandlebarServletWebConfiguration.class, HandlebarsReactiveWebConfiguration.class })
public class HandlebarsAutoConfiguration {

    private static final Log logger = LogFactory.getLog(HandlebarsAutoConfiguration.class);

    private final HandlebarsProperties handlebars;
    private final ApplicationContext applicationContext;
    private final ObjectProvider<HandlebarsCustomizer> handlebarsCustomizers;

    public HandlebarsAutoConfiguration(HandlebarsProperties handlebars,
        ApplicationContext applicationContext,
        ObjectProvider<HandlebarsCustomizer> handlebarsCustomizers) {
        this.handlebars = handlebars;
        this.applicationContext = applicationContext;
        this.handlebarsCustomizers = handlebarsCustomizers;
    }

    @Bean
    @ConditionalOnMissingBean
    public Handlebars handlebars(TemplateLoader templateLoader) {
        Handlebars handlebars = new Handlebars(templateLoader);
        handlebars.setCharset(Charset.forName(this.handlebars.getCharsetName()));
        customizer(handlebars);
        return handlebars;
    }

    @Bean
    @ConditionalOnMissingBean
    public TemplateLoader templateLoader(ResourceLoader resourceLoader) {
        return new SpringTemplateLoader(resourceLoader);
    }

    private void customizer(Handlebars handlebars) {
        List<HandlebarsCustomizer> customizers = this.handlebarsCustomizers
            .orderedStream().collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(customizers)) {
            customizers.forEach(customizer -> customizer.customize(handlebars));
        }
    }

    @Configuration
    protected static class SimpleHandlebarsCustomizer implements HandlebarsCustomizer {

        @Override
        public void customize(Handlebars handlebars) {
            handlebars.registerHelper(LocalDateHelper.NAME, LocalDateHelper.INSTANCE);
            StringHelpers.register(handlebars);
            conditionalHelpersRegister(handlebars);
        }

        private void conditionalHelpersRegister(Handlebars handlebars) {
            for (ConditionalHelpers helper : ConditionalHelpers.values()) {
                handlebars.registerHelper(helper.name(), helper);
            }
        }
    }

    private static class LocalDateHelper implements Helper<TemporalAccessor> {

        final static Helper<TemporalAccessor> INSTANCE = new LocalDateHelper();

        final static String NAME = "localDate";

        @Override
        public String apply(final TemporalAccessor context, final Options options) {
            Object format = options.hash.get("format");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern((String) format);
            return dateTimeFormatter.format(context);
        }
    }

    @PostConstruct
    public void checkTemplateLocationExists() {
        if (this.handlebars.isCheckTemplateLocation()) {
            TemplateLocation location = new TemplateLocation(this.handlebars.getPrefix());
            if (!location.exists(this.applicationContext)) {
                logger.warn("Cannot find template location: " + location
                    + " (please add some templates, check your Mustache "
                    + "configuration, or set spring.handlebarso."
                    + "check-template-location=false)");
            }
        }
    }
}
