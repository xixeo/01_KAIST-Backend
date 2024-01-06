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
import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.DomainVO;
import kr.co.igns.framework.comm.service.AsgnService;
import kr.co.igns.framework.comm.service.DomainService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"7. System_ASGN"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AsgnController {
	
	private final ResponseService responseService;
	private final AsgnService asgnService;
	
	@ApiOperation(value="조직정보 조회", notes="조직정보를 조회한다 param: DOMAIN_CD, ASGN_NM", produces="application/json")
	@PostMapping(value="/getAsgn")
	public CommonResult getAsgn(@RequestBody AsgnReqDto reqDto){
		List<AsgnVO> vo = asgnService.getAsgn(reqDto);
		return responseService.getListResult(vo);
    }
	
	@ApiOperation(value="조직코드 중복확인", notes="조직코드 중복확인한다 param: DOMAIN_CD, ASGN_CD", produces="application/json")
	@PostMapping(value="/checkAsgnCd")
	public CommonResult checkAsgnCd(@RequestBody AsgnReqDto reqDto){
		int count = asgnService.checkAsgnCd(reqDto);
		return responseService.getSingleResult(count);
    }


	@ApiOperation(value="조직코드 입력/수정", notes="조직코드를 입력/수정한다.(__created__ = true 입력/__modified__ = 수정)", produces="application/json")
	@PostMapping(value = "/createAsgn")
	private CommonResult createAsgn(@NotNull @RequestBody List<AsgnVO> voList) throws Exception {
		asgnService.createAsgn(voList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value="도메인코드 삭제", notes="도메인코드를 삭제한다.", produces="application/json")
	@PostMapping(value = "/deleteAsgn")
	public CommonResult deleteAsgn(HttpServletRequest request, @RequestBody List<AsgnVO> voList) throws Exception{
		asgnService.deleteAsgn(voList);
		return responseService.getSuccessResult();
	}
    
}
