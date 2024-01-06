package kr.co.igns.framework.file.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.igns.framework.file.service.FileService;
import kr.co.igns.framework.file.model.MultiFileVO;
import kr.co.igns.framework.file.model.FileVO;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.file.model.FileDto;
import kr.co.igns.framework.utils.file.CommonFileUtils;
import kr.co.igns.framework.utils.file.UploadParameters;

@Api(tags = {"4. File"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/file")
public class FileController {
	
	private final FileService fileService;
	private final ResponseService responseService; // 결과를 처리할 Service
	private static int result = 0;
		
	@ApiOperation(value = "파일 등록", notes = "파일을 서버에 등록한다. TARGET_ID, TARGET_TYPE 으로 저장페이지를 구분한다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "targetType", value = "타켓타입", required=true, defaultValue="", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "targetId", value = "타겟아이디", required=true, defaultValue="", dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "fileDesc", value = "파일설명", required=false, defaultValue="", dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "sort", value = "정렬", required=false, defaultValue="0", dataType = "String", paramType = "query")
	})
	@PostMapping(value = "/upload", headers = "X-APIVERSION=2.0.0")
	public List<MultiFileVO> upload(
            @RequestParam(value = "file") MultipartFile multipartFile,
            @RequestParam(value = "targetType", required = true) String targetType,
            @RequestParam(value = "targetId", required = true) String targetId,
            @RequestParam(value = "sort", required = false, defaultValue = "0") String sort,
            @RequestParam(value = "deleteIfExist", required = false, defaultValue = "false") boolean deleteIfExist,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "thumbnail", required = false, defaultValue = "false") boolean thumbnail,
            @RequestParam(value = "thumbnailWidth", required = false, defaultValue = "640") int thumbnailWidth,
            @RequestParam(value = "thumbnailHeight", required = false, defaultValue = "640") int thumbnailHeight ) throws  IllegalStateException, IOException {
		
		UploadParameters uploadParameters = new UploadParameters();
        uploadParameters.setMultipartFile(multipartFile);
        uploadParameters.setTargetType(targetType);
        uploadParameters.setTargetId(targetId);
        uploadParameters.setSort(sort);
        uploadParameters.setDeleteIfExist(deleteIfExist);
        uploadParameters.setDesc(desc);
        uploadParameters.setThumbnail(thumbnail);
        uploadParameters.setThumbnailWidth(thumbnailWidth);
        uploadParameters.setThumbnailHeight(thumbnailHeight);
        
        List<MultiFileVO> fileList = new ArrayList();
		try {
	    	fileList.add( fileService.multiUpload(uploadParameters) );
		} catch (Exception e) {
			//fileService.deleteFile(filevo);
			throw new CUserDefinedException("파일업로드에 실패했습니다.");
		}

		return fileList;
	}
	
	
	@ApiOperation(value = "파일 조회", notes = "파일을 조회한다")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "fileId", value = "파일아이디", required=false, defaultValue="", dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "targetType", value = "타켓타입", required=true, defaultValue="", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "targetId", value = "타겟아이디", required=true, defaultValue="", dataType = "String", paramType = "query")        
	})
	@GetMapping(value = "/getFile", produces="application/json")
	private List<FileVO> getFile(FileDto dto) throws Exception {
		//
		List<FileVO> list = fileService.getFile(dto);
		return list;
	}
	
	@ApiOperation(value = "파일 삭제", notes = "파일을 삭제한다(fileId, targetId, targetName)")
	@PostMapping(value = "/deleteFile", produces="application/json")
	private CommonResult deleteFile(@NotNull @RequestBody List<FileVO> dataList) throws Exception {
		
		result = fileService.deleteFile(dataList);
		return responseService.getSuccessResult();
	}
	
	@ApiOperation(value = "파일 다운로드", notes = "파일을 다운로드한다(fileId)")
    @GetMapping(value="/downloadFile")
    public void getDownloadFile(HttpServletRequest req, HttpServletResponse response, @RequestParam int fileId) {
    	
    	fileService.downloadFile(req, response, fileId);
    }
    
	// 파일 압축 다운로드 (타겟 중에서도 선택하여)
	@ApiOperation(value = "파일 다운로드", notes = "파일을 다운로드한다(fileId)")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "fileIdArr", value = "타겟아이디(배열)", required=true, defaultValue="", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "zipName", value = "압출파일이름", required=true, defaultValue="", dataType = "String", paramType = "query")
	})
    @GetMapping(value = "/downloadSelectZIP" ,produces="application/zip")
    public ResponseEntity<byte[]> downloadSelectZIP(FileDto dto) throws IOException {
        return fileService.getDownloadSelectZIP(dto);
    }
    
    //이하 기능확인필요
	
	
}
