package kr.co.igns.framework.comm.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.igns.business.sample.model.SampleVO;
import kr.co.igns.framework.comm.model.LoginReqDto;
import kr.co.igns.framework.comm.model.LoginVO;
import kr.co.igns.framework.comm.model.TokenDto;
import kr.co.igns.framework.comm.model.TokenVO;
import kr.co.igns.framework.comm.service.LoginService;
import kr.co.igns.framework.comm.service.TokenService;
import kr.co.igns.framework.config.exception.CEmailSigninFailedException;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.config.response.SingleResult;
import kr.co.igns.framework.config.security.JwtTokenProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Api(tags = {"9. Token"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/com")
public class TokenController {
	
	private final ResponseService responseService;
	private final TokenService tokenService;
	
	
	@ApiOperation(value = "refresh token 발급", notes = "리프레쉬토큰 재발급 param: token, refreshToken, userId, domainCd")
    @PostMapping(value = "/reissue")
    public CommonResult reissue(@RequestBody TokenDto tokenDto, HttpServletRequest req, HttpServletResponse res) {
        System.out.println("***/reissue");
		TokenVO result = tokenService.reissue(tokenDto, req);
		return responseService.getSingleResult(result);
    }
	
	
	@ApiOperation(value = "refresh token 조회", notes = "리프레쉬토큰조회 param: domainCd, userId")
    @PostMapping(value = "/findBykey")
    public CommonResult findBykey(@RequestBody LoginReqDto loginDto, HttpServletRequest req, HttpServletResponse res) {
        
		String result = tokenService.findBykey(loginDto);
		return responseService.getSingleResult(result);
    }
	
	@ApiOperation(value="refresh token 생성", notes="INSERT refresh token", produces="application/json")
	@PostMapping(value = "/createRefreshToken")
	private CommonResult createRefreshToken(@NotNull @RequestBody TokenVO vo) throws Exception {
		int resultCode = tokenService.createRefreshToken(vo);
		return responseService.getSuccessResult();
	}
	
	
	@ApiOperation(value="refresh token 삭제", notes="DELETE refresh token.", produces="application/json")
	@PostMapping(value = "/deleteRefreshToken")
	public CommonResult deleteRefreshToken(HttpServletRequest request, @RequestBody List<TokenVO> voList) {
		tokenService.deleteRefreshToken(voList);
		return responseService.getSuccessResult();
	}
	
    
    
}

