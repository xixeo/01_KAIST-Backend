package kr.co.igns.framework.config.security;

import java.lang.reflect.Member;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import kr.co.igns.framework.comm.model.LoginVO;
import kr.co.igns.framework.utils.etc.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: com.web.framework.igns.config.security
 * @ 파일명		: JwtTokenProvider.java
 * @ 기능명 		: JWT 토큰을 생성 및 검증 모듈
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2020. 9. 10.
 */
@RequiredArgsConstructor
@Component
@Log4j2
public class JwtTokenProvider {
	
	private static String secretKey = GlobalConstants.ADMIN_AUTH_TOKEN_KEY;

    public long tokenValidMilisecond = 1000L * 60 * 60 * 24 * 30; // 24시간 토큰 유효 -> 30일
    public long refreshTokenValidMilisecond = 1000L * 60 * 60 * 24 * 60; // 60일 토큰 유효
    private static String ip = null;
    
    private final UserDetailsService userDetailsService;
    

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    //	AccessToken 발급
    public String createAccessToken(LoginVO vo) {
    	log.info(" =============== Jwt access 토큰 생성 =============== ");
        Claims claims = Jwts.claims().setSubject(Integer.toString(vo.getUuid()));
        claims.put("domainCd", vo.getDomainCd());
        claims.put("userId", vo.getUserId());
        claims.put("ip", vo.getIp());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }
    
    //	RefreshToken 발급
//    public String createRefreshToken(LoginVO vo) {
//    	log.info(" =============== Jwt refresh 토큰 생성 =============== ");
//        Claims claims = Jwts.claims().setSubject(Integer.toString(vo.getUuid()));
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims) // 데이터
//                .setIssuedAt(now) // 토큰 발행일자
//                .setExpiration(new Date(now.getTime() + refreshTokenValidMilisecond)) // set Expire Time
//                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
//                .compact();
//    }
    
    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
    	log.info(" =============== Jwt 토큰으로 인증 정보를 조회 =============== ");
    	//DB에서 권한정보 및 유저정보 가져오기<현재 단일 권한정보만 조회> list처리가 필요하면 loadUserByUsername 로직 추가
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보(subject) 추출
    public String getUserPk(String token) {
    	log.info(" =============== Jwt 토큰에서 회원 구별 정보 추출 =============== ");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Jwt 토큰에서 클라이언트 IP 추출  
    public String getTokenIp(String token) {
//    	log.info(" =============== Jwt 토큰에서 클라이언트 IP 추출 =============== ");
    	String getIp = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("ip", String.class);
    	return getIp;
    }

    // Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
    // Request Header 에서 토큰 정보를 꺼내오기
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }
    

    // Jwt 토큰의 유효성 검증
    public boolean validateToken(String jwtToken) {
    	
    	boolean expire = false;
    	boolean ipFlag = false;
    	
        try {
        	log.info(" =============== Jwt 토큰의 유효성 + 만료일자 확인 ===============  ");
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return true;
            //if(!claims.getBody().getExpiration().before(new Date())) { //기간 내면 true
            //	expire = true;
            //} else {
            //	log.error("error Token : expire");
            //}
            //getClientIP(request);
            //if(this.getTokenIp(jwtToken).equals(this.ip)) { //request ip == token에 저장된 ip 
        	//	ipFlag = true;
        	//} else {
        	//	log.error("error Token : ip");
        	//}
            //if(expire && ipFlag) {
            //	return true;
            //}
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
        	log.error("error Token : " + e);
        } 
        return false;
    }
    
    public static String getClientIP(HttpServletRequest request) {
	    ip = request.getHeader("X-Forwarded-For");
	    if (ip == null) { ip = request.getHeader("Proxy-Client-IP"); }
	    if (ip == null) { ip = request.getHeader("WL-Proxy-Client-IP"); }
	    if (ip == null) { ip = request.getHeader("HTTP_CLIENT_IP"); }
	    if (ip == null) { ip = request.getHeader("HTTP_X_FORWARDED_FOR"); }
	    if (ip == null) { ip = request.getRemoteAddr(); }
	    return ip;
	}
    
    
    //token에서 데이터 조회
    public String getTokenData(String data, String token) {
        return extractAllClaims(token).get(data, String.class);
    }
    
    //만료된 토큰이어도 정보를 꺼내기 위해서 별도 함수로 분리
    public Claims extractAllClaims(String token) throws ExpiredJwtException {
    	try {
	        return Jwts.parser()
	                .setSigningKey(secretKey)
	                .parseClaimsJws(token)
	                .getBody();
    	} catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
