package kr.co.igns.framework.comm.dao.postgre;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import kr.co.igns.framework.comm.model.TokenVO;

@Mapper
public interface TokenDao {

	String findBykey(String uuid);
	Date getTokenExpirationDt(String refreshToken);
	
	public int checkfreshToken(TokenVO vo);
	public int createRefreshToken(TokenVO vo);
	public int updateRefreshToken(TokenVO vo);
	public int deleteRefreshToken(TokenVO vo);
	public int deleteExpirationToken();
	
}
