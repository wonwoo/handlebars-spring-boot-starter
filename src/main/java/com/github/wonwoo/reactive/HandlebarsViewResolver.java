package com.github.wonwoo.reactive;

import com.github.jknack.handlebars.Handlebars;
import org.springframework.web.reactive.result.view.AbstractUrlBasedView;
import org.springframework.web.reactive.result.view.UrlBasedViewResolver;

public class HandlebarsViewResolver extends UrlBasedViewResolver {

    private final Handlebars handlebars;

    public HandlebarsViewResolver(Handlebars handlebars) {
        this.handlebars = handlebars;
        setViewClass(requiredViewClass());
    }

    @Override
    protected Class<?> requiredViewClass() {
        return HandlebarsView.class;
    }

    @Override
    protected AbstractUrlBasedView createView(String viewName) {
        HandlebarsView view = (HandlebarsView) super.createView(viewName);
        view.setHandlebars(handlebars);
        return view;
    }

}
