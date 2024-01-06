package kr.co.igns.iotMonitoring.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.sample.model.SampleDTO;
import kr.co.igns.business.sample.model.SampleVO;
import kr.co.igns.iotMonitoring.model.DeviceGroupVO;
import kr.co.igns.iotMonitoring.model.DeviceVO;

@Mapper
@Repository
public interface DeviceDAO {

	//조회
	List<DeviceGroupVO> selectDeviceGroup(String deviceGroup);

	List<DeviceVO> selectDevice(DeviceVO vo);
	
	List<DeviceVO> selectDevicebyCond(DeviceVO oParam);

	List<DeviceVO> selectGroupDevices(String group);

	List<DeviceGroupVO> selectAllGroups();

	List<DeviceVO> selectAllDevices();   

	// 그룹 추가/변경/삭제
	int InsertGroup(DeviceGroupVO vo);

	int UpdateGroup(DeviceGroupVO vo);

	void DeleteGroups(DeviceGroupVO vo);

	// 디바이스 추가/변경/삭제
	int InsertDevice(DeviceVO vo);

	int UpdateDevice(DeviceVO vo);

	void DeleteDevices(DeviceVO vo);

	List<DeviceVO> selectDeviceByUniqueID(String UniqueID);

	List<DeviceVO> selectDevices(String deviceId);

	void DeleteAllDevicesInGroup(String deviceGrpId);

	List<String> selectUnscribeTopicsInGroup(List<String> groupList);

	List<String> selectUnscribeTopicOfDevice(List<String> dev_unique_id);

	

}
