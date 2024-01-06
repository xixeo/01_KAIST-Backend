package kr.co.igns.iotMonitoring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.iotMonitoring.model.DashboardLastHistoryDTO;
import kr.co.igns.iotMonitoring.model.DeviceGroupVO;
import kr.co.igns.iotMonitoring.model.DeviceStatusByTime;
import kr.co.igns.iotMonitoring.model.DeviceStatusVO;
import kr.co.igns.iotMonitoring.model.DeviceVO;
import kr.co.igns.iotMonitoring.model.GroupDeviceStatusVO;
import kr.co.igns.iotMonitoring.service.DeviceService;
import kr.co.igns.iotMonitoring.service.InfluxdbService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"1.3 dashboard"})
@RestController
@RequiredArgsConstructor
public class DashboardController {
	
	private final InfluxdbService influxdbService;
	private final DeviceService deviceService;
	private final ResponseService responseService;
	private final ExceptionAdvice exceptionAdvice;
	private static final Logger log = LogManager.getLogger("com.project");
	
	
	//#################### [디바이스별 최종 가동정보] ###########################
	  @ApiOperation(value = "디바이스별 최종 가동정보(for dashboard)", notes = "전체 디바이스의 최종 수신이력 출력(대시보드용)") 
	  @PostMapping(value = "/api/v1/getDeviceStatusForDashboard") 
	  public CommonResult getDeviceStatusForDashboard(@RequestBody DashboardLastHistoryDTO vo){
	  
		  Map<String, List<Object>> gadongDevices = new HashMap<String, List<Object>>();
		  List<DeviceVO> devices = deviceService.selectAllDevices();	
		  List<Map<String, Object>> result = influxdbService.getDeviceLastStatus(vo, false); 
		  List<Map<String, Object>> finalResult = new ArrayList<Map<String,Object>>();
		 
		  //가동 정보 로딩 
		  for(Map<String,Object> map : result)
		  {
			  if(map.get("running").equals(true))
			  {
				  List<Object> subItem = new ArrayList<Object>();
				  subItem.add(map.get("time").toString());
				  subItem.add(map.get("countPerMinute"));
				  subItem.add(map.get("countMaesureTime"));
				  gadongDevices.put(map.get("device_unique_id").toString(), subItem);
			  }
		  }
		  

		  for(DeviceVO dev :devices)
		  {
			  if(dev.getGrpDashUseYN().toUpperCase().equals("Y")== true &&
					  dev.getDashUseYN().toUpperCase().equals("Y"))
			  {
				  boolean bRunning = false;
				  String receivedTime = "";
				  Object countPerMin = 0.0;
				  String sCountMaesureTime ="";
				  if(gadongDevices.keySet().contains(dev.getDeviceUniqueId()))
				  {
					  bRunning = true;
					  receivedTime = gadongDevices.get(dev.getDeviceUniqueId()).get(0).toString();
					  countPerMin = gadongDevices.get(dev.getDeviceUniqueId()).get(1);
					  sCountMaesureTime= gadongDevices.get(dev.getDeviceUniqueId()).get(2).toString();
				  }
				  
				  Map<String,Object> item = new HashMap<String,Object>();
				  item.put("device_id", dev.getDeviceId());
				  item.put("device_nm", dev.getDeviceNm());
				  item.put("device_unique_id", dev.getDeviceUniqueId());
				  item.put("device_grp_id", dev.getDeviceGrpId());
				  item.put("device_grp_nm", dev.getDeviceGrpNm());
				  item.put("time" , receivedTime);
				  item.put("deviceTimeout", dev.getDeviceTimeout());
				  item.put("countPerMinute", countPerMin);
				  item.put("countMaesureTime", sCountMaesureTime);
				  //item.put("dashboardYN" , dev.getDashUseYN().toUpperCase());
				  item.put("running", bRunning);
				  
				  finalResult.add(item);
			  }
		  }
		  
		  
		  System.out.println(gadongDevices);
		  //System.out.println(finalResult);
		  
		  return responseService.getListResult(finalResult); 
	  }
	  
	  //#################### [디바이스별 최종 가동정보] ###########################
	  @ApiOperation(value = "디바이스별 최종 가동정보", notes = "전체 디바이스의 최종 수신이력 출력") 
	  @PostMapping(value = "/api/v1/getDeviceStatus") 
	  public CommonResult getDeviceStatus(@RequestBody DashboardLastHistoryDTO vo){
	  
		  List<Map<String, Object>> result = influxdbService.getDeviceLastStatus(vo, false); 
		  
		  return responseService.getListResult(result); 
	  }
	  
	//#################### [비가동 디바이스 목록 조회] ###########################
	  @ApiOperation(value = "비가동 디바이스 목록 조회", notes = "") 
	  @PostMapping(value = "/api/v1/getBigadongDevices") 
	  public CommonResult getBigadongDevices(){
		  
		  List<DeviceStatusVO> begadongList = new ArrayList<DeviceStatusVO>();
		  List<DeviceVO> devices = deviceService.selectAllDevices();

		  JSONArray result = influxdbService.getDeviceStatusForDash(false); 
		  JSONArray devicesInInfluxdb = (JSONArray)result;
 		    
		  List<String> gadongInfos = new ArrayList<String>();
		  for(int i= 0; i < devicesInInfluxdb.size() ; i++)
		  {
			   JSONObject item = (JSONObject)devicesInInfluxdb.get(i);
			    //System.out.println(item);
			    if(item.get("running").equals(true)) //가동일 경우 
			    {
			    	gadongInfos.add(item.get("device_unique_id").toString());
			    }
		   }
		  
		  //System.out.println(gadongInfos);
		  
		  for(DeviceVO dev : devices)
		  {
			  if(dev.getDashUseYN().toUpperCase().equals("Y") == false &&
					  dev.getGrpDashUseYN().toUpperCase().equals("Y"))
				  continue;
			  
			  if(gadongInfos.contains(dev.getDeviceUniqueId()) == false)
			  {
			      DeviceStatusVO oDeviceStatusVO = new DeviceStatusVO();
			      oDeviceStatusVO.setDeviceId(dev.getDeviceId());
			      oDeviceStatusVO.setDeviceNm(dev.getDeviceNm());
			      oDeviceStatusVO.setUniqueID(dev.getDeviceUniqueId());
			      begadongList.add(oDeviceStatusVO);
			  }
		  }
		  
		  return responseService.getListResult(begadongList); 
	  
	  }
	  
	  
	  //#################### [시간대별 디바이스 가동 현황] ###########################
	  @ApiOperation(value = "시간대별 디바이스 가동 현황", notes = "") 
	  @PostMapping(value = "/api/v1/getDevicesStatusByTime") 
	  public CommonResult getDevicesStatusByTime(){
		  
		  List<Map<String, Object>> result = influxdbService.getDevicesStatusByTime(); 
		  //System.out.println(result);
		  return responseService.getListResult(result); 
	  
	  }
	  //#################### [디바이스 가동 현황] ###########################
	  @ApiOperation(value = "디바이스 가동 현황", notes = "총 디바이스 수, 가동중인 디바이스 수 출력") 
	  @PostMapping(value = "/api/v1/getAllDeviceGadongStatus") 
	  public CommonResult getAllDeviceGadongStatus()
	  {
		  //Map<String, String> oResultMap = new HashMap<>();
		  int totalCnt = 0;
		  int gadongCnt = 0;
		  int bigadongCnt = 0;
		  
		  //디바이스의 총 카운트 
		  totalCnt = deviceService.selectAllDevices().stream()
				  .filter(x->x.getGrpDashUseYN().toUpperCase().equals("Y") 
						  && x.getDashUseYN().toUpperCase().equals("Y"))
				  .collect(Collectors.toList()).size();//;		    
		  //System.out.println(totalCnt);
			  
		  JSONArray result = influxdbService.getDeviceStatusForDash(false); //전체 가동 정보 조회
		 
		  JSONArray devicesInInfluxdb = (JSONArray)result;
		   		    
		  for(int i= 0; i < devicesInInfluxdb.size() ; i++)
		  {
			   JSONObject item = (JSONObject)devicesInInfluxdb.get(i);
			    //System.out.println(item);
			    if(item.get("running").equals(true)) //가동일 경우 
			    {
			    	gadongCnt +=1;
			    }
		   }

		   
		  List<JSONObject> resultList = new ArrayList<JSONObject>();
		  
		  bigadongCnt = totalCnt - gadongCnt;
		  if(bigadongCnt < 0) bigadongCnt = 0;
		  
		  //가동 정보
		  JSONObject item1 = new JSONObject();
		  item1.put("subject", "가동");
		  item1.put("val", String.valueOf(gadongCnt));
		  resultList.add(item1);
		  
		  //비가동 정보
		  JSONObject item2 = new JSONObject();
		  item2.put("subject", "비가동");
		  item2.put("val", String.valueOf(bigadongCnt));
		  resultList.add(item2);	    
		  
		  //System.out.println(resultList);
		  return responseService.getListResult(resultList); 
	  
	  }
	  //#################### [그룹별 디바이스 가동 현황] ###########################
	  @ApiOperation(value = "그룹별 디바이스 가동 현황", notes = "") 
	  @PostMapping(value = "/api/v1/getDeviceGadongByGroup") 
	  public CommonResult getDeviceGadongByGroup()
	  {
		  List<GroupDeviceStatusVO> oTotalGroupList = new ArrayList<GroupDeviceStatusVO>();

		   List<DeviceGroupVO> groupInfoList = deviceService.selectAllGroups();  
		   
		   for(DeviceGroupVO group : groupInfoList)
		   {
			   if(group.getDashUseYN().toUpperCase().equals("Y") == true)
			   {			   
				    GroupDeviceStatusVO oGroupDeviceStatusVO = new GroupDeviceStatusVO();
			        oGroupDeviceStatusVO.setGroupId(group.getDeviceGrpId());
			        oGroupDeviceStatusVO.setGroupNm(group.getDeviceGrpNm());
			        oGroupDeviceStatusVO.setTotalDeviceCnt(group.getDevCnt());
			        oGroupDeviceStatusVO.setBigadongDeviceCnt(group.getDevCnt());
			        oTotalGroupList.add(oGroupDeviceStatusVO);
			   }
		   }
		   
		   //전체 디바이스  조회(influxdb 데이타내에서) -- 가동인 디바이스만 조회
		   JSONArray result = influxdbService.getDeviceStatusForDash(false); 
		 
		   JSONArray obj1 = (JSONArray)result;
		   
		   for(int i= 0; i < obj1.size() ; i++)
		   {
			    JSONObject item = (JSONObject)obj1.get(i);
			    System.out.println(item);
			    
			    GroupDeviceStatusVO vo = null;
			    int iTotalDevCnt = 0;
			    String sGroupID = item.get("device_grp_id").toString();
			    
			    //그룹내 전체 디바이스의 종료 조회 
			    DeviceGroupVO groupInfo =  groupInfoList.stream().filter(grp->grp.getDeviceGrpId().equals(sGroupID)).collect(Collectors.toList()).get(0);
                if(groupInfo!= null) iTotalDevCnt = groupInfo.getDevCntForDash();
                
                if(oTotalGroupList.stream().filter(x -> x.getGroupId().equals(sGroupID)).count() > 0 &&
                		item.get("running").equals(true))//가동상태이면 
			    {
			    	System.out.println("modify");
			    	vo = oTotalGroupList.stream().filter(x -> x.getGroupId().equals(sGroupID)).collect(Collectors.toList()).get(0);
			    	vo.setTotalDeviceCnt(iTotalDevCnt);
			    	vo.setGadongDeviceCnt(vo.getGadongDeviceCnt() + 1 );
			    	vo.setBigadongDeviceCnt(iTotalDevCnt -vo.getGadongDeviceCnt());
			    }
//			    else
//			    {
//			    	System.out.println("insert");
//			    	vo = new GroupDeviceStatusVO();
//			    	vo.setGroupId(item.get("device_grp_id").toString());
//			    	vo.setGroupNm(item.get("device_grp_nm").toString());
//			    	vo.setTotalDeviceCnt(iTotalDevCnt);
//                    vo.setGadongDeviceCnt(item.get("running").equals(true)?  1 : 0);
//			    	vo.setBigadongDeviceCnt(item.get("running").equals(false)? 1 : 0);
//			    	
//			    	oTotalGroupList.add(vo);
//			    }		    	    
		   }

		   //System.out.println(oTotalGroupList);  		  
		  return responseService.getListResult(oTotalGroupList); 
	  
	  }
}
