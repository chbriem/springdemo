package com.example.springdemo;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JettyWebServerCustomizer implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {

    private final int inflateBufferSize;

    public JettyWebServerCustomizer(@Value("${server.compression.inflate-buffer-size:65536}") int inflateBufferSize) {
        this.inflateBufferSize = inflateBufferSize;
    }

    @Override
    public void customize(JettyServletWebServerFactory factory) {
        factory.addServerCustomizers(server -> {
            configureGzipHandler(server.getHandler(), inflateBufferSize);
        });
    }

    private static void configureGzipHandler(Handler handler, int inflateBufferSize) {
        if (handler == null) {
            return;
        } else if (handler instanceof GzipHandler) {
            ((GzipHandler) handler).setInflateBufferSize(inflateBufferSize);
        } else if (handler instanceof HandlerWrapper) {
            configureGzipHandler(((HandlerWrapper) handler).getHandler(), inflateBufferSize);
        }
    }
}