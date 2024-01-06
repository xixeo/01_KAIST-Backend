package kr.co.igns.iotMonitoring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.influxdb.dto.QueryResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import kr.co.igns.framework.config.influxDb.InfluxConnector;
import kr.co.igns.framework.config.influxDb.InfluxDBUtil;
import kr.co.igns.framework.config.influxDb.InfluxProperty;
import kr.co.igns.framework.utils.type.DateUtils;
import kr.co.igns.iotMonitoring.model.DashboardLastHistoryDTO;
import kr.co.igns.iotMonitoring.model.DeviceRecDataDTO;
import kr.co.igns.iotMonitoring.model.DeviceStatusByTime;
import kr.co.igns.iotMonitoring.model.DeviceVO;
import kr.co.igns.iotMonitoring.model.InfuxDBQueryDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfluxdbService {

	private final DateUtils dateUtils;
	private final DeviceService deviceService;
	private final InfluxConnector influxConnector;
	private final InfluxDBUtil influxDBUtil;
	private final InfluxProperty influxProperty;
	
	public JSONArray getDeviceRowDataByTime(DeviceRecDataDTO dto) {
		
		List<DeviceVO> devices = null;
		JSONObject jsonResult = new JSONObject();
		JSONArray arrayHist = new JSONArray();
		
		//현재시간
		String strNowDate = dateUtils.getNowDate("yyyy-MM-dd'T'HH:mm:ss");

		//디바이스 목록 조회		
		if(dto.getGroupId().equals("") == true ||
	       dto.getGroupId() == null)
		{		
			if(dto.getDeviceId().equals("")== true ||
				    dto.getDeviceId() == null)
			     devices = deviceService.selectAllDevices();		
			else
				 devices = deviceService.selectDevices(dto.getDeviceId());	
		} 
		else
		{
			if(dto.getDeviceId().equals("")== true ||
			    dto.getDeviceId() == null)
			{
				devices  = deviceService.selectGroupDevices(dto.getGroupId());
			}
			else
			{
				DeviceVO deviceVo = new DeviceVO();
				deviceVo.setDeviceGrpId(dto.getGroupId().toUpperCase());
				deviceVo.setDeviceId(dto.getDeviceId().toUpperCase());
				devices = deviceService.selectDevice(deviceVo);
			}
		}
		
		
		Map<String, DeviceVO> devMap = new HashMap<>();
		for(DeviceVO dev : devices )
		{
			devMap.put(dev.getDeviceUniqueId(), dev);

		}
		
		//deviceList = deviceStr.split(",");
		
		InfuxDBQueryDTO infuxDBQueryDTO = new InfuxDBQueryDTO();
		infuxDBQueryDTO.setSelectData("*");
		infuxDBQueryDTO.setOrderByData(" DESC");
		infuxDBQueryDTO.setStartDt(dto.getStartDt());
		infuxDBQueryDTO.setEndDt(dto.getEndDt());
		infuxDBQueryDTO.setLimitCnt(dto.getLimitCnt());
		List<Map<String, Object>> result = getDeviceDataInInfluxDB(infuxDBQueryDTO, devMap);
		
		
		//정리된 데이터 넣기
		for (Map<String, Object> map : result) {
			
			String[] mKeys = map.keySet().toArray(new String[map.size()]);
			JSONObject jsonHist = new JSONObject();
			
			String UniqueId = "";
			for (String key : mKeys) 
			{	
				if(key.equals("device_id"))
				{
					UniqueId = map.get(key).toString();
					jsonHist.put("device_unique_id", map.get(key));
				} 
				else
				{		
			        jsonHist.put( key, map.get(key));
				}
			}
			//System.out.println("UniqueId : " + UniqueId);
			jsonHist.put("device_id", devMap.get(UniqueId).getDeviceId());
			jsonHist.put("device_nm", devMap.get(UniqueId).getDeviceNm());
			jsonHist.put("device_grp_id", devMap.get(UniqueId).getDeviceGrpId());
			jsonHist.put("device_grp_nm", devMap.get(UniqueId).getDeviceGrpNm());
			jsonHist.put("running", devMap.get(UniqueId).getDeviceGrpNm());
			
			arrayHist.add(jsonHist);
		}
		
		jsonResult.put("history", arrayHist);
		
		JSONArray arrayResult = new JSONArray();
		arrayResult.add(jsonResult);
		return arrayHist;
	}
	
	

	public List<Map<String, Object>> getDevicesStatusByTime() {
		
		List<DeviceVO> devices = null;
		JSONArray arrayHist = new JSONArray();
		JSONObject jsonResult = new JSONObject();
		
		devices = deviceService.selectAllDevices();	
		
		Map<String, DeviceVO> devMap = new HashMap<>();
		for(DeviceVO dev : devices )
		{
			if(dev.getGrpDashUseYN().toUpperCase().equals("Y")== true && 
					dev.getDashUseYN().toUpperCase().equals("Y") == true) //
			   devMap.put(dev.getDeviceUniqueId(), dev);

		}
		
		List<Map<String, Object>> result  = getDeviceStatusByTimeInInfluxDB(devMap);
		List<Map<String, Object>> result2 = getDataCountStatusByTime(devMap);
		List<Map<String, Object>> finalResult = new ArrayList<Map<String, Object>>();
	
//		System.out.println("윤주=============1==========================================");
//		System.out.println(result);
//		System.out.println(result2);
        //merge list

		String key = "time";
		for(Map<String, Object> map : result)
		{   			
            String time = map.get(key).toString();
			Optional<Map<String, Object>> totalCntMap =  result2.stream().filter(x->x.get(key).equals(time) == true).findAny();
//			System.out.println(totalCntMap);
			Object stotalCount = totalCntMap.isPresent()==true? totalCntMap.get().get("totalCount"):0.0;
			
			
			Map<String, Object> statusMap = new HashMap<String, Object>();
			statusMap.put("time", time);
			statusMap.put("count", map.get("count")== null? 0.0 :map.get("count"));
			statusMap.put("totalCount", stotalCount);
			finalResult.add(statusMap);
		}
		
//		System.out.println("윤주=======================================================");
	
		
//		JSONArray arrayResult = new JSONArray();
//		arrayResult.add(result);
//		arrayResult.add(result2);
		return finalResult;
	}

	public JSONArray getDeviceLastStatus(DashboardLastHistoryDTO dto, boolean bOnlyBigadong) {
		//현재시간
				String strNowDate = dateUtils.getNowDate("yyyy-MM-dd'T'HH:mm:ss");
				
				List<DeviceVO> devices = null;
				JSONArray arrayHist = new JSONArray();
				JSONObject jsonResult = new JSONObject();
				
				DeviceVO oParam = new DeviceVO();
				oParam.setDeviceGrpId(dto.getDevGrpID().toUpperCase());
				oParam.setDeviceId(dto.getDevID().toUpperCase());
				devices = deviceService.selectDevicebyCond(oParam);	

				Map<String, DeviceVO> devMap = new HashMap<>();
				for(DeviceVO dev : devices )
				{
				    	devMap.put(dev.getDeviceUniqueId(), dev);

				}
				
				List<Map<String, Object>> result  = getDeviceStatusInInfluxDB(devMap);
				List<Map<String, Object>> result2 = getDataTimeCountByDevice(devMap);
							
				//정리된 데이터 넣기
				for (Map<String, Object> map : result) {
					
					String[] mKeys = map.keySet().toArray(new String[map.size()]);
					JSONObject jsonHist = new JSONObject();
					
					String UniqueId = "";
					Object oCountPerMinute = 0.0;
					String sCountMeasureTime="";
					boolean bRunning = true;
					for (String key : mKeys) 
					{
						if(key.equals("device_id"))
						{					
							UniqueId = map.get(key).toString();
							//분당 데이타 수신 수(디바이스별) 
							Optional<Map<String, Object>> CountPerMinMap  = result2.stream().filter(x->x.get(key).equals(map.get(key).toString()) == true).findAny();
							oCountPerMinute = CountPerMinMap.isPresent() == true? CountPerMinMap.get().get("count"):0.0;
							sCountMeasureTime= CountPerMinMap.isPresent() == true? CountPerMinMap.get().get("time").toString():"";
							jsonHist.put("device_unique_id", map.get(key));
						} 
						else
						{		
					        jsonHist.put( key, map.get(key));
						}

						
						//가동 상태 확인
						if(key.equals("time"))
						{				
							 String time = map.get(key).toString(); 
							 System.out.println("time: " + time.substring(0,19));
							 System.out.println("strNowDate : " + strNowDate);

							 //default time out = 60
							 long timeout = devMap.get(UniqueId).getDeviceTimeout() == 0? 60: devMap.get(UniqueId).getDeviceTimeout();
							 long timeDiff = dateUtils.getTimeDifference(strNowDate, time.substring(0,19)); //minutes

					          if(timeDiff > timeout)
						         bRunning = false;

						         System.out.println(timeDiff + ">" + timeout);
						         System.out.println("bRunning : " + bRunning);	
						}
						
					}
					
					jsonHist.put("device_id", devMap.get(UniqueId).getDeviceId());
					jsonHist.put("device_nm", devMap.get(UniqueId).getDeviceNm());
					jsonHist.put("device_type", devMap.get(UniqueId).getDeviceType());
					jsonHist.put("device_grp_id", devMap.get(UniqueId).getDeviceGrpId());
					jsonHist.put("device_grp_nm", devMap.get(UniqueId).getDeviceGrpNm());
					jsonHist.put("countPerMinute", oCountPerMinute);
					jsonHist.put("countMaesureTime", sCountMeasureTime);
					jsonHist.put("dash_use_yn", devMap.get(UniqueId).getDashUseYN()!= null && devMap.get(UniqueId).getDashUseYN().toUpperCase().equals("Y") == true?true:false);
					jsonHist.put("running", bRunning);

					if((bOnlyBigadong == true && bRunning == false) || bOnlyBigadong == false)
					   arrayHist.add(jsonHist);
				}
				

//				 System.out.println("윤주 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//				 System.out.println(arrayHist);
				/*jsonResult.put("result", arrayHist);
				
				JSONArray arrayResult = new JSONArray();
				arrayResult.add(jsonResult);*/
				return arrayHist;
	}

	
	
	public JSONArray getDeviceStatusForDash(boolean bOnlyBigadong) {
		
		//현재시간
		String strNowDate = dateUtils.getNowDate("yyyy-MM-dd'T'HH:mm:ss");
		
		List<DeviceVO> devices = null;
		JSONArray arrayHist = new JSONArray();
		JSONObject jsonResult = new JSONObject();
		
		devices = deviceService.selectAllDevices();	
				
		Map<String, DeviceVO> devMap = new HashMap<>();
		for(DeviceVO dev : devices )
		{
			if(dev.getGrpDashUseYN().toUpperCase().equals("Y")== true&& 
					dev.getDashUseYN().toUpperCase().equals("Y") == true) //
			    devMap.put(dev.getDeviceUniqueId(), dev);

		}
		
		List<Map<String, Object>> result  = getDeviceStatusInInfluxDB(devMap);
		
		
		//정리된 데이터 넣기
		for (Map<String, Object> map : result) {
			
			String[] mKeys = map.keySet().toArray(new String[map.size()]);
			JSONObject jsonHist = new JSONObject();
			
			String UniqueId = "";
			boolean bRunning = true;
			for (String key : mKeys) 
			{
				if(key.equals("device_id"))
				{
					
					UniqueId = map.get(key).toString();
					jsonHist.put("device_unique_id", map.get(key));
				} 
				else
				{		
			        jsonHist.put( key, map.get(key));
				}
				
				
				//가동 상태 확인
				if(key.equals("time"))
				{				
					 String time = map.get(key).toString(); 
					 System.out.println("time: " + time.substring(0,19));
					 System.out.println("strNowDate : " + strNowDate);

					 //default time out = 60
					 long timeout = devMap.get(UniqueId).getDeviceTimeout() == 0? 60: devMap.get(UniqueId).getDeviceTimeout();
					 long timeDiff = dateUtils.getTimeDifference(strNowDate, time.substring(0,19)); //minutes

			          if(timeDiff > timeout)
				         bRunning = false;

				         System.out.println(timeDiff + ">" + timeout);
				         System.out.println("bRunning : " + bRunning);	
				}
				
			}
			
			jsonHist.put("device_id", devMap.get(UniqueId).getDeviceId());
			jsonHist.put("device_nm", devMap.get(UniqueId).getDeviceNm());
			jsonHist.put("device_type", devMap.get(UniqueId).getDeviceType());
			jsonHist.put("device_grp_id", devMap.get(UniqueId).getDeviceGrpId());
			jsonHist.put("device_grp_nm", devMap.get(UniqueId).getDeviceGrpNm());
			jsonHist.put("dash_use_yn", devMap.get(UniqueId).getDashUseYN()!= null && devMap.get(UniqueId).getDashUseYN().toUpperCase().equals("Y") == true?true:false);
			jsonHist.put("running", bRunning);

			if((bOnlyBigadong == true && bRunning == false) || bOnlyBigadong == false)
			   arrayHist.add(jsonHist);
		}
		

		/*jsonResult.put("result", arrayHist);
		
		JSONArray arrayResult = new JSONArray();
		arrayResult.add(jsonResult);*/
		return arrayHist;
	}


	
	
	
	//---------------------------------------------------------------------------------------------------------------------------------
	
	
	
	
	private String getInfluxQuery(InfuxDBQueryDTO dto, String strType, String influxTable, String selectData, Map<String, DeviceVO> devMap ) {
		//influxDB 기본정보
		String dataBase = influxProperty.getDatabase();
		String rPolicy = influxProperty.getRPolicy();
				
		String query =  "SELECT " + selectData;  
		   query += " FROM " + dataBase + "." + rPolicy + "." + influxTable; 
		   query += " WHERE 1=1";
		   
		  String[] gs1keys = {};
		  String gs1keyStr = "";
		  String[] mKeys = devMap.keySet().toArray(new String[devMap.size()]);
		  for(String key: mKeys)
		  {
			  if(gs1keyStr.equals("") ==false)
				  gs1keyStr +=",";
			  
			  gs1keyStr += key;
		  }
		
		  gs1keys = gs1keyStr.split(",");
		  
		  System.out.println("gs1keys : " + gs1keyStr);
		  
		 //device ID가 있으면 카메라 아이디에 대한 데이터 조회,, 아이디가 없으면 도메인코드, 존코드에 해당하는 카메라 전체
		   if(!gs1keys[0].equals("ALL") && !gs1keys[0].equals("NULL")) {
			   for (int i = 0; i < gs1keys.length; i++) {
				   if(i == 0) {
					   query += " AND (device_id = '" + gs1keys[i] + "'";
				   } else {
					   query += " OR device_id = '" + gs1keys[i] + "'";
				   }
				   if(i == gs1keys.length - 1) {
					   query += ")";
				   }
			   } 
		   }
		   
		   System.out.println("dto.getStartDt() : " +dto.getStartDt()) ;
		   //시작일자
		   if(dto.getStartDt() != null && !dto.getStartDt().equals("")) {
			   query += " AND time > '" + dto.getStartDt() + "'";
		   }

		   //종료일자
		   if(dto.getEndDt() != null && !dto.getEndDt().equals("")) {
			   if(dto.getEndDt().length() > 10) {
				   query += " AND time < '" + dto.getEndDt() + "'";
			   } else {
				   query += " AND time < '" + dto.getEndDt() + "' +1d ";
			   }
		   }
		/*
		 * //기준일자 if(dto.getBaseDt() != null && !dto.getBaseDt().equals("")) { query +=
		 * " AND time < '" + dto.getBaseDt() + "'"; }
		 */
		   //추가조건
		   if(dto.getAddWhere() != null && !dto.getAddWhere().equals("")) {
			   query += " AND " + dto.getAddWhere();
		   }
		   //groupBy Data
		   if(dto.getGroupByData() != null && !dto.getGroupByData().equals("")) {
			   query += " GROUP BY " + dto.getGroupByData();
		   }
		   //fill Data
		   if(dto.getFillData() != null && !dto.getFillData().equals("")) {
			   query += " FILL(" + dto.getFillData() + ") ";
		   }
		   //orderBy Data
		   if(dto.getOrderByData() != null && !dto.getOrderByData().equals("")) {
			   query += " ORDER BY " + dto.getOrderByData();
		   }
		/*
		 * if(dto.getBaseDt() != null && !dto.getBaseDt().equals("")) { query +=
		 * " limit 1"; }
		 */
		   
		   if(dto.getLimitCnt() != 0)
			   query += " limit " + dto.getLimitCnt();
		   
		   if(dto.getAddWhereLast() != null && !dto.getAddWhereLast().equals("")) {
			   query += " " + dto.getAddWhereLast();
		   }
		   query += " tz('Asia/Seoul')";//리눅스 timezonectl 설정 후 사용
		   System.out.println("@ dto : " + dto);
		   System.out.println("@ query : " + query);
		   
		   return query;
	}
	
	private List<Map<String, Object>> getDeviceDataInInfluxDB(InfuxDBQueryDTO dto, Map<String, DeviceVO> devMap) {
		
		//influxDB 기본정보
		String dataBase = influxProperty.getDatabase();
		String rPolicy = influxProperty.getRPolicy();
		String influxTable = "KAIST_ITC_HIST";
						
		//오늘날짜
		

		// 데이터 대량조회를 방지하기위해, 날짜데이터가 없는경우 일주일 데이터만 조회
		if ((dto.getStartDt() == null || dto.getStartDt().equals("")) && 
			(dto.getEndDt() == null || dto.getEndDt().equals(""))) {
			String strEnd = dateUtils.getNowDate("yyyy-MM-dd'T'HH:mm:ss+09:00");
			String strStart = dateUtils.AddDate(strEnd, 0, 0, -1);
			dto.setStartDt(strStart);
			dto.setEndDt(strEnd);
		}
		else if((dto.getStartDt() == null || dto.getStartDt().equals(""))) //시작일시가 날짜 데이타 없는 경우
		{
			String strStart = dateUtils.AddDate(dto.getEndDt(), 0, 0, -1);
			dto.setStartDt(strStart);
		}
		else if((dto.getEndDt() == null || dto.getEndDt().equals("")))//종료일자 날짜 데이타가 없는 경우
		{
			String strEnd = dateUtils.AddDate(dto.getStartDt(), 0, 0, +1);
			dto.setEndDt(strEnd);
		}
		
		System.out.println(dto.getStartDt());
		System.out.println(dto.getEndDt());
		
		//조회 리스트
		String selectData = "*";
		if(dto.getSelectData() != null && !dto.getSelectData().equals("")) {
			selectData = dto.getSelectData() ;
		} 

		
		//influxDB 조회쿼리
		String query = getInfluxQuery(dto, "device", influxTable, selectData, devMap);				   
		QueryResult result = influxConnector.getQuery(query);
		List<Map<String, Object>> resultMap = influxDBUtil.convertType(result); 
		
		//System.out.println("@ resultMap : " + resultMap );
		
		return resultMap;
	}


	//분당 수신데이타가 1건이상인 디바이스의 수 조회
	private List<Map<String, Object>> getDeviceStatusByTimeInInfluxDB(Map<String, DeviceVO> devMap) {
		
		//influxDB 기본정보
		String dataBase = influxProperty.getDatabase();
		String rPolicy = influxProperty.getRPolicy();
		String influxTable = "KAIST_ITC_HIST";
		
		//influxDB 기본정보

				String query =  "SELECT count(device_id) FROM (SELECT count(jsondata) as cnt";  
				   query += " FROM " + dataBase + "." + rPolicy + "." + influxTable; 
				   query += " WHERE 1=1";
			   
				  String[] gs1keys = {};
				  String gs1keyStr = "";
				  String[] mKeys = devMap.keySet().toArray(new String[devMap.size()]);
				  for(String key: mKeys)
				  {
					  if(gs1keyStr.equals("") == false)
						  gs1keyStr +=",";
					  
					  gs1keyStr += key;
				  }
				
				  gs1keys = gs1keyStr.split(",");
				  
				  System.out.println("gs1keys : " + gs1keyStr);
				  
				 //device ID가 있으면 카메라 아이디에 대한 데이터 조회,, 아이디가 없으면 도메인코드, 존코드에 해당하는 카메라 전체
				   if(!gs1keys[0].equals("ALL") && !gs1keys[0].equals("NULL")) {
					   for (int i = 0; i < gs1keys.length; i++) {
						   if(i == 0) {
							   query += " AND (device_id = '" + gs1keys[i] + "'";
						   } else {
							   query += " OR device_id = '" + gs1keys[i] + "'";
						   }
						   if(i == gs1keys.length - 1) {
							   query += ")";
						   }
					   } 
				   }
				   query += " and time > now() -30m";
				   query += " GROUP BY time(1m), device_id ) where cnt > 0 group by time(1m)";
				   query += " tz('Asia/Seoul')";//리눅스 timezonectl 설정 후 사용
				   System.out.println("@ query : " + query);
				   
					QueryResult result = influxConnector.getQuery(query);
					List<Map<String, Object>> resultMap = influxDBUtil.convertType(result); 
					
					//System.out.println("@ resultMap : " + resultMap );
					return resultMap;
	
	}
	
	//분당 수신되는 총 데이타의 수 
	private List<Map<String, Object>> getDataCountStatusByTime(Map<String, DeviceVO> devMap) {
		
		//influxDB 기본정보
		String dataBase = influxProperty.getDatabase();
		String rPolicy = influxProperty.getRPolicy();
		String influxTable = "KAIST_ITC_HIST";
		
		//influxDB 기본정보

				String query =  "SELECT count(jsondata) as totalCount";  
				   query += " FROM " + dataBase + "." + rPolicy + "." + influxTable; 
				   query += " WHERE 1=1";
			   
				  String[] gs1keys = {};
				  String gs1keyStr = "";
				  String[] mKeys = devMap.keySet().toArray(new String[devMap.size()]);
				  for(String key: mKeys)
				  {
					  if(gs1keyStr.equals("") == false)
						  gs1keyStr +=",";
					  
					  gs1keyStr += key;
				  }
				
				  gs1keys = gs1keyStr.split(",");
				  
				  System.out.println("gs1keys : " + gs1keyStr);
				  
				 //device ID가 있으면 카메라 아이디에 대한 데이터 조회,, 아이디가 없으면 도메인코드, 존코드에 해당하는 카메라 전체
				   if(!gs1keys[0].equals("ALL") && !gs1keys[0].equals("NULL")) {
					   for (int i = 0; i < gs1keys.length; i++) {
						   if(i == 0) {
							   query += " AND (device_id = '" + gs1keys[i] + "'";
						   } else {
							   query += " OR device_id = '" + gs1keys[i] + "'";
						   }
						   if(i == gs1keys.length - 1) {
							   query += ")";
						   }
					   } 
				   }
				   query += " and time > now() -30m";
				   query += " GROUP BY time(1m)";
				   query += " tz('Asia/Seoul')";//리눅스 timezonectl 설정 후 사용
				   System.out.println("@ query : " + query);
				   
					QueryResult result = influxConnector.getQuery(query);
					List<Map<String, Object>> resultMap = influxDBUtil.convertType(result); 
					
					//System.out.println("@ resultMap : " + resultMap );
					return resultMap;
	
	}

	//디바이스별 분당 데이타 수 조회
	private List<Map<String, Object>> getDataTimeCountByDevice(Map<String, DeviceVO> devMap) {
		
		//influxDB 기본정보
		String dataBase = influxProperty.getDatabase();
		String rPolicy = influxProperty.getRPolicy();
		String influxTable = "KAIST_ITC_HIST";
		
		//influxDB 기본정보

				String query =  "SELECT device_id, last(totalCount) as count from(SELECT count(jsondata) as totalCount";  
				   query += " FROM " + dataBase + "." + rPolicy + "." + influxTable; 
				   query += " WHERE 1=1";
			   
				  String[] gs1keys = {};
				  String gs1keyStr = "";
				  String[] mKeys = devMap.keySet().toArray(new String[devMap.size()]);
				  for(String key: mKeys)
				  {
					  if(gs1keyStr.equals("") == false)
						  gs1keyStr +=",";
					  
					  gs1keyStr += key;
				  }
				
				  gs1keys = gs1keyStr.split(",");
				  
				  System.out.println("gs1keys : " + gs1keyStr);
				  
				 //device ID가 있으면 카메라 아이디에 대한 데이터 조회,, 아이디가 없으면 도메인코드, 존코드에 해당하는 카메라 전체
				   if(!gs1keys[0].equals("ALL") && !gs1keys[0].equals("NULL")) {
					   for (int i = 0; i < gs1keys.length; i++) {
						   if(i == 0) {
							   query += " AND (device_id = '" + gs1keys[i] + "'";
						   } else {
							   query += " OR device_id = '" + gs1keys[i] + "'";
						   }
						   if(i == gs1keys.length - 1) {
							   query += ")";
						   }
					   } 
				   }
				   query += " and time >= now() -59s";
				   query += " GROUP BY time(1m),device_id ) group by device_id";
				   query += " tz('Asia/Seoul')";//리눅스 timezonectl 설정 후 사용
				   System.out.println("@ query : " + query);
				   
					QueryResult result = influxConnector.getQuery(query);
					List<Map<String, Object>> resultMap = influxDBUtil.convertType(result); 
					
					//System.out.println("@ resultMap : " + resultMap );
					return resultMap;
	
	}
	

	private List<Map<String, Object>> getDeviceStatusInInfluxDB(Map<String, DeviceVO> devMap) {
		
		//influxDB 기본정보
		String dataBase = influxProperty.getDatabase();
		String rPolicy = influxProperty.getRPolicy();
		String influxTable = "KAIST_ITC_HIST";
		
		//influxDB 기본정보
       //measure_dtm -> message로 변경함
		String query =  "SELECT device_id,last(message), jsondata";  
		   query += " FROM " + dataBase + "." + rPolicy + "." + influxTable; 
		   query += " WHERE 1=1";
	   
		  String[] gs1keys = {};
		  String gs1keyStr = "";
		  String[] mKeys = devMap.keySet().toArray(new String[devMap.size()]);
		  for(String key: mKeys)
		  {
			  if(gs1keyStr.equals("") ==false)
				  gs1keyStr +=",";
			  
			  gs1keyStr += key;
		  }
		
		  gs1keys = gs1keyStr.split(",");
		  
		  System.out.println("gs1keys : " + gs1keyStr);
		  
		 //device ID가 있으면 카메라 아이디에 대한 데이터 조회,, 아이디가 없으면 도메인코드, 존코드에 해당하는 카메라 전체
		   if(!gs1keys[0].equals("ALL") && !gs1keys[0].equals("NULL")) {
			   for (int i = 0; i < gs1keys.length; i++) {
				   if(i == 0) {
					   query += " AND (device_id = '" + gs1keys[i] + "'";
				   } else {
					   query += " OR device_id = '" + gs1keys[i] + "'";
				   }
				   if(i == gs1keys.length - 1) {
					   query += ")";
				   }
			   } 
		   }
		   query += " GROUP BY device_id";
		   query += " tz('Asia/Seoul')";//리눅스 timezonectl 설정 후 사용
		   System.out.println("@ query : " + query);
		   
			QueryResult result = influxConnector.getQuery(query);
			List<Map<String, Object>> resultMap = influxDBUtil.convertType(result); 
			
			//System.out.println("@ resultMap : " + resultMap );
			return resultMap;
		
	}










}
