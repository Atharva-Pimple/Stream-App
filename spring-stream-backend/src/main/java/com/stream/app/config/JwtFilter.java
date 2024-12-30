package com.stream.app.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stream.app.services.JWTService;
import com.stream.app.services.SecurityCustomUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    private JWTService jwtService;

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String authHeader=request.getHeader("Authorization");
                String token =null;
                String username= null;
                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    token=authHeader.substring(7);
                    username=jwtService.extractUsername(token);
                }

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails=userDetailService.loadUserByUsername(username);
                    if(jwtService.validateToken(token,userDetails)){
                        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

                filterChain.doFilter(request, response);
    }

}