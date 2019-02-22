package com.github.wonwoo.web;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.AbstractTemplateView;

public class HandlebarsView extends AbstractTemplateView {

    private Handlebars handlebars;

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        return super.checkResource(locale);
    }

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        Template template = handlebars.compile(this.getUrl());
        template.apply(model, response.getWriter());
    }

    public void setHandlebars(Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    private Resource getResource() {
        return getApplicationContext().getResource(this.getUrl());
    }

}
