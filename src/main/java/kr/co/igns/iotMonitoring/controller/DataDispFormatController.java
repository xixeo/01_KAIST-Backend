package kr.co.igns.iotMonitoring.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.iotMonitoring.model.DataDispFormatSearchDTO;
import kr.co.igns.iotMonitoring.model.DataDispFormatVO;
import kr.co.igns.iotMonitoring.model.DeviceVO;
import kr.co.igns.iotMonitoring.service.DataDisplayFormatService;
import lombok.RequiredArgsConstructor;

/**
 * @author lyj
 * Device Data Display Config 관련
 */
@SuppressWarnings("unused")
@Api(tags = {"1.1 Data Display Config"})
@RestController
@RequiredArgsConstructor
//디바이스 기준정보 관련
public class DataDispFormatController {
	
	private final DataDisplayFormatService dataDisplayFormatService;
	private final ResponseService responseService;
	private final ExceptionAdvice exceptionAdvice;
	
	private static final Logger log = LogManager.getLogger("com.project");
	
	
	//post : 데이타 디스플레이 포맷 정보 조회
	@PostMapping(value="/api/v1/selectAllDataDisplayFormat")
	@ApiOperation(value = "1. POST selectAllDataDisplayFormat", notes = "POST selectAllDataDisplayFormat")
	public CommonResult selectAllDataDisplayFormat(@NotNull @RequestBody DataDispFormatSearchDTO vo) throws Exception {
		log.info(" 1.1 Data Display Config --> AllDataDisplayFormat API ");
		List<DataDispFormatVO> resultList = dataDisplayFormatService.selectAllDataDisplayFormat(vo);
		return responseService.getListResult(resultList);
	}
	
	@PostMapping(value="/api/v1/insertDisplayFormat")
	@ApiOperation(value = "1. POST 디스플레이 포맷 추가 ", notes = "POST insertDisplayFormat : ex) "
			+ "  \" \r\n{ " + 
			"  \"alignment\": \"center\",\r\n" + 
			"  \"description\": \"\",\r\n" + 
			"  \"dispGubn\": \"GROUP\",\r\n" + 
			"  \"dispGubnCode\": \"GRP_002\",\r\n" + 
			"  \"displayCol\": \"device_id\",\r\n" + 
			"  \"displayColNm\": \"디바이스아이디\",\r\n" + 
			"  \"displayOrder\": 0,\r\n" + 
			"  \"fieldtype\": \"string\",\r\n" + 
			"  \"fixed\": false,\r\n" + 
			"  \"format\": \"\",\r\n" + 
			"  \"insertId\": \"admin\",\r\n" + 
			"  \"max\": \"20\",\r\n" + 
			"  \"required\": true,\r\n" + 
			"  \"useYN\": \"Y\",\r\n" + 
			"  \"width\": \"130\"\r\n" + 
			"}")
	public CommonResult insertDisplayFormat(@NotNull @RequestBody DataDispFormatVO vo) throws Exception {
		log.info(" 1.1 Data Display Config --> insertDisplayFormat API ");
		int resultCode = dataDisplayFormatService.insertDisplayFormat(vo);
		return responseService.getSuccessResult();
		

	}
	
	@PostMapping(value="/api/v1/updateDisplayFormat")
	@ApiOperation(value = "1. POST 디스플레이 포맷 수정 ", notes = "POST updateDisplayFormat ex) "+
	        "  \r\n{" + 
			"  \"alignment\": \"center\",\r\n" + 
			"  \"description\": \"TEST\",\r\n" + 
			"  \"dispGubn\": \"GROUP\",\r\n" + 
			"  \"dispGubnCode\": \"GRP_002\",\r\n" + 
			"  \"displayCol\": \"device_id\",\r\n" + 
			"  \"displayColNm\": \"test\",\r\n" + 
			"  \"displayOrder\": 0,\r\n" + 
			"  \"fieldtype\": \"string\",\r\n" + 
			"  \"fixed\": true,\r\n" + 
			"  \"format\": \"\",\r\n" + 
			"  \"max\": \"50\",\r\n" + 
			"  \"required\": true,\r\n" + 
			"  \"updateId\": \"admin\",\r\n" + 
			"  \"useYN\": \"\",\r\n" + 
			"  \"width\": \"\"\r\n" + 
			"}" )
	public CommonResult updateDisplayFormat(@NotNull @RequestBody DataDispFormatVO vo) throws Exception {
		log.info(" 1.1 Data Display Config --> updateDisplayFormat API ");
		int resultCode = dataDisplayFormatService.updateDisplayFormat(vo);
		return responseService.getSuccessResult();
	}
	
	@PostMapping(value="/api/v1/deleteDisplayFormat")
	@ApiOperation(value = "1. POST 디스플레이 포맷 삭제 ", notes = "POST deleteDisplayFormat ex) "+
	        "\r\n[" + 
			"  {\r\n" + 
			"    \"dispGubn\": \"GROUP\",\r\n" + 
			"    \"dispGubnCode\": \"GRP_002\",\r\n" + 
			"    \"displayCol\": \"device_id\"\r\n" + 
			"  }\r\n" + 
			"]")
	public CommonResult deleteDisplayFormat(HttpServletRequest request, @RequestBody List<DataDispFormatVO> voList) throws Exception {
		log.info(" 1.1 Data Display Config --> deleteDisplayFormat API ");
		dataDisplayFormatService.deleteDisplayFormat(voList);
		return responseService.getSuccessResult();
	}
}
