package com.github.wonwoo;

import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.handlebars")
public class HandlebarsProperties extends AbstractTemplateViewResolverProperties {

    private static final String DEFAULT_PREFIX = "classpath:/templates/";

    private static final String DEFAULT_SUFFIX = ".hbs";

    /**
     * Prefix to apply to template names.
     */
    private String prefix = DEFAULT_PREFIX;

    /**
     * Suffix to apply to template names.
     */
    private String suffix = DEFAULT_SUFFIX;

    public HandlebarsProperties() {
        super(DEFAULT_PREFIX, DEFAULT_SUFFIX);
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
