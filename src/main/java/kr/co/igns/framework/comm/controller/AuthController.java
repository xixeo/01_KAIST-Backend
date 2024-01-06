package kr.co.igns.framework.comm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.framework.comm.model.AsgnReqDto;
import kr.co.igns.framework.comm.model.AsgnVO;
import kr.co.igns.framework.comm.model.AuthMenuUserVO;
import kr.co.igns.framework.comm.model.AuthMenuVO;
import kr.co.igns.framework.comm.model.AuthReqDto;
import kr.co.igns.framework.comm.model.AuthUserVO;
import kr.co.igns.framework.comm.model.AuthVO;
import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.DomainVO;
import kr.co.igns.framework.comm.service.AsgnService;
import kr.co.igns.framework.comm.service.AuthService;
import kr.co.igns.framework.comm.service.DomainService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"8. System_Auth"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
	
	private final ResponseService responseService;
	private final AuthService authService;
	
	@ApiOperation(value="권한그룹 조회", notes="권한그룹을 조회한다 param: DOMAIN_CD, authgrp_cd, authgrp_nm", produces="application/json")
	@PostMapping(value="/getAuthGroup")
	public CommonResult getAuthGroup(@RequestBody AuthReqDto reqDto){
		List<AuthVO> vo = authService.getAuthGroup(reqDto);
		return responseService.getListResult(vo);
    }
	

	@ApiOperation(value="권한그룹 입력/수정", notes="권한그룹을 입력/수정한다.(__created__ = true 입력/__modified__ = 수정)", produces="application/json")
	@PostMapping(value = "/createAuthGroup")
	private CommonResult createAuthGroup(@NotNull @RequestBody List<AuthVO> voList) throws Exception {
		authService.createAuthGroup(voList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value="권한그룹 삭제", notes="권한그룹을 삭제한다.", produces="application/json")
	@PostMapping(value = "/deleteAuthGroup")
	public CommonResult deleteAuthGroup(HttpServletRequest request, @RequestBody List<AuthVO> voList) throws Exception{
		authService.deleteAuthGroup(voList);
		return responseService.getSuccessResult();
	}
	
	
	
	
	@ApiOperation(value="권한유저 조회", notes="권한유저를 조회한다 param: DOMAIN_CD, AUTHGRP_CD, AUTHGRP_ASG_CLS", produces="application/json")
	@PostMapping(value="/getUserAuthGroup")
	public CommonResult getUserAuthGroup(@RequestBody AuthReqDto reqDto){
		List<AuthUserVO> vo = authService.getUserAuthGroup(reqDto);
		return responseService.getListResult(vo);
    }
	

	@ApiOperation(value="권한유저 입력/수정", notes="권한유저를 입력/수정한다.(__created__ = true 입력/__modified__ = 수정)", produces="application/json")
	@PostMapping(value = "/createUserAuthGroup")
	private CommonResult createUserAuthGroup(@NotNull @RequestBody List<AuthUserVO> voList) throws Exception {
		authService.createUserAuthGroup(voList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value="권한유저 삭제", notes="권한유저를 삭제한다.", produces="application/json")
	@PostMapping(value = "/deleteUserAuthGroup")
	public CommonResult deleteUserAuthGroup(HttpServletRequest request, @RequestBody List<AuthUserVO> voList) throws Exception{
		authService.deleteUserAuthGroup(voList);
		return responseService.getSuccessResult();
	}
	
	
	
	
	@ApiOperation(value="권한메뉴 조회", notes="권한메뉴를 조회한다 param: DOMAIN_CD, AUTHGRP_CD", produces="application/json")
	@PostMapping(value="/getMenuAuthGroup")
	public CommonResult getMenuAuthGroup(@RequestBody AuthReqDto reqDto){
		List<AuthMenuVO> vo = authService.getMenuAuthGroup(reqDto);
		return responseService.getListResult(vo);
    }
	

	@ApiOperation(value="권한메뉴 입력/수정", notes="권한메뉴를 입력/수정한다.(__created__ = true 입력/__modified__ = 수정)", produces="application/json")
	@PostMapping(value = "/createMenuAuthGroup")
	private CommonResult createMenuAuthGroup(@NotNull @RequestBody List<AuthMenuVO> voList) throws Exception {
		authService.createMenuAuthGroup(voList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value="권한메뉴 삭제", notes="권한메뉴를 삭제한다.", produces="application/json")
	@PostMapping(value = "/deleteMenuAuthGroup")
	public CommonResult deleteMenuAuthGroup(HttpServletRequest request, @RequestBody List<AuthMenuVO> voList) throws Exception{
		authService.deleteMenuAuthGroup(voList);
		return responseService.getSuccessResult();
	}
	
	
	
	@ApiOperation(value="사용자별 권한메뉴 조회", notes="사용자별 권한메뉴를 조회한다 param: DOMAIN_CD, USER_ID", produces="application/json")
	@PostMapping(value="/getMenuUserAuth")
	public CommonResult getMenuUserAuth(@RequestBody AuthReqDto reqDto){
		List<AuthMenuUserVO> vo = authService.getMenuUserAuth(reqDto);
		return responseService.getListResult(vo);
    }

	@ApiOperation(value="사용자별 권한메뉴 입력", notes="사용자별 권한메뉴를 입력한다.", produces="application/json")
	@PostMapping(value = "/createMenuUserAuth")
	private CommonResult createMenuUserAuth(@NotNull @RequestBody List<AuthMenuUserVO> voList) throws Exception {
		authService.createMenuUserAuth(voList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value="사용자별 권한메뉴 삭제", notes="사용자별 권한메뉴를 삭제한다.", produces="application/json")
	@PostMapping(value = "/deleteMenuUserAuth")
	public CommonResult deleteMenuUserAuth(HttpServletRequest request, @RequestBody List<AuthMenuUserVO> voList) throws Exception{
		authService.deleteMenuUserAuth(voList);
		return responseService.getSuccessResult();
	}
	
	
	
	
}
