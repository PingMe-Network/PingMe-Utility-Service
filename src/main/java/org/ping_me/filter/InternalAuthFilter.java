package org.ping_me.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class InternalAuthFilter extends OncePerRequestFilter {

    @Value("${app.internal.secret}")
    private String secret;

    @Override
    @NullMarked
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String timestampStr = request.getHeader("X-Timestamp");
        String signature = request.getHeader("X-Internal-Signature");

        if (signature == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            long ts = Long.parseLong(timestampStr);
            long currentTs = System.currentTimeMillis() / 1000;

            if (Math.abs(currentTs - ts) > 300) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Request Expired");
                return;
            }

            String path = request.getRequestURI();
            String payload = request.getMethod() + path + ts;

            String serverSignature = HmacUtils.hmacSha256(secret, payload);

            if (!serverSignature.equals(signature)) {
                log.error("Invalid signature. Expected: {}, Got: {}", serverSignature, signature);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Signature");
                return;
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Internal Auth Error", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Internal Auth Failed");
        }
    }
}