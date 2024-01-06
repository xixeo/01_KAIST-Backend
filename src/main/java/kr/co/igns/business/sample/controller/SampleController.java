package kr.co.igns.business.sample.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sun.istack.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.igns.business.sample.model.SampleDTO;
import kr.co.igns.business.sample.model.SampleVO;
import kr.co.igns.business.sample.service.SampleService;
import kr.co.igns.framework.config.exception.ExceptionAdvice;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.utils.redis.RedisUtil;
import lombok.RequiredArgsConstructor;

@Api(tags = {"0. Sample"})
@RestController
@RequiredArgsConstructor
public class SampleController {
	
	@Autowired
    private RedisUtil redisUtil;
	
	@Autowired
    RestTemplate restTemplate;
	
	private final ResponseService responseService;
	private final ExceptionAdvice exceptionAdvice;
	private final SampleService sampleService;
	private static final Logger log = LogManager.getLogger("com.project");
	
	//1. GET 기본형
	@GetMapping(value="/api/sample/testGetMapping")
	@ApiOperation(value = "1. GET test", notes = "GET test")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "testParam", value = "MFG_IND null 가능", required=false, defaultValue="", dataType = "String", paramType = "query")
	})
	public CommonResult testGetMapping(String testParam) {
		log.info(" 0. Sample --> testGetMapping API " + testParam);
		List<SampleVO> resultList = sampleService.testGetMapping(testParam);
		

		if (resultList.size() < 1) {
			//에러처리는 아래와 같이 처리함. 오류코드 등록은 exception_ko.yml에 등록하고 사용
			return responseService.getFailResult(Integer.valueOf(exceptionAdvice.getMessage("noResultFound.code")), exceptionAdvice.getMessage("noResultFound.msg"));
		}
		return responseService.getListResult(resultList);
	}
		
	//2. GET @PathVariable 사용
	@GetMapping(value="/api/sample/{userId}")
	@ApiOperation(value = "2. GET test2", notes = "@PathVariable 사용")
	@ResponseBody
	public CommonResult testGetMapping2(@PathVariable(value="userId") String userId) {
		log.info(" 0. Sample --> testGetMapping API " + userId);
		List<SampleVO> resultList = sampleService.testGetMapping(userId);
		return responseService.getListResult(resultList);
	}
	
	
	//3. second DB 사용
	@GetMapping(value="/api/sample/testSecondDb")
	@ApiOperation(value = "3. second DB GET test", notes = "second DB test")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "testParam", value = "MFG_IND null 가능", required=false, defaultValue="", dataType = "String", paramType = "query")
	})
	public List<SampleVO> testSecondDb(String testParam) {
		log.info(" 0. Sample --> testSecondDb API " + testParam);
		//반드시 return type은 CommonResult로 사용하기!!!!! 
		//List<SampleVO>같이 직접 자료형으로 넘기지 말것!!!!!!!
		return sampleService.testSecondDb(testParam);
	}
	
	//4. POST 기본형
	@PostMapping(value="/api/sample/testPostMapping")
	@ApiOperation(value = "4. POST test", notes = "POST test")
	public CommonResult testPostMapping(@RequestBody SampleDTO sample) {
		log.info(" 0. Sample --> testPostMapping API " + sample);
		List<SampleVO> resultList = sampleService.testPostMapping(sample);
		return responseService.getListResult(resultList);
	}
	
	//5. INSERT/UPDATE 기본형
	@ApiOperation(value="5. INSERT/UPDATE test", notes="INSERT TEST", produces="application/json")
	@PostMapping(value = "/api/sample/testCreate")
	private CommonResult testCreate(@NotNull @RequestBody List<SampleVO> voList) throws Exception {
		int resultCode = sampleService.testCreate(voList);
		return responseService.getSuccessResult();
	}
	
	
	//6. DELETE 기본형
	@ApiOperation(value="6. DELETE test", notes="DELETE test.", produces="application/json")
	@PostMapping(value = "/api/sample/testDelete")
	public CommonResult testDelete(HttpServletRequest request, @RequestBody List<SampleVO> voList) {
		sampleService.testDelete(voList);
		return responseService.getSuccessResult();
	}
	
	//7. TRANSACTION INSERT/UPDATE 
	@ApiOperation(value="7. TRANSACTION INSERT/UPDATE test", notes="TRANSACTION TEST", produces="application/json")
	@PostMapping(value = "/api/sample/testTransaction")
	private CommonResult testTransaction(@NotNull @RequestBody List<SampleVO> voList) throws Exception {
		int resultCode = sampleService.testTransaction(voList);
		return responseService.getSuccessResult();
	}
	
	//8. redis test
	@GetMapping(value="/api/sample/redis/{key}")
	@ApiOperation(value = "8. redis test", notes = "redisTest")
	@ResponseBody
	public CommonResult testRedis(@PathVariable(value="key") String key) {
		
		String msg = "sampleTest!" + key;
		redisUtil.setData(key, msg);
		String saveData = redisUtil.getData(key);
		System.out.println("redis Test : " + saveData);
        
		return responseService.getSuccessResult();
	}
	
	//9. restTemplate test
	@GetMapping(value="/restTemplate")
	@ApiOperation(value = "9. restTemplate test", notes = "restTemplateTest")
	@ResponseBody
	public String testRestTemplate(){
        
		/*GET*/
		//String obj = restTemplate.getForObject("http://localhost:7000/api/sample/admin", String.class);        
        //return obj;
        
		/*post*/
		//String url = "http://devloggergen.thubiot.com/api/getAllCmCode";
		String url = "http://localhost:9000/api/getAllCmCode";
			
		restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");  
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject personJsonObject = new JSONObject();
	    personJsonObject.put("domainCd", "admin");
	    
	    HttpEntity<String> request = new HttpEntity<>(personJsonObject.toString(), headers);
	    //System.out.println("request -> "+request.toString());
        
	    String personResultAsJsonStr = restTemplate.postForObject(url, request, String.class);
        //System.out.println("personResultAsJsonStr -> "+personResultAsJsonStr);
        
        ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);
        //System.out.println("result.getStatusCodeValue() -> "+result.getStatusCodeValue());
        
        return personResultAsJsonStr; 
	}
	
}
