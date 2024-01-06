package kr.co.igns.framework.comm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.sf.json.JSON;
import springfox.documentation.spring.web.json.Json;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.sample.model.SampleVO;
import kr.co.igns.framework.comm.dao.postgre.LoginDao;
import kr.co.igns.framework.comm.dao.postgre.TokenDao;
import kr.co.igns.framework.comm.model.LoginReqDto;
import kr.co.igns.framework.comm.model.LoginVO;
import kr.co.igns.framework.comm.model.TokenDto;
import kr.co.igns.framework.comm.model.TokenVO;
import kr.co.igns.framework.config.exception.CEmailSigninFailedException;
import kr.co.igns.framework.config.exception.CUserNotFoundException;
import kr.co.igns.framework.config.response.MapResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.config.security.JwtTokenProvider;
import kr.co.igns.framework.utils.type.DateUtils;

@RequiredArgsConstructor
@Service
@Log4j2
public class TokenService{

	private final TokenDao tokenDao;
	private final LoginDao loginDao;
	private final JwtTokenProvider jwtTokenProvider;
	private final DateUtils dateUtils;
	private final ResponseService responseService;
	
	
	public String findBykey(LoginReqDto loginDto) {
		int uuid = getUuid(loginDto);
		String refreshToken = tokenDao.findBykey(Integer.toString(uuid));
		
        return refreshToken;
    }
	
	public Date getTokenExpirationDt(String refreshToken) {
		Date expirationDate = tokenDao.getTokenExpirationDt(refreshToken);	
        return expirationDate;
    }
	
	public int createRefreshToken(TokenVO vo) {
		int count = tokenDao.checkfreshToken(vo);
		
		if(count > 0) {
			tokenDao.updateRefreshToken(vo);
		} else {
			tokenDao.createRefreshToken(vo);
		}
		
		
		return 0;
    }
	
	public void deleteRefreshToken(List<TokenVO> voList) {
		// TODO Auto-generated method stub
		for(TokenVO vo : voList) {
			tokenDao.deleteRefreshToken(vo);
		}
	}
	
	public void deleteExpirationToken() {
		tokenDao.deleteExpirationToken();
	}
	
	
	@Transactional
    public TokenVO reissue(TokenDto requestDto, HttpServletRequest req) {
		log.info("**********token reissue --> " + requestDto.getRefreshToken());
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(requestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID, 기타 데이터 가져오기
        // 만료된 토큰에 접근하면 에러처리가 됨... 확인해보기
        //Authentication authentication = jwtTokenProvider.getAuthentication(requestDto.getToken());
        //String uuid = jwtTokenProvider.getUserPk(requestDto.getToken());
        //String ip = jwtTokenProvider.extractAllClaims(requestDto.getToken()).get("ip", String.class);
        
        // 3. DB에서 Member ID(uuid) 를 기반으로 Refresh Token 값 가져옴
        LoginReqDto loginDto = new LoginReqDto();
        loginDto.setUserId(requestDto.getUserId());
        loginDto.setDomainCd(requestDto.getDomainCd());
        String beforeRefreshToken = this.findBykey(loginDto);
        Date beforeRefreshTokenExpiration = getTokenExpirationDt(beforeRefreshToken);
        
        // 4. Refresh Token 일치하는지 검사
        if (!beforeRefreshToken.equals(requestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        LoginVO vo = new LoginVO();
        vo.setUuid(getUuid(loginDto));
        vo.setUserId(requestDto.getUserId());
        vo.setDomainCd(requestDto.getDomainCd());
        vo.setIp(JwtTokenProvider.getClientIP(req));

        // 6. 저장소 정보 업데이트
        TokenVO tokenVO = new TokenVO();
        
        //엑세스토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(vo);
        tokenVO.setAccessToken(accessToken);
        
        //만료일자 일주일전에만 리프레쉬토큰 재발급
//        Long afterDay = 7l;
//        Date afterDate = dateUtils.nowAfterDaysToDate(afterDay);
//    	if (beforeRefreshTokenExpiration.before(afterDate)) {
//    		String refreshToken = jwtTokenProvider.createRefreshToken(vo);
//    		Date refreshExpiration = jwtTokenProvider.extractAllClaims(refreshToken).getExpiration();
//    		tokenVO.setRefreshToken(refreshToken);
//    		
//    		//refresh token 정보 저장
//    		TokenVO tVO = new TokenVO();
//    		tVO.setKey(Integer.toString(vo.getUuid()));
//    		tVO.setValue(refreshToken);
//    		tVO.setExpirationDt(refreshExpiration);
//            createRefreshToken(tVO);
//    	}

        // 토큰 발급
        return tokenVO;
    }
	
	
	private int getUuid(LoginReqDto loginDto) {
		LoginVO vo = loginDao.login(loginDto);
		int uuid = vo.getUuid();
		return uuid;
	}
	

}
