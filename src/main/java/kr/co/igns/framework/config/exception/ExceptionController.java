package kr.co.igns.framework.config.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.security.SecurityAuthenticationEntryPoint;

@Api(tags = {"#. Exception"})
@RequiredArgsConstructor
@RestController
@Log4j2
public class ExceptionController {
	//webSecurity exception 처리
	
	@ApiOperation(value = "에러처리", notes = "진입 메서드가 없을때 에러 처리.")
    @GetMapping(value = "/exception/entrypoint")
    public CommonResult entrypointException() {
		log.info("============세션만료 에러처리============");
        throw new CAuthenticationEntryPointException();
    }

	@ApiOperation(value = "에러처리", notes = "권한이 없을때 에러 처리.")
    @GetMapping(value = "/exception/accessdenied")
    public CommonResult accessdeniedException() {
        throw new AccessDeniedException("");
    }
}
