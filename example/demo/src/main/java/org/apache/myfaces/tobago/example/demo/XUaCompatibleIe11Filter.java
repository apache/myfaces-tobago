package org.apache.myfaces.tobago.example.demo;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is a workaround for IE11 with Tobago 1.0.x
 * You need also a CSS in a style.css file:
 * * {
 * box-sizing: border-box;
 * }
 */
public class XUaCompatibleIe11Filter implements Filter {

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    final String userAgent = httpServletRequest.getHeader("User-Agent");
    if (userAgent != null && userAgent.contains("Trident") && userAgent.contains("rv:11")) { // is IE 11
      final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
      httpServletResponse.setHeader("X-UA-Compatible", "IE=9");
      // known problems
      //                 box of input    menu arrow key     access-key
      // EmulateIE11     +               -                  -
      // EmulateIE10     +               -                  +
      // 10              +               -                  +
      // EmulateIE9      -               +                  +
      // 9               +               -                  +
      // EmulateIE8      -               +                  +
      // 8               -               +                  +
      // EmulateIE7      -               +                  +
      // 7               -               +                  +
      // 5               -               +                  +
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  public void destroy() {
  }
}