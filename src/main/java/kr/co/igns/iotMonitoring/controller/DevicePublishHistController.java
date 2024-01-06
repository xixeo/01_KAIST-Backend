package kr.co.igns.iotMonitoring.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.iotMonitoring.model.DeviceControlHistoryDTO;
import kr.co.igns.iotMonitoring.model.DeviceControlHistoryVO;
import kr.co.igns.iotMonitoring.model.DeviceGroupVO;
import kr.co.igns.iotMonitoring.model.DeviceStatusVO;
import kr.co.igns.iotMonitoring.service.DataDisplayFormatService;
import kr.co.igns.iotMonitoring.service.DeviceControlHistService;
import kr.co.igns.iotMonitoring.service.DeviceService;
import lombok.RequiredArgsConstructor;

/**
 * @author lyj
 * Device 제어 이력 조회 
 */
@SuppressWarnings("unused")
@Api(tags = {"1.4 Device Control History"})
@RestController
@RequiredArgsConstructor
public class DevicePublishHistController {
	
	private final DeviceControlHistService deviceControlHistService;
	private final ResponseService responseService;
	private final ExceptionAdvice exceptionAdvice;

	  @ApiOperation(value = "디바이스 제어 이력", notes = "필수 : startDt, endDT format : 'YYYYMMDDHH24MISS' / 선택 : topic, clientId") 
	  @PostMapping(value = "/api/v1/getDevicePublishHistory") 
	  public CommonResult getDevicePublishHistory(@RequestBody DeviceControlHistoryDTO vo){
		  
		  System.out.println(vo);
		  
		  List<DeviceControlHistoryVO> result = deviceControlHistService.getDevicePublishHistory(vo); 
		  return responseService.getListResult(result); 
	  
	  }
	  
	
}
