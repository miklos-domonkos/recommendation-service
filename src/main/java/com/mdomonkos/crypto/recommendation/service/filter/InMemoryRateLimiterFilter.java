package com.mdomonkos.crypto.recommendation.service.filter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Instance scope simple rate limiter.
 * The client ip is blocked until expiry.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "rate-limiter.enabled", matchIfMissing = true)
public class InMemoryRateLimiterFilter implements InitializingBean, Filter {

  @Value("${rate-limiter.max}")
  private Long MAX_REQUESTS;

  @Value("${rate-limiter.window}")
  private Long REQUEST_WINDOW;

  private LoadingCache<String, Integer> requestCountsPerIpAddress;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    String clientIpAddress = getClientIp(httpServletRequest);
    if (isMaximumRequestsPerSecondExceed(clientIpAddress)) {
      httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      httpServletResponse.getWriter().write(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
      return;
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  /**
   * Atomically increment the counter.
   * If there was remaining request expire resets
   *
   * @param clientIpAddress ip of the client
   * @return if there was remaining request
   */
  private boolean isMaximumRequestsPerSecondExceed(String clientIpAddress) {

    Integer requestsProbe = requestCountsPerIpAddress.get(clientIpAddress);

    if (requestsProbe <= MAX_REQUESTS) {
      Integer updatedCount = requestCountsPerIpAddress.asMap()
                                                      .compute(
                                                        clientIpAddress,
                                                        (key, value) -> Objects.isNull(value) ? 1 : (value + 1)
                                                      );
      return updatedCount > MAX_REQUESTS;
    } else {
      return true;
    }

  }

  public String getClientIp(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }

  @Override
  public void destroy() {

  }

  @Override
  public void afterPropertiesSet() throws Exception {
    requestCountsPerIpAddress = Caffeine.newBuilder()
                                        .expireAfterWrite(REQUEST_WINDOW, TimeUnit.SECONDS)
                                        .build(key -> 0);
  }
}
