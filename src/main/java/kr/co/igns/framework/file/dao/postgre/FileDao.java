package kr.co.igns.framework.file.dao.postgre;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.igns.framework.file.model.FileDto;
import kr.co.igns.framework.file.model.FileVO;
import kr.co.igns.framework.utils.file.FileCommonDto;


@Mapper
public interface FileDao {
	
	// 시퀀스 postgre auto increment
	//public int getFileSequence();
	
	// 저장
	public int createFile(FileVO vo);
	
	//파일카운트
	public Integer getFileNmCount(String fileName);
	public Integer getFileCount(FileCommonDto params);
	
	// 조회
	public List<FileVO> getFile(FileDto params);
	public FileVO getFileOne(FileDto params);
	
	// 파일경로 가져오기
	public String getFilePath(int fileId);
	
	// 삭제
	public int deleteFile(int id);
	
	
}
