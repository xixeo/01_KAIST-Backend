package kr.co.igns.iotMonitoring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.igns.iotMonitoring.dao.postgre.DeviceControlHistDAO;
import kr.co.igns.iotMonitoring.dao.postgre.DeviceDAO;
import kr.co.igns.iotMonitoring.model.DeviceControlHistoryDTO;
import kr.co.igns.iotMonitoring.model.DeviceControlHistoryVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceControlHistService {
	
	private final DeviceControlHistDAO deviceControlHistDAO;

	public List<DeviceControlHistoryVO> getDevicePublishHistory(DeviceControlHistoryDTO vo) {
		return deviceControlHistDAO.getDevicePublishHistory(vo);
	}

}
