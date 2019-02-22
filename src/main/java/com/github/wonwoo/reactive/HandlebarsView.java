package com.github.wonwoo.reactive;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.result.view.AbstractUrlBasedView;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HandlebarsView extends AbstractUrlBasedView {

    private Handlebars handlebars;

    @Override
    public boolean checkResourceExists(Locale locale) {
        return getResource().exists();
    }

    @Override
    protected Mono<Void> renderInternal(Map<String, Object> model, MediaType contentType, ServerWebExchange exchange) {
        DataBuffer dataBuffer = exchange.getResponse().bufferFactory().allocateBuffer();
        try {
            Template template = handlebars.compile(this.getUrl());
            Charset charset = getCharset(contentType).orElse(getDefaultCharset());
            try (Writer writer = new OutputStreamWriter(dataBuffer.asOutputStream(),
                charset)) {
                template.apply(model, writer);
            }
        } catch (Exception ex) {
            DataBufferUtils.release(dataBuffer);
            return Mono.error(ex);
        }
        return exchange.getResponse().writeWith(Flux.just(dataBuffer));
    }

    public void setHandlebars(Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    private Resource getResource() {
        return getApplicationContext().getResource(this.getUrl());
    }

    private Optional<Charset> getCharset(MediaType mediaType) {
        return Optional.ofNullable((mediaType != null) ? mediaType.getCharset() : null);
    }
}
