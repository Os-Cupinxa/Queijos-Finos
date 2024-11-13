package com.queijos_finos.main.middleware;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.queijos_finos.main.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && jwtUtils.isValidToken(token) && !jwtUtils.isTokenExpired(token)) {
            Long teamId = jwtUtils.getTeamId(token);
            request.setAttribute("teamId", teamId);

            String profile = jwtUtils.getUserProfile(token);
            request.setAttribute("profile", profile);

            return true;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendRedirect("/");
            return false;
        }
    }
}
