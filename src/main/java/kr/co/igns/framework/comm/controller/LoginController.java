package kr.co.igns.framework.comm.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.igns.framework.comm.model.LoginReqDto;
import kr.co.igns.framework.comm.model.LoginVO;
import kr.co.igns.framework.comm.service.LoginService;
import kr.co.igns.framework.config.exception.CEmailSigninFailedException;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.config.response.SingleResult;
import kr.co.igns.framework.config.security.JwtTokenProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Api(tags = {"2. Login"})
@RestController
@RequiredArgsConstructor
public class LoginController {
	
	private static final Logger log = LogManager.getLogger("com.project");
	private final ResponseService responseService;
	private final LoginService loginService;
	private final PasswordEncoder passwordEncoder;
	
	@ApiOperation(value = "로그인", notes = "로그인 param: domainCd, userId, userPw")
    @PostMapping(value = "/com/login")
    public CommonResult login(@RequestBody LoginReqDto loginDto, HttpServletRequest req, HttpServletResponse res) throws NoSuchAlgorithmException {
        
		Map<String, String> resultMap = loginService.login(loginDto, req);
		return responseService.getMapResult(resultMap);
    }
	
	
	@ApiOperation(value = "로그인", notes = "로그인 param: domainCd, userId, userPw")
    @PostMapping(value = "/com/refreshToken")
    public CommonResult refreshToken(@RequestBody LoginReqDto loginDto, HttpServletRequest req, HttpServletResponse res) {
        
		Map<String, String> resultMap = loginService.login(loginDto, req);
		return responseService.getMapResult(resultMap);
    }
	
	
	@ApiOperation(value="통합로그인 인증", notes="통합로그인 인증", httpMethod="POST")
    @PostMapping(value = "/com/sso")
    public SingleResult<Void> loginSSO(HttpServletRequest req, HttpServletResponse res) throws Exception {
		/*
        String userId = "";
        SSOConfig.request = req;
        AuthCheck auth = new AuthCheck(req, res);

        try {
            AuthStatus status = auth.CheckLogon(AuthCheckLevel.Medium);

            if (status == AuthStatus.SSOFirstAccess) { // 최초 SSO연결인경우
                log.debug("######################### AuthStatus.SSOFirstAccess #########################");
                auth.TrySSO();
            } else if (status == AuthStatus.SSOSuccess) { // 인증 성공
                log.debug("######################### AuthStatus.SSOSuccess #########################");
                userId = auth.UserID();

                IgnsLoginReqDto IgnsLoginReqDto = new IgnsLoginReqDto();
                IgnsLoginReqDto.setUser_id(userId);

                ResDto.IgnsMemberDto IgnsMemberDto = IgnsLoginService.sso(req, IgnsLoginReqDto);
                if (IgnsMemberDto != null && !"".equals(IgnsMemberDto.getUser_id())) {

                    IgnsMember member = IgnsMember.builder()
                            .user_id(IgnsMemberDto.getUser_id())
                            .kor_nm(IgnsMemberDto.getKor_nm())
                            .build();

                    Gson gson = new Gson();
                    String json = gson.toJson(member);
                    req.getSession(true).setAttribute(TokenConstant.MEMBER, json);
                } else {
                    throw new BizException("loginfail");
                }

            } else if (status == AuthStatus.SSOFail) { // SSO 연결을 실패한 경우
                log.debug("######################### AuthStatus.SSOFail #########################");
                throw new BizException("sso_fail");
            } else if (status == AuthStatus.SSOUnAvaliable) { // SSO 서버가 사용불가인 경우
                log.debug("######################### AuthStatus.SSOUnAvaliable #########################");
                throw new BizException("sso_unavailable");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizException("loginfail");
        }
		*/
        return (SingleResult<Void>) null;
    }
	
	
	
    
    
}

