package kr.co.igns.iotMonitoring.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.iotMonitoring.model.DataDispFormatSearchDTO;
import kr.co.igns.iotMonitoring.model.DataDispFormatVO;

@Mapper
@Repository
public interface DataDisplayFormatDAO {

	public List<DataDispFormatVO> AllDataDisplayFormat(DataDispFormatSearchDTO vo);

	public int insertDisplayFormat(DataDispFormatVO vo);

	public int updateDisplayFormat(DataDispFormatVO vo);

	public void deleteDisplayFormat(DataDispFormatVO vo);
}
