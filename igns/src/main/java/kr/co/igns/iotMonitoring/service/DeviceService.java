package kr.co.igns.iotMonitoring.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.iotMonitoring.dao.postgre.DeviceDAO;
import kr.co.igns.iotMonitoring.model.DeviceGroupVO;
import kr.co.igns.iotMonitoring.model.DeviceVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService {

	private final DeviceDAO deviceDao;

	//주어진 그룹의 정보 조회
	public List<DeviceGroupVO> selectGroup(String deviceGroup) {

		return deviceDao.selectDeviceGroup(deviceGroup.toUpperCase());
	}

	//주어진 그룹, 디바이스에 의한 디바이스 정보 조회
	public List<DeviceVO> selectDevice(DeviceVO vo) {

		return deviceDao.selectDevice(vo);
	}
	
	public List<DeviceVO> selectDevices(String deviceId)
	{
		return deviceDao.selectDevices(deviceId.toUpperCase());
	}
	
	//주어진 unique아이디에 의한 디바이스 정보 조회
	public List<DeviceVO> selectDeviceByUniqueID(String UniqueID) {
		return deviceDao.selectDeviceByUniqueID(UniqueID.toUpperCase());
	}

	//주어진 그룹의 디바이스 리스트 조회
	public List<DeviceVO> selectGroupDevices(String group) {

		return deviceDao.selectGroupDevices(group);
	}

	public List<DeviceGroupVO> selectAllGroups() {

		return deviceDao.selectAllGroups();
   }

	public List<DeviceVO> selectAllDevices() {

		return deviceDao.selectAllDevices();
	}

	public int InsertGroup(DeviceGroupVO vo, boolean is__created__) {
		
		System.out.println(vo);
		if(is__created__) {
			int createCount = deviceDao.InsertGroup(vo);
			System.out.println(" #insert Count : " + createCount);
			
			//강제에러발생 - 트랜젝션 테스트
			/*
			 * if (vo.getErrCd().equals("1")) { int a = 10/0; }
			 */
			
			/*
			 * int createCount2 = deviceDao.InsertGroup(vo);
			 * System.out.println(" #insert Count2 : " + createCount2);
			 */
			
		} else {
			int updateCount = deviceDao.UpdateGroup(vo);
			System.out.println(" #update Count : " + updateCount);
		}
		
       return 0;
	}

	public List<DeviceVO> selectDevicesInGroups(List<DeviceGroupVO> voList) {
		
		List<DeviceVO> totalDevices = new ArrayList<DeviceVO>();
		
		for(DeviceGroupVO vo : voList) {
			List<DeviceVO> deviceList =  	deviceDao.selectGroupDevices(vo.getDeviceGrpId().toUpperCase());
			totalDevices.addAll(deviceList);
		}
		
		return totalDevices;
	}

	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void DeleteGroups(List<DeviceGroupVO> voList) {
		
		for(DeviceGroupVO vo : voList) {
			vo.setDeviceGrpId(vo.getDeviceGrpId().toUpperCase());
			deviceDao.DeleteGroups(vo);
			//포함된 디바이스들 전부 삭제
			deviceDao.DeleteAllDevicesInGroup(vo.getDeviceGrpId().toUpperCase());
		}
		
	}

	public int InsertDevice(DeviceVO vo, boolean is__created__) {
		System.out.println(vo);

		if(is__created__) {
			int createCount = deviceDao.InsertDevice(vo);
			System.out.println(" #insert Count : " + createCount);
			
			//강제에러발생 - 트랜젝션 테스트
			/*
			 * if (vo.getErrCd().equals("1")) { int a = 10/0; }
			 */
			
			/*
			 * int createCount2 = deviceDao.InsertGroup(vo);
			 * System.out.println(" #insert Count2 : " + createCount2);
			 */
			
		} else {
			int updateCount = deviceDao.UpdateDevice(vo);
			System.out.println(" #update Count : " + updateCount);
		}
		
       return 0;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void DeleteDevices(List<DeviceVO> voList) {
		for(DeviceVO vo : voList) {
			vo.setDeviceId(vo.getDeviceId().toUpperCase());
			vo.setDeviceUniqueId(vo.getDeviceUniqueId().toUpperCase());
			deviceDao.DeleteDevices(vo);
		}
		
		
	}

	public List<DeviceVO> selectDevicebyCond(DeviceVO oParam) {
		return deviceDao.selectDevicebyCond(oParam);
	}

	public List<String> selectUnscribeTopicsOfGroup(List<String> voList) {
		
		List<String> groupList = new ArrayList<String>();
		
		for(String groupId : voList) {
			groupList.add(groupId.toUpperCase());		
		}
		
		return deviceDao.selectUnscribeTopicsInGroup(groupList);
	}

	public List<String> selectUnscribeTopicOfDevice(List<String> voList) {
		
		List<String> devUniqueIdList = new ArrayList<String>();
		
		for(String uniqueId : voList) {
			devUniqueIdList.add(uniqueId.toUpperCase());
		}
		
		return deviceDao.selectUnscribeTopicOfDevice(devUniqueIdList);
	}



	
}
	
