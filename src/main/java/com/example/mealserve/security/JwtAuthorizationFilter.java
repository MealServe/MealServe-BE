package com.example.mealserve.security;


import com.example.mealserve.exception.CustomException;
import com.example.mealserve.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;


public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = req.getHeader(JwtUtil.AUTHORIZATION_HEADER);

            if (!StringUtils.hasText(token)) {
                throw new CustomException(ErrorCode.NOT_FOUND_TOKEN);
            }

            token = URLDecoder.decode(token, "UTF-8");
            logger.debug("Processing token: {}", token);

            if (jwtUtil.validateToken(token)) {
                Claims info = jwtUtil.getUserInfoFromToken(token);
                setAuthentication(info.getSubject());
            }
        } catch (CustomException e) {
            handleException(res, e);
            return;
        }
        filterChain.doFilter(req, res);
    }

    // TODO 예외처리 방식 바꾸기
    private void handleException(HttpServletResponse res, CustomException e) throws IOException {
        res.setStatus(e.getErrorCode().getHttpStatus());
        res.getWriter().write(e.getMessage());
    }

    private void setAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}