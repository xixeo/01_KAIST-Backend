package kr.co.igns.framework.config.exception;

public class CUserExistException extends RuntimeException {
    public CUserExistException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUserExistException(String msg) {
        super(msg);
    }

    public CUserExistException() {
        super();
    }
}
