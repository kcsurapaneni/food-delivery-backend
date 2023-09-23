package com.assessment.restaurant.config;

import io.micrometer.tracing.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import java.io.*;
import java.util.*;

/**
 * @author Krishna Chaitanya
 */
@Component
@Profile("docker")
public class TraceFilter extends OncePerRequestFilter {

    // read more on `traceparent` header here -> https://www.w3.org/TR/trace-context/#traceparent-header
    private static final String TRACER_HEADER_NAME = "traceparent";
    private static final String DEFAULT = "00";
    private final Tracer tracer;

    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!response.getHeaderNames().contains(TRACER_HEADER_NAME)) {
            var traceContext = tracer.currentTraceContext().context();
            if (Objects.nonNull(traceContext)) {
                var traceId = traceContext.traceId();
                var spanId = traceContext.spanId();
                response.setHeader(TRACER_HEADER_NAME, DEFAULT + "-" + traceId + "-" + spanId + "-" + DEFAULT);
            }
        }
        filterChain.doFilter(request, response);
    }

}
