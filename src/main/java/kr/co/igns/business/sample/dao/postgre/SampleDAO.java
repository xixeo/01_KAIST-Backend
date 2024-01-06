package kr.co.igns.business.sample.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.igns.business.sample.model.SampleDTO;
import kr.co.igns.business.sample.model.SampleVO;

@Mapper
@Repository
public interface SampleDAO {
	List<SampleVO> testGetMapping(final String userId);
	List<SampleVO> getSample(final String userId);
	List<SampleVO> testPostMapping(SampleDTO sample);
	public int testCreate(SampleVO vo);
	public int testUpdate(SampleVO vo);
	public int testDelete(SampleVO vo);
}
