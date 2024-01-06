package kr.co.igns.iotMonitoring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.iotMonitoring.dao.postgre.DataDisplayFormatDAO;
import kr.co.igns.iotMonitoring.model.DataDispFormatSearchDTO;
import kr.co.igns.iotMonitoring.model.DataDispFormatVO;
import kr.co.igns.iotMonitoring.model.DeviceGroupVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataDisplayFormatService {

	private final DataDisplayFormatDAO dataDisplayFormatDAO;

	public List<DataDispFormatVO> selectAllDataDisplayFormat(DataDispFormatSearchDTO vo) {
		// TODO Auto-generated method stub
		vo.setDispGubn(vo.getDispGubn().toUpperCase());
		vo.setDispGubnCode(vo.getDispGubnCode().toUpperCase());
		return dataDisplayFormatDAO.AllDataDisplayFormat(vo);
	}

	public int insertDisplayFormat(DataDispFormatVO vo) {
//		vo.setDispGubn(vo.getDispGubn().toUpperCase());
//		vo.setDispGubnCode(vo.getDispGubnCode().toUpperCase());
		System.out.println(vo);
		int createCount = dataDisplayFormatDAO.insertDisplayFormat(vo);
		System.out.println(" #insert Count : " + createCount);
		
       return 0;
	}

	public int updateDisplayFormat(DataDispFormatVO vo) {
		vo.setDispGubn(vo.getDispGubn().toUpperCase());
		vo.setDispGubnCode(vo.getDispGubnCode().toUpperCase());
		vo.setDisplayCol(vo.getDisplayCol().toUpperCase());
		System.out.println(vo);
		int updateCount = dataDisplayFormatDAO.updateDisplayFormat(vo);
		System.out.println(" #update Count : " + updateCount);
		  return 0;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteDisplayFormat(List<DataDispFormatVO> delList) {
	
		for(DataDispFormatVO vo : delList) {
			vo.setDispGubn(vo.getDispGubn().toUpperCase());
			vo.setDispGubnCode(vo.getDispGubnCode().toUpperCase());
			vo.setDisplayCol(vo.getDisplayCol().toUpperCase());
			dataDisplayFormatDAO.deleteDisplayFormat(vo);
		}
	}
	
}
