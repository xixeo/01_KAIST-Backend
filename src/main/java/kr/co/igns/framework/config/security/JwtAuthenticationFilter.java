package kr.co.igns.framework.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
	
	private JwtTokenProvider jwtTokenProvider;

    // Jwt Provier 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 실제 필터링 로직은 doFilter 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    // Request로 들어오는 Jwt Token의 유효성을 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.
    // filterChain이 완료되어야 mvc로직에 접근
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    	// Request Header 에서 토큰을 꺼냄
    	String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
    	// validateToken 으로 토큰 유효성 검사
    	// 정상 토큰이면 해당 토큰으로 Authentication을 가져와서 SecurityContext 에 저장
        if (token != null && jwtTokenProvider.validateToken(token)) {
            //권한체크<시스템관리에서 메뉴권한 조정, api로 권한체크 안함>, 계정 구분값틀 통해 권한정보 및 개인정보를 포함한 Authentication을 생성
        	Authentication auth = jwtTokenProvider.getAuthentication(token);
        	// 생성한 Authentication을 SecurityContextHolder에 저장한다.
            SecurityContextHolder.getContext().setAuthentication(auth);
            //System.out.println("doFilter SecurityContextHolder getContext() : "+ SecurityContextHolder.getContext().getAuthentication());
        }
        filterChain.doFilter(request, response);
    }
    
    
    
    
}
