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
import kr.co.igns.framework.comm.model.CmChildCodeVO;
import kr.co.igns.framework.comm.model.CmCodeReqDto;
import kr.co.igns.framework.comm.model.CmCodeVO;
import kr.co.igns.framework.comm.model.MenuReqDto;
import kr.co.igns.framework.comm.model.MenuVO;
import kr.co.igns.framework.comm.service.CmCodeService;
import kr.co.igns.framework.comm.service.MenuService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"5. CommonCode"})
@RestController
@RequiredArgsConstructor
public class CmCodeController {
	
	private final ResponseService responseService;
	private final ExceptionAdvice exceptionAdvice;
	private final CmCodeService cmCodeService;

	@ApiOperation(value="부모공통코드 전체 조회 ", notes="공통코드를 조회한다. param: DOMAIN_CD")
	@PostMapping(value="/api/getAllCmCode")
	public CommonResult getAllCmCode(@RequestBody CmCodeReqDto reqDto){
		List<CmCodeVO> list = cmCodeService.getAllCmCode(reqDto);
		return responseService.getListResult(list);
	}
	
	@ApiOperation(value="공통코드 조회 ", notes="공통코드를 조회한다. param: DOMAIN_CD, CODE_CD, CODE_NM, LEVEL, PRNTS_CODE")
	@PostMapping(value="/api/getCmCode")
	public CommonResult getCmCode(@RequestBody CmCodeReqDto reqDto){
		List<CmCodeVO> list = cmCodeService.getCmCode(reqDto); 
		return responseService.getListResult(list);
	}
	
	@ApiOperation(value="세부 공통코드 조회 ", notes="공통코드를 조회한다. param: DOMAIN_CD, CODE_CD, CODE_NM, LEVEL, PRNTS_CODE")
	@PostMapping(value="/api/getChildCmCodeList")
	public CommonResult getChildCmCodeList(@RequestBody CmCodeReqDto reqDto){
		List<CmChildCodeVO> list = cmCodeService.getChildCmCodeList(reqDto); 
		return responseService.getListResult(list);
	}
	
	@ApiOperation(value="공통코드 리스트 입력/수정", notes="메뉴리스트를 입력/수정한다.(__created__ = true 입력/__modified__ = 수정)", produces="application/json")
	@PostMapping(value = "/api/createCmCode")
	public CommonResult createCmCode(@NotNull @RequestBody List<CmCodeVO> voList) throws Exception {
		System.out.println("volist : " + voList);
		cmCodeService.createCmCode(voList);
		return responseService.getSuccessResult();
	}
	
	
	@ApiOperation(value="공통코드 삭제", notes="메뉴를 삭제한다.", produces="application/json")
	@PostMapping(value = "/api/deleteCmCode")
	public CommonResult deleteCmCode(HttpServletRequest request, @RequestBody List<CmCodeVO> voList) throws Exception{
		System.out.println("volist : " + voList);
		cmCodeService.deleteCmCode(voList);
		return responseService.getSuccessResult();
	}
    
}

