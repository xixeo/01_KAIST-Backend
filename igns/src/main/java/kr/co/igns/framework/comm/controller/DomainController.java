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
import kr.co.igns.framework.comm.model.DomainReqDto;
import kr.co.igns.framework.comm.model.DomainVO;
import kr.co.igns.framework.comm.service.DomainService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"6. System_Domain"})
@RestController
@RequiredArgsConstructor
public class DomainController {
	
	private final ResponseService responseService;
	private final DomainService domainService;
	
	@ApiOperation(value="전체 도메인코드 조회", notes="도메인코드를 조회한다 param: DOMAIN_STATUS_CD", produces="application/json")
	@PostMapping(value="/com/getAllDomainCd")
	public CommonResult getAllDomainCd(@RequestBody DomainReqDto reqDto){
		System.out.println("getAllDomainCd");
		DomainReqDto newReq = new DomainReqDto();
		newReq.setDomainStatusCd(reqDto.getDomainStatusCd());
		List<DomainVO> commonCodeVo = domainService.getAllDomain(newReq);
		return responseService.getListResult(commonCodeVo);
    }
	
	@ApiOperation(value="전체 도메인코드 조회", notes="도메인코드를 조회한다 param: DOMAIN_STATUS_CD", produces="application/json")
	@PostMapping(value="/api/getAllDomain")
	public CommonResult getAllDomain(@RequestBody DomainReqDto reqDto){
		System.out.println("getAllDomain");
		DomainReqDto newReq = new DomainReqDto();
		newReq.setDomainStatusCd(reqDto.getDomainStatusCd());
		List<DomainVO> commonCodeVo = domainService.getAllDomain(newReq);
		return responseService.getListResult(commonCodeVo);
    }
	
	@ApiOperation(value="서브도메인 리스트 조회", notes="서브 도메인 코드를 조회한다 param: DOMAIN_CD, DOMAIN_AUTH(미정)", produces="application/json")
	@PostMapping(value="/com/getSubDomain")
	public CommonResult getSubDomain(@RequestBody DomainReqDto reqDto){
		System.out.println("getSubDomain");
		List<DomainVO> commonCodeVo = domainService.getSubDomain(reqDto);
		return responseService.getListResult(commonCodeVo);
    }

	@ApiOperation(value="도메인코드 조회", notes="도메인코드를 조회한다 param: DOMAIN_STATUS_CD, DOMAIN_NM, DOMAIN_CD", produces="application/json")
	@PostMapping(value="/api/getDomain")
	public CommonResult getDomain(@RequestBody DomainReqDto reqDto){
		List<DomainVO> commonCodeVo = domainService.getDomain(reqDto);
		return responseService.getListResult(commonCodeVo);
    }
	
	@ApiOperation(value="도메인코드 입력/수정", notes="도메인코드를 입력/수정한다.(__created__ = true 입력/__modified__ = 수정)", produces="application/json")
	@PostMapping(value = "/api/createDomain")
	private CommonResult createDomain(@NotNull @RequestBody List<DomainVO> voList) throws Exception {
		domainService.createDomain(voList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value="도메인코드 삭제", notes="도메인코드를 삭제한다.", produces="application/json")
	@PostMapping(value = "/api/deleteDomain")
	public CommonResult deleteDomain(HttpServletRequest request, @RequestBody List<DomainVO> voList) throws Exception{
		domainService.deleteDomain(voList);
		return responseService.getSuccessResult();
	}

    
}
