package com.br.u2d.hazelcast.interceptor;

import com.br.u2d.hazelcast.config.properties.RateLimitProperties;
import com.br.u2d.hazelcast.service.RateLimitService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitService rateLimitService;

    @Autowired
    private RateLimitProperties rateLimitProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (rateLimitProperties.isEnabled()) {
            String apiKey = request.getHeader("X-api-key");
            if (apiKey == null || apiKey.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: X-api-key");
                return false;
            }
            Bucket tokenBucket = rateLimitService.getBucket(apiKey);
            ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
            if (probe.isConsumed()) {
                response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
                return true;
            } else {
                response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())));
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                        "You have exhausted your API Request Quota");
                return false;
            }
        } else {
            return true;
        }
    }
}
