/**
 * 
 */
package kr.co.igns.iotMonitoring.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.business.sample.model.SampleDTO;
import kr.co.igns.business.sample.model.SampleVO;
import kr.co.igns.business.sample.service.SampleService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.iotMonitoring.model.DeviceRecDataDTO;
import kr.co.igns.iotMonitoring.service.InfluxdbService;
import lombok.RequiredArgsConstructor;

/**
 * @author lyj
 *
 */
@Api(tags = {"1.2 influxdb"})
@RestController
@RequiredArgsConstructor
public class InfluxdbController {

	private final InfluxdbService influxdbService;
	private final ResponseService responseService;
	private final ExceptionAdvice exceptionAdvice;
	private static final Logger log = LogManager.getLogger("com.project");
	
	//POST
/*	@PostMapping(value="/api/v1/")
	@ApiOperation(value = "4. POST test", notes = "POST test")
	public CommonResult testPostMapping(@RequestBody SampleDTO sample) {
		log.info(" 0. Sample --> testPostMapping API " + sample);
		List<SampleVO> resultList = sampleService.testPostMapping(sample);
		return responseService.getListResult(resultList);
	}*/
	
	
	
	  @ApiOperation(value = "수집 데이터 조회", notes = "param:deviceId, groupId, startDt(ex : 2021-09-01 00:00:00), endDt, limitCnt") 
	  @PostMapping(value = "/api/v1/getDeviceRowData") public CommonResult
	  getDeviceRowData(@RequestBody DeviceRecDataDTO dto){
	  
	  List<Map<String, String>> result = influxdbService.getDeviceRowDataByTime(dto); 
	  return responseService.getListResult(result); }
	 
}
