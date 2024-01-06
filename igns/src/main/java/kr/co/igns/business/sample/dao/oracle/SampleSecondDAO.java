package kr.co.igns.business.sample.dao.oracle;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.sample.model.SampleVO;

@Mapper
@Repository
public interface SampleSecondDAO {
	
	List<SampleVO> testSecondDb(String testParam);
}
