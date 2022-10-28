package com.epam.esm.security.filter;

import com.epam.esm.security.provider.JwtTokenProvider;
import com.epam.esm.service.exception.JwtAuthenticationException;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider provider;

    public JwtTokenFilter(JwtTokenProvider provider){
        this.provider = provider;
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        String token = provider.resolveToken((HttpServletRequest) request);
    try{
        if (token != null && provider.validateToken(token)){
            Authentication authentication = provider.getAuthentication(token);
            if (authentication != null){
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    } catch (JwtAuthenticationException e) {
        SecurityContextHolder.clearContext();
        ((HttpServletResponse) response).sendError(e.getHttpStatus().value(), "i cant fell my face");
         throw new JwtAuthenticationException("invalid.jwt.auth");
    }
    chain.doFilter(request, response);
    }
}
