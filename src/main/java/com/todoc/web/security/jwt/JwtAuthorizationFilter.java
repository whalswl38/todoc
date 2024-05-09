package com.todoc.web.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// permitAll, login 요청 제외 요청 처리 // 사용자 인증, 인가를 처리해주는 필터
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter 
{	
	@SuppressWarnings("unused")
	private final ObjectMapper objectMapper;
	@SuppressWarnings("unused")
	private final JwtTokenProvider jwtTokenProvider;
	
	public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper)
	{
		this.jwtTokenProvider = jwtTokenProvider;
		this.objectMapper = objectMapper;
	}
	
    // 요청에서 쿠키를 추출해서, JWT 토큰을 담고 있는 쿠키를 찾아 JWT 토큰 문자열 또는 null 을 반환
    public String extractJwtFromCookie(HttpServletRequest request) 
    {
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) 
        {
            for (Cookie cookie : cookies)
            {
                if (JwtProperties.HEADER_STRING.equals(cookie.getName())) 
                {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
    
    // JWT 토큰을 쿠키에서 추출하고, 토큰이 유효한 경우 사용자 이름을 추출해서 security의 SecurityContext에 인증 정보를 등록
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException 
    {
        String token = extractJwtFromCookie(request);
        
        if (token != null && validateToken(token)) 
        {
            String username = getUsernameFromToken(token); // 토큰에서 사용자 이름 추출 로직 구현 필요
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        chain.doFilter(request, response);
    }
    
    // 토큰 값 유효성 체크
    public boolean validateToken(String token) 
    {
        try 
        {
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                .build()
                .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""));
            
            return true;
        } 
        catch (JWTVerificationException e) 
        {
        	log.error("[JwtAuthorizationFilter validateToken JWTVerificationException] ", e);
            return false;
        }
    }
    
    // 토큰에서 추출한 사용자 이름(userEmail) 반환
    public String getUsernameFromToken(String token) 
    {
        try 
        {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                                .build()
                                .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""));
            
            return jwt.getSubject();
        } 
        catch (Exception e) 
        {
            log.error("[JwtAuthorizationFilter getUsernameFromToken Exception] ", e);
            return null;
        }
    }
}
