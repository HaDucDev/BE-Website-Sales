package com.haduc.beshop.config.jwt;

import com.haduc.beshop.config.springSecurity.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MyUserDetailsService myUserDetailsService;



    //lay chuoi jwt dung trong bo loc
    private String parseJwt(String headerAuth) {
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String headerAuth = httpServletRequest.getHeader("Authorization");
            String jwt = this.parseJwt(headerAuth);

            if (jwt != null && this.jwtUtils.validateToken(jwt)) {// kiem tra token
                String username = this.jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);// load thong tin

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
