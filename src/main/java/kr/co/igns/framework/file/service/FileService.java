package kr.co.igns.framework.file.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.igns.framework.file.model.FileVO;
import kr.co.igns.framework.file.model.MultiFileVO;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.file.dao.postgre.FileDao;
import kr.co.igns.framework.file.model.FileDto;
import kr.co.igns.framework.utils.etc.IgnsSessionUtils;
import kr.co.igns.framework.utils.file.CommonFileUtils;
import kr.co.igns.framework.utils.file.FileCommonDto;
import kr.co.igns.framework.utils.file.UploadParameters;
import kr.co.igns.framework.utils.type.EncodeUtils;
import kr.co.igns.framework.utils.type.Types;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FileService {
	
	private final FileDao fileDao;
	private static int result = 0;
	
	//yml에서 설정
	@Value("${spring.file.max-size}")
	private long FILE_MAX_SIZE;

	@Value("${spring.file.upload-dir}")
	private String FILE_DIR;
	
	/*
	 * 파일업로드
	 */
	@Transactional
    public MultiFileVO multiUpload(UploadParameters uploadParameters) throws IOException {
		
		//날짜별로 디렉토리 구분. 세부적으로 나눠야하는지 협의필요.
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
    	Date dt = new Date(); 
    	String timeDir = sdf.format(dt);
    	
    	//파일정보
    	MultipartFile file = uploadParameters.getMultipartFile();
    	String fileName = file.getOriginalFilename();
    	String[] fileNameSplit = fileName.split("\\.");
    	
    	//파일명 가져오기
    	HashMap<String,String> realFile = new HashMap<String, String>();
    	realFile = getFileName(fileName);
    	String baseName = realFile.get("filename");
    	String sort = realFile.get("sort");
    	String extension = fileNameSplit[fileNameSplit.length - 1];
    	String fileType = getFileType(extension);
    	Long fileSize = file.getSize();
    	
    	//저장경로설정
    	String savePath = getSavePath(timeDir + "/" + baseName +"." + extension );
    	File dir = null;
    	dir = new File(getSavePath(timeDir));   
    	if(!dir.exists()) {
    		dir.mkdirs();
    	}
    	//System.out.println("  #3 : " + savePath );
    	
    	
    	try {
    		FileCopyUtils.copy( file.getInputStream(), new FileOutputStream( savePath ));
    	} catch (Exception e) {
    		throw new CUserDefinedException("파일복사에 실패했습니다.");
    	}
    	
    	//저장파일정보 DB table에 저장(TB_FILE_INFO)
    	FileVO fileVO = new FileVO();
    	//postgre auto increment
    	//fileVO.setFileId(FileDao.getFileSequence()); 
    	fileVO.setFileNm(fileName);
    	fileVO.setSaveNm(timeDir + "/" + baseName +"." + extension);
    	fileVO.setFileType(fileType);
    	fileVO.setFileExtension(extension);
    	fileVO.setFileSize(String.valueOf( fileSize ));
    	fileVO.setDelYn("N");
    	fileVO.setSort("0");
    	fileVO.setFileDesc(fileName);
    	fileVO.setTargetId(uploadParameters.getTargetId());
    	fileVO.setTargetType(uploadParameters.getTargetType());
    	fileVO.setInsertUserId(IgnsSessionUtils.getCurrentLoginUserCd());
    	//fileVO.setUpdateUserId(IgnsSessionUtils.getCurrentLoginUserCd());
    	fileDao.createFile(fileVO);
    	
    	//저장파일데이터 리스트 return
    	MultiFileVO multiFile = new MultiFileVO();
    	multiFile.setFileId(fileVO.getFileId());
    	multiFile.setFileNm(fileName);
    	multiFile.setTargetId(uploadParameters.getTargetId());
    	multiFile.setTargetType(uploadParameters.getTargetType());
    	multiFile.setFileSize(String.valueOf( fileSize ));
    	multiFile.setFileDesc(fileName);
    	
    	return multiFile;
    }
	
	/*
	 * 저장파일명 처리
	 */
	public HashMap<String, String> getFileName(String fileName) {
		String realFile = "";
		HashMap<String,String> list = new HashMap<String, String>();
		
		String[] fileNameSplit = fileName.split("\\.");
		String extension = fileNameSplit[fileNameSplit.length - 1];
		String nameRemoveExtension = FilenameUtils.removeExtension(fileName);
		
		Integer iCnt = fileDao.getFileNmCount(nameRemoveExtension);
		
		if( iCnt == 0 ) {
			realFile = nameRemoveExtension;
			list.put("filename", realFile);
			list.put("sort", "0");
		} else {
			realFile = nameRemoveExtension + "_" + iCnt;
			list.put("filename", realFile);
			list.put("sort", String.valueOf(iCnt));
		}
		return list;
	}
	
	/*
	 * 파일타입
	 */
    private String getFileType(String extension) {
        switch (extension.toUpperCase()) {
            case Types.FileExtensions.PNG:
            case Types.FileExtensions.JPG:
            case Types.FileExtensions.JPEG:
            case Types.FileExtensions.GIF:
            case Types.FileExtensions.BMP:
            case Types.FileExtensions.TIFF:
            case Types.FileExtensions.TIF:
                return Types.FileType.IMAGE;

            case Types.FileExtensions.PDF:
                return Types.FileType.PDF;

            default:
                return Types.FileType.ETC;
        }
    }
    
    /*
	 * 저장경로
	 */
    public String getSavePath(String saveName) {
        return getBasePath() + "/" + saveName;
    }
    
    /*
	 * 기본경로
	 */
    public String getBasePath() {
        return FILE_DIR;
    }
    
    
	/*
	 * 파일 목록 조회
	 */
	public List<FileVO> getFile(FileDto dto) {
		System.out.println("  #1 : " + fileDao.getFile(dto));
		return fileDao.getFile(dto);
	}
	
	
	/*
	 * 파일 삭제
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int deleteFile(List<FileVO> params) {
		for(FileVO i : params) {	
			FileCommonDto comDto = new FileCommonDto(); 
			comDto.setTableName("TB_FILE_INFO");
			comDto.setTableCol("FILE_ID");
			comDto.setValue(i.getFileId());
			Integer getCnt = fileDao.getFileCount(comDto);
			if(getCnt > 0) {
				// DB에서 파일경로 가져오기
				String filePath = fileDao.getFilePath(i.getFileId());
				// DB에서 파일데이터 삭제
				result = fileDao.deleteFile(i.getFileId());
				// 로컬파일 삭제
				deleteLocalFile(filePath);
			}else {
				result = 1;
				throw new CUserDefinedException("삭제할 파일이 없습니다.");
			}
		}
		return result;
	}
	
	
	/*
	 * 로컬 파일 삭제
	 */
	public void deleteLocalFile(String filePath) {		
		String totalPath = FILE_DIR + "/" + filePath;
		File filesDir = new File(totalPath);
		try {
			// 파일만 삭제
			if(filesDir.exists()) {
				filesDir.delete();
			}else {
				throw new CUserDefinedException("이미 삭제 되었거나, 삭제할 파일이 없습니다.");
			}
		}catch (Exception e) {
			e.getStackTrace();
		}		
	}
	
	/*
	 * 파일다운로드
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, int fileId) {
    	//fileId로 파일데이터 불러오기
		FileDto fileDto = new FileDto();
		fileDto.setFileId(fileId);
		FileVO fileVo = fileDao.getFileOne(fileDto);
		String filePath = getSavePath(fileVo.getSaveNm());
    	File file = new File(filePath);
    	//System.out.println("  #1 : " + fileVo.get(0));
    	
    	if(file.exists()) {
    		OutputStream os = null;
    		FileInputStream is = null;
    		
    		try {
    			String fileName = EncodeUtils.encodeDownloadFileName(fileVo.getFileNm());
    			//System.out.println("  #2 FileName : " + fileName);
    			
    			response.setContentType("application/fileName-stream; charset=utf-8");
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setCharacterEncoding("UTF-8");
				
				os = response.getOutputStream();
				is = null;
				is = new FileInputStream(file);
				
				FileCopyUtils.copy(is, os);
				os.flush();
    				
    		}catch (FileNotFoundException var25) {
    			//
			} catch (IOException var26) {
				//
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
    	}
    }
	
	/*
	 * 파일 압축 다운로드
	 * id를 멀티로 선택하여 배열로 받아온다
	 * 선택한 파일들만 압축다운로드. 
	 */
	public ResponseEntity<byte[]> getDownloadSelectZIP(FileDto dto) throws IOException {
				
		String str = dto.getFileIdArr();
		String[] arr = str.split(",");
		
		FileVO vo = new FileVO();
		List<FileVO> filesList = new ArrayList<FileVO>();
		
		for(int i = 0; i < arr.length; i++) {
			dto.setFileId(Integer.parseInt(arr[i]));
			vo = fileDao.getFileOne(dto);
			
			filesList.add(vo);
		}
		String zipName = dto.getZipName() + ".zip";
		return fileZip(filesList, zipName);
	}
	
	/*
	 * 파일리스트를 압축파일로 반환 
	 */
	public ResponseEntity<byte[]> fileZip(List<FileVO> filesList, String zipName) throws IOException {
		
		if(filesList == null) return null;
		
		byte[] buffer = new byte[1024];
		HttpHeaders httpHeaders = new HttpHeaders();
		ResponseEntity<byte[]> reEntity = new ResponseEntity<byte[]>(buffer, httpHeaders, HttpStatus.OK);
		
		ZipOutputStream zout = null;
		String basePath = FILE_DIR;
		
		if(filesList.size() > 0) {
			try {
				
				
				zout = new ZipOutputStream(new FileOutputStream(basePath + "/" + zipName));				
				FileInputStream in = null;
				
				for(int k=0; k < filesList.size(); k++) {
					String totalPath = FILE_DIR + "/" + filesList.get(k).getSaveNm();
					File filesDir = new File(totalPath);
					
					if(filesDir.exists()) {
						in = new FileInputStream(totalPath);
						zout.putNextEntry(new ZipEntry(filesList.get(k).getSaveNm()));
						
						int len;
						while((len = in.read(buffer)) > 0) {
							zout.write(buffer, 0, len);
						}
						
						zout.closeEntry();
						in.close();
					}else {
						
					}
				}				
				zout.close();
				
								
			}catch (IOException e) {
				// TODO: handle exception
			}finally {
				if (zout != null) {
					zout.close();
				}
				
				byte[] bytes = FileUtils.readFileToByteArray(new File(getSavePath(zipName)));
				String fileName = EncodeUtils.encodeDownloadFileName(zipName);
		        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		        httpHeaders.setContentLength(bytes.length);
		        httpHeaders.setContentDispositionFormData("attachment", fileName);
		        reEntity = new  ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
		        
		        File filesDir = new File(basePath + "/" + zipName);
				try {
					
					// 만들어진 zip파일 삭제
					if(filesDir.exists()) {
						filesDir.delete();
					}else {
						
					}
				}catch (Exception e) {
					// TODO: handle exception
				}				
			}
		}
		return reEntity;
	}
	
}
