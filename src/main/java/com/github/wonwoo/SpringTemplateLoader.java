package com.github.wonwoo;

import com.github.jknack.handlebars.io.URLTemplateLoader;
import java.io.IOException;
import java.net.URL;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

public class SpringTemplateLoader extends URLTemplateLoader {

    private final ResourceLoader resourceLoader;

    public SpringTemplateLoader() {
        this(new DefaultResourceLoader());
    }

    public SpringTemplateLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public String resolve(final String uri) {
        return uri;
    }

    @Override
    protected URL getResource(String location) throws IOException {
        return resourceLoader.getResource(location).getURL();
    }
}
