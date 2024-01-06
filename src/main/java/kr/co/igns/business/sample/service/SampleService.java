package kr.co.igns.business.sample.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.business.sample.dao.oracle.SampleSecondDAO;
import kr.co.igns.business.sample.dao.postgre.SampleDAO;
import kr.co.igns.business.sample.model.SampleDTO;
import kr.co.igns.business.sample.model.SampleVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleService {
	
	private final SampleDAO sampleDao;
	private final SampleSecondDAO sampleSecondDao;
	
	
	public List<SampleVO> testGetMapping(final String userId) {
		return sampleDao.testGetMapping(userId);
	}
	
	public List<SampleVO> getSample(final String userId) {
		return sampleDao.getSample(userId);
	}
	
	public List<SampleVO> testSecondDb(String testParam) {
		return sampleSecondDao.testSecondDb(testParam);
	}
	
	public List<SampleVO> testPostMapping(SampleDTO sample) {
		return sampleDao.testPostMapping(sample);
	}
	
	public int testCreate(List<SampleVO> voList) {
		System.out.println(voList);
		for(SampleVO vo : voList) {
			if(vo.is__created__()) {
				int createCount = sampleDao.testCreate(vo);
				System.out.println(" #create Count : " + createCount);
				
				//강제에러발생 - 트랜젝션 테스트
				if (vo.getErrCd().equals("1")) {
					int a = 10/0;
				}
				
				vo.setUserId(vo.getUserId()+"Test");
				int createCount2 = sampleDao.testCreate(vo);
				System.out.println(" #create Count2 : " + createCount2);
				
			} else {
				int updateCount = sampleDao.testUpdate(vo);
				System.out.println(" #update Count : " + updateCount);
			}
		}
		
//		아래 주석처럼 service에서 try catch 문을 이용하여 예외처리시하면 안됨!!! 
//		만약 try catch를 사용하면  자제적으러 에러처리하기 때문에 response값에는 success로 넘어감
//		mybatis에서 에러가 발생하면 ExceptionAdvice.java에서 처리하도록 되어있음.
//		try {
//			System.out.println(voList);
//			for(SampleVO vo : voList) {
//				if(vo.is__created__()) {
//					int createCount = sampleDao.testCreate(vo);
//					System.out.println(" #create Count : " + createCount);
//				} else {
//					int updateCount = sampleDao.testUpdate(vo);
//					System.out.println(" #update Count : " + updateCount);
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			return -1;
//		}
		
		return 0;
    }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int testTransaction(List<SampleVO> voList) {
		System.out.println(voList);
		//트랜젝션 미적용 (testCreate) 시에는 에러전까지 실행된 sql문은 롤백되지 않음!
		
		for(SampleVO vo : voList) {
			if(vo.is__created__()) {
				int createCount = sampleDao.testCreate(vo);
				System.out.println(" #create Count : " + createCount);
				
				//강제에러발생 - 트랜젝션 테스트
				if (vo.getErrCd().equals("1")) {
					int a = 10/0;
				}
				
				vo.setUserId(vo.getUserId()+"Test");
				int createCount2 = sampleDao.testCreate(vo);
				System.out.println(" #create Count2 : " + createCount2);
				
			} else {
				int updateCount = sampleDao.testUpdate(vo);
				System.out.println(" #update Count : " + updateCount);
			}
		}
		
		return 0;
    }
	
	public void testDelete(List<SampleVO> voList) {
		// TODO Auto-generated method stub
		for(SampleVO vo : voList) {
			sampleDao.testDelete(vo);
		}
	}
	
}
