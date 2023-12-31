package com.example.mealserve.global.security.filter;

import com.example.mealserve.global.security.impl.UserDetailsServiceImpl;
import com.example.mealserve.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
                                    throws ServletException, IOException {
        String token = jwtUtil.getTokenFromRequest(req);

        if (StringUtils.hasText(token)) {

            String tokenValue = jwtUtil.substringToken(token);
            logger.info("토큰 확인용 : " + tokenValue);
            if (!jwtUtil.validateToken(tokenValue)) {
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            setAuthentication(info.getSubject());
        }
        logger.info(req.getRequestURI());
        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = null;
        try {
            authentication = createAuthentication(email);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
