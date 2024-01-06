package kr.co.igns.framework.comm.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.igns.framework.comm.model.CmCodeReqDto;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.comm.model.LoginReqDto;
import kr.co.igns.framework.comm.model.LoginVO;
import kr.co.igns.framework.comm.model.MenuReqDto;
import kr.co.igns.framework.comm.model.MenuVO;
import kr.co.igns.framework.comm.model.UserCertVO;
import kr.co.igns.framework.comm.model.UserReqDto;
import kr.co.igns.framework.comm.model.UserVO;
import kr.co.igns.framework.comm.service.LoginService;
import kr.co.igns.framework.comm.service.UserService;
import kr.co.igns.framework.config.exception.CEmailSigninFailedException;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.config.response.SingleResult;
import kr.co.igns.framework.config.security.JwtTokenProvider;

@Api(tags = {"5. User"})
@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final ResponseService responseService;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;


	@ApiOperation(value="유저정보 조회 ", notes="유저정보를 조회한다. param: DOMAIN_CD, USER_ID, USER_NM")
	@PostMapping(value="/com/getUser")
	public CommonResult getUser(@RequestBody UserReqDto reqDto){
		List<UserVO> list = userService.getUser(reqDto); 
		return responseService.getListResult(list);
	}
	
	/*****수정중*******/
	@ApiOperation(value="비밀번호 체크(현재비번) ", notes="비밀번호 체크(현재비번)한다. param: DOMAIN_CD, USER_ID, USER_PW")
	@PostMapping(value="/api/checkAdminPw")
	public CommonResult checkAdminPw(@RequestBody UserReqDto reqDto){
		boolean result = userService.checkAdminPw(reqDto);
		if(result) {
			return responseService.getSuccessResult();
		} else {
			return responseService.getFailResult(-1200, "비밀번호 불일치");
		}
	}
	
	/*****수정중*******/
	@ApiOperation(value="사용자 비밀번호 변경 ", notes="사용자 비밀번호를 변경한다. param: DOMAIN_CD, USER_ID, USER_PW")
	@PostMapping(value="/api/updateUserPass")
	public CommonResult updateUserPass(HttpServletRequest request, @RequestBody UserVO voList){
		userService.updateUserPass(voList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value="유저정보 입력/수정", notes="유저정보를 입력/수정한다.(__created__ = true 입력/__modified__ = 수정)", produces="application/json")
	@PostMapping(value = "/api/createUser")
	public CommonResult createUser(@NotNull @RequestBody List<UserVO> voList) throws Exception {
		userService.createUser(voList);
		return responseService.getSuccessResult();
	}
    
	@ApiOperation(value="유저정보 삭제", notes="유저정보를 삭제한다.", produces="application/json")
	@PostMapping(value = "/api/deleteUser")
	public CommonResult deleteUser(HttpServletRequest request, @RequestBody List<UserVO> voList) throws Exception{
		userService.deleteUser(voList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value = "사용자정보조회", notes = "로그인 param: uuid")
    @PostMapping(value = "/com/findByUserId")
    public CommonResult findByUserId(@RequestBody UserReqDto reqDto) {

		List<UserCertVO> userAuthes = userService.findByUserId(reqDto);
		return responseService.getListResult(userAuthes);
    }
}

