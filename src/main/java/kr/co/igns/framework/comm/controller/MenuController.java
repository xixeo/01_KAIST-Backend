package kr.co.igns.framework.comm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.framework.comm.model.MenuReqDto;
import kr.co.igns.framework.comm.model.MenuVO;
import kr.co.igns.framework.comm.model.UserMenuVO;
import kr.co.igns.framework.comm.service.MenuService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import lombok.RequiredArgsConstructor;



@Api(tags = {"3. menu"})
@RestController
@RequiredArgsConstructor
//@RequestMapping(value="/menu", headers="X-APIVERSION=2.0.0")
public class MenuController {
	
	private final ResponseService responseService;
	private final ExceptionAdvice exceptionAdvice;
	private final MenuService menuService;
	
	@ApiOperation(value="사용자 메뉴 조회", notes="세션정보로 메뉴 조회 param: domainCd, userId")
	@PostMapping(value="/api/getUserMenu")
	public CommonResult getUserMenu(@RequestBody MenuReqDto reqDto){

		
		//사용자별 메뉴권한
		List<UserMenuVO> menuList = menuService.getUserMenu(reqDto);
		 
		
		if (menuList.size() < 1) {
			return responseService.getFailResult(Integer.valueOf(exceptionAdvice.getMessage("noResultFound.code")), exceptionAdvice.getMessage("noResultFound.msg"));
		}
		
		return responseService.getListResult(menuList);
	}
	
	
	@ApiOperation(value="도메인코드로 메뉴조회 ", notes="도메인코드로 조회 param: domainCd")
	@PostMapping(value="/api/getMenuByDomain")
	public CommonResult getMenuByDomain(@RequestBody MenuReqDto reqDto){
		//도메인코드의 메뉴리스트
		List<MenuVO> menuList = menuService.getMenuByDomain(reqDto); 
		return responseService.getListResult(menuList);
	}
	
	@ApiOperation(value="하위메뉴조회 ", notes="param: domainCd, menuCd")
	@PostMapping(value="/api/getLowerLevelMenu")
	public CommonResult getLowerLevelMenu(@RequestBody MenuReqDto reqDto){
		//도메인코드의 메뉴리스트
		List<MenuVO> menuList = menuService.getLowerLevelMenu(reqDto); 
		return responseService.getListResult(menuList);
	}

	@ApiOperation(value="메뉴리스트 입력/수정", notes="메뉴리스트를 입력/수정한다.(__created__ = true 입력/__modified__ = 수정)", produces="application/json")
	@PostMapping(value = "/api/createMenu")
	public CommonResult createMenu(@NotNull @RequestBody List<MenuVO> voList) throws Exception {
		menuService.createMenu(voList);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value="메뉴 삭제", notes="메뉴를 삭제한다.", produces="application/json")
	@PostMapping(value = "/api/deleteMenu")
	public CommonResult deleteMenu(HttpServletRequest request, @RequestBody List<MenuVO> voList) throws Exception{
		menuService.deleteMenu(voList);
		return responseService.getSuccessResult();
	}
    
}

