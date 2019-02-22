package com.github.wonwoo.autoconfigure;

import com.github.jknack.handlebars.Handlebars;

@FunctionalInterface
public interface HandlebarsCustomizer {

    void customize(Handlebars handlebars);

}
