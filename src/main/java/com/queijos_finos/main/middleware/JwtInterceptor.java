package com.queijos_finos.main.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.queijos_finos.main.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || !jwtUtils.isValidToken(token)) {
            setJsonResponse(response, "Token inválido ou faltante");
            return false;
        } else if (jwtUtils.isTokenExpired(token)) {
            setJsonResponse(response, "Token expirado");
            return false;
        }

        Long teamId = jwtUtils.getTeamId(token);
        request.setAttribute("teamId", teamId);

        String profile = jwtUtils.getUserProfile(token);
        request.setAttribute("profile", profile);

        return true;
    }

    private void setJsonResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
