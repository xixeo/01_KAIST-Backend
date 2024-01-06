package kr.co.igns.framework.config.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    @ExceptionHandler(CUserDefinedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userDefinedException(HttpServletRequest request, CUserDefinedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("-0000")), getMessage(e.toString().split(":")[1].replace(" ", "")));
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest req, Exception e) {
    	System.out.println("   **** e : " + e);
    	//log4j2 처리필요!!!!!
    	//sql 오류처리
    	if (e instanceof BadSqlGrammarException) {
    		SQLException se = ((BadSqlGrammarException) e).getSQLException();
    		return responseService.getFailResult(Integer.valueOf(getMessage("badSqlGrmmar.code")), getMessage("badSqlGrmmar.msg") + se.getMessage());
		} else if (e instanceof InvalidResultSetAccessException) {
			SQLException se = ((InvalidResultSetAccessException) e).getSQLException();
			return responseService.getFailResult(Integer.valueOf(getMessage("invaildResult.code")), getMessage("invaildResult.msg") + se.getMessage());
		} else if (e instanceof DuplicateKeyException) {	
			//
			return responseService.getFailResult(Integer.valueOf(getMessage("codeDuplicateError.code")), getMessage("codeDuplicateError.msg"));
		} else if (e instanceof DataIntegrityViolationException) {
			// 고유성 제한 위반과 같은 데이터 삽입 또는 업데이트시 무결성 위반
			return responseService.getFailResult(Integer.valueOf(getMessage("dataDuplicateError.code")), getMessage("dataDuplicateError.msg"));
		} else if (e instanceof DataAccessResourceFailureException) {
			// 데이터 액세스 리소스가 완전히 실패했습니다 (예 : 데이터베이스에 연결할 수 없음)
			return responseService.getFailResult(Integer.valueOf(getMessage("dbConnectError.code")), getMessage("dbConnectError.msg"));
		} else if (e instanceof CannotAcquireLockException) {
			

		} else if (e instanceof DeadlockLoserDataAccessException) {
			// 교착 상태로 인해 현재 작업이 실패했습니다.
			return responseService.getFailResult(Integer.valueOf(getMessage("deadlockError.code")), getMessage("deadlockError.msg"));
		} else if (e instanceof CannotSerializeTransactionException) {

		}
    	
        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFound(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    @ExceptionHandler(CEmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSigninFailed(HttpServletRequest request, CEmailSigninFailedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.msg"));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("entryPointException.code")), getMessage("entryPointException.msg"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.msg"));
    }

    @ExceptionHandler(CCommunicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult communicationException(HttpServletRequest request, CCommunicationException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("communicationError.code")), getMessage("communicationError.msg"));
    }

    @ExceptionHandler(CUserExistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult communicationException(HttpServletRequest request, CUserExistException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("existingUser.code")), getMessage("existingUser.msg"));
    }

    // code정보에 해당하는 메시지를 조회합니다.
    public String getMessage(String code) {
        return getMessage(code, null);
    }
    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}