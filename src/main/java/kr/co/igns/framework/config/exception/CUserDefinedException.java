package kr.co.igns.framework.config.exception;

public class CUserDefinedException extends RuntimeException {
    public CUserDefinedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUserDefinedException(String msg) {
        super(msg);
    }

    public CUserDefinedException() {
        super();
    }
}