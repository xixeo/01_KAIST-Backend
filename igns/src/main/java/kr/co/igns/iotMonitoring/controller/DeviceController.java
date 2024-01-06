package kr.co.igns.iotMonitoring.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.business.sample.model.SampleDTO;
import kr.co.igns.business.sample.model.SampleVO;
import kr.co.igns.business.sample.service.SampleService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.iotMonitoring.model.DeviceGroupVO;
import kr.co.igns.iotMonitoring.model.DeviceVO;
import kr.co.igns.iotMonitoring.service.DeviceService;
import lombok.RequiredArgsConstructor;


/**
 * @author lyj
 * Device 관련 컨트롤러 
 */
@SuppressWarnings("unused")
@Api(tags = {"1. Device"})
@RestController
@RequiredArgsConstructor
//디바이스 기준정보 관련

public class DeviceController {

	private final DeviceService deviceService;
	private final ResponseService responseService;
	private final ExceptionAdvice exceptionAdvice;
	
	private static final Logger log = LogManager.getLogger("com.project");
	
	//get : 디바이스 그룹 조회
	@RequestMapping(value = "/api/v1/Groups/{Group}", method=RequestMethod.GET)
	@ApiOperation(value = "1. GET Group", notes = "get Group")
	@ResponseBody
	public CommonResult selectDeviceGroup(@PathVariable(value="Group") String Group){
		log.info(" 1. Device --> selectDeviceGroup API " + Group);
				
		List<DeviceGroupVO> resultList = deviceService.selectGroup(Group);
		return responseService.getListResult(resultList);
	}
	
	//get : 디바이스 조회
	@RequestMapping(value = "/api/v1/Devices/{Device}", method=RequestMethod.GET)
	@ApiOperation(value = "1. GET Device", notes = "get Device")
	@ResponseBody
	public CommonResult selectDevices(@PathVariable(value="Device") String Device){
		log.info(" 1. Device --> selectDevices API " + Device);
				
		List<DeviceVO> resultList = deviceService.selectDevices(Device);
		return responseService.getListResult(resultList);
	}
	
	//get : 특정 그룹의 디바이스 정보 조회
	@RequestMapping(value = "/api/v1/{Group}/{Device}", method=RequestMethod.GET)
	@ApiOperation(value = "2. GET Device", notes = "get Device")
	@ResponseBody
	public CommonResult selectDevice(@PathVariable(value="Group") String Group, @PathVariable(value="Device") String Device){
		log.info(" 1. Device --> selectDevice API " + Group + '/' + Device);
		
		DeviceVO oParam = new DeviceVO();
		oParam.setDeviceGrpId(Group.toUpperCase());
		oParam.setDeviceId(Device.toUpperCase());
		
		List<DeviceVO> resultList = deviceService.selectDevice(oParam);
		return responseService.getListResult(resultList);
	}
	
	//get : 특정 gs1키(like)의 디바이스 정보 조회
	@RequestMapping(value = "/api/v1/Device/{UniqueID}", method=RequestMethod.GET)
	@ApiOperation(value = "2. GET Device", notes = "get Device(like UniqueID)")
	@ResponseBody
	public CommonResult selectDeviceByUniqueID(@PathVariable(value="UniqueID") String UniqueID){
		log.info(" 1. Device --> selectDeviceByUniqueID API " + UniqueID );

		List<DeviceVO> resultList = deviceService.selectDeviceByUniqueID(UniqueID);
		return responseService.getListResult(resultList);
	}
	
	//get : 모든 디바이스 목록 조회
	@RequestMapping(value = "/api/v1/{Group}/Devices", method=RequestMethod.GET)
	@ApiOperation(value = "3. GET Devices of Group", notes = "get Devices of Group")
	@ResponseBody
	public CommonResult selectGroupoDevices(@PathVariable(value="Group") String Group){
		log.info(" 1. Device --> selectGroupoDevices API " + Group);
		
		List<DeviceVO> resultList = deviceService.selectGroupDevices(Group.toUpperCase());
		return responseService.getListResult(resultList);
	}
	
		
	//post : 모든 디바이스 그룹 조회
	@PostMapping(value="/api/v1/AllGroups")
	@ApiOperation(value = "4. POST AllGroups", notes = "POST AllGroups")
	public CommonResult selectAllDeviceGroups() {
		log.info(" 1. Device --> selectAllDeviceGroups API ");
		List<DeviceGroupVO> resultList = deviceService.selectAllGroups();
		return responseService.getListResult(resultList);
	}
	
	//post : 모든 디바이스  조회
	@PostMapping(value="/api/v1/AllDevices")
	@ApiOperation(value = "5. POST AllDevices", notes = "POST AllDevices")
	public CommonResult selectAllDevices() {
		log.info(" 1. Device --> selectAllDevices API ");
		List<DeviceVO> resultList = deviceService.selectAllDevices();
		return responseService.getListResult(resultList);
	}
	
	//post : 그룹들의 모든 디바이스의 정보 
	@PostMapping(value="/api/v1/DevicesInGroups")
	@ApiOperation(value = "6. POST DevicesInGroups", notes = "POST DevicesInGroups [param: group id]")
	public CommonResult DevicesInGroups(HttpServletRequest request, @RequestBody List<DeviceGroupVO> voList) {
		log.info(" 1. Device --> selectDevicesInGroups API ");
		List<DeviceVO> resultList = deviceService.selectDevicesInGroups(voList);
		return responseService.getListResult(resultList);
	}
	
	
	//post : 그룹 추가
	@ApiOperation(value = "7. POST InsertGroup", notes = "POST InsertGroup", produces="application/json")
	@PostMapping(value="/api/v1/InsertGroup" )
	public CommonResult InsertGroup(@NotNull @RequestBody DeviceGroupVO vo) throws Exception {
		log.info(" 1. Device --> InsertGroup API ");
		int resultCode = deviceService.InsertGroup(vo, true);
		return responseService.getSuccessResult();
	}
	
	//post : 그룹 변경
	@ApiOperation(value = "8. POST UpdateGroup", notes = "POST UpdateGroup", produces="application/json")
	@PostMapping(value="/api/v1/UpdateGroup" )
	public CommonResult UpdateGroup(@NotNull @RequestBody DeviceGroupVO vo) throws Exception {
		log.info(" 1. Device --> UpdateGroup API ");
		vo.setDeviceGrpId(vo.getDeviceGrpId().toUpperCase());
		int resultCode = deviceService.InsertGroup(vo, false);
		return responseService.getSuccessResult();
	}
	
	//post : 그룹 삭제(복수)
	@ApiOperation(value="9. POST DeleteGroups", notes=" POST DeleteGroups [param: group id]", produces="application/json")
	@PostMapping(value = "/api/v1/DeleteGroups")
	public CommonResult DeleteGroups(HttpServletRequest request, @RequestBody List<DeviceGroupVO> voList) {
		deviceService.DeleteGroups(voList);
		return responseService.getSuccessResult();
	}
	
	//post : 디바이스 추가
	@ApiOperation(value = "10. POST InsertDevice", notes = "POST InsertDevice [param: group id]", produces="application/json")
	@PostMapping(value="/api/v1/InsertDevice" )
	public CommonResult InsertDevice(@NotNull @RequestBody DeviceVO vo) throws Exception {
		log.info(" 1. Device --> InsertDevice API ");
		
		//디폴트 타임아웃 설정 60분
		if(vo.getDeviceTimeout() == 0) vo.setDeviceTimeout(60);
		int resultCode = deviceService.InsertDevice(vo, true);
		return responseService.getSuccessResult();
	}
	
	//post : 디바이스 변경
	@ApiOperation(value = "11. POST UpdateDevice", notes = "POST UpdateDevice [param: device id, device unique id]", produces="application/json")
	@PostMapping(value="/api/v1/UpdateDevice" )
	public CommonResult UpdateDevice(@NotNull @RequestBody DeviceVO vo) throws Exception {
		log.info(" 1. Device --> UpdateDevice API ");
		
		//디폴트 타임아웃 설정 60분
		if(vo.getDeviceTimeout() == 0) vo.setDeviceTimeout(60);
		vo.setDeviceId(vo.getDeviceId().toUpperCase());
		vo.setDeviceUniqueId(vo.getDeviceUniqueId().toUpperCase());
		int resultCode = deviceService.InsertDevice(vo, false);
		return responseService.getSuccessResult();
	}
	
	//post : 디바이스 삭제(복수)
	@ApiOperation(value="12. POST DeleteDevices", notes=" POST DeleteDevices [param: device id, device unique id]", produces="application/json")
	@PostMapping(value = "/api/v1/DeleteDevices")
	public CommonResult DeleteDevices(HttpServletRequest request, @RequestBody List<DeviceVO> voList) {
		deviceService.DeleteDevices(voList);
		return responseService.getSuccessResult();
	}
	
	//post : 여러개의 그룹들에서 포함된 디바이스들의 unscribe 대상이 되는 토픽 리스트 
	@PostMapping(value="/api/v1/UnscribeTopicsInGroups")
	@ApiOperation(value = "13. POST UnscribeTopicsInGroups", notes = "POST UnscribeTopicsInGroups [param: group id list]")
	public CommonResult UnscribeTopicsInGroups(HttpServletRequest request, @RequestBody List<String> grpIdList) {
		log.info(" 1. Device --> selectUnscribeTopics API ");
		List<String> resultList = deviceService.selectUnscribeTopicsOfGroup(grpIdList);
		return responseService.getListResult(resultList);
	}
	
	//post : 1개의 device에 대해 unscribe 대상이 되는지? 
	@PostMapping(value="/api/v1/UnscribeTopicsInDevices")
	@ApiOperation(value = "14. POST UnscribeTopicsInDevices", notes = "POST UnscribeTopicsInDevices [param: device_unique_id id list]")
	public CommonResult UnscribeTopicsInDevices(HttpServletRequest request, @RequestBody List<String> devUniqueIdList) {
		log.info(" 1. Device --> selectUnscribeTopics for device API ");
		List<String> resultList = deviceService.selectUnscribeTopicOfDevice(devUniqueIdList);
		return responseService.getListResult(resultList);
	}
	
}
