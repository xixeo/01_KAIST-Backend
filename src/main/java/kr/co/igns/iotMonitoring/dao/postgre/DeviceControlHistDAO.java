package kr.co.igns.iotMonitoring.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.iotMonitoring.model.DeviceControlHistoryDTO;
import kr.co.igns.iotMonitoring.model.DeviceControlHistoryVO;

@Mapper
@Repository
public interface DeviceControlHistDAO {

	List<DeviceControlHistoryVO> getDevicePublishHistory(DeviceControlHistoryDTO vo);
	
}
