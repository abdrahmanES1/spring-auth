package com.esab.springauth.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.esab.springauth.config.SecurityFilter;
import com.esab.springauth.repositories.UserRepository;
import com.esab.springauth.services.UserService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class OurSecurityFilter extends OncePerRequestFilter {
  private static final Logger LOGGER = Logger.getLogger(OurSecurityFilter.class.getName());

  @Autowired
  TokenProvider tokenService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      if (SecurityContextHolder.getContext().getAuthentication() != null &&
          SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"error\": \"User is already authenticated\"}");
        response.getWriter().flush();
        return;
      }

      var token = this.recoverToken(request);
      if (token != null) {
        var login = tokenService.validateToken(token);
        if (login == null) {
          LOGGER.log(Level.WARNING, "Invalid token");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.getWriter().write("{\"error\": \"Invalid token\"}");
          response.getWriter().flush();
          return;
        }

        var user = userService.loadUserByUsername(login);

        if (user == null) {
          LOGGER.log(Level.WARNING, "User not found");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.getWriter().write("{\"error\": \"User not found\"}");
          response.getWriter().flush();
          return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error during authentication", e);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("{\"error\": \"Invalid credentials\"}");
      response.getWriter().flush();
    }
  }

  private String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null)
      return null;
    return authHeader.replace("Bearer ", "");
  }
}