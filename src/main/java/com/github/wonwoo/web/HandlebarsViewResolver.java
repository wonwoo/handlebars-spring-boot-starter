package com.github.wonwoo.web;

import com.github.jknack.handlebars.Handlebars;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class HandlebarsViewResolver extends AbstractTemplateViewResolver {

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
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        HandlebarsView view = (HandlebarsView) super.buildView(viewName);
        view.setHandlebars(handlebars);
        return view;
    }
}
