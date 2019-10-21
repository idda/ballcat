package com.hccake.ballcat.common.core.exception;

import com.hccake.ballcat.common.core.result.ResultStatus;
import lombok.Getter;

/**
 * 系统自定义异常
 * @author Hccake
 */
@Getter
public class BallCatException extends RuntimeException {

    private String msg;
    private int code;
    
    public BallCatException(ResultStatus resultStatus) {
		super(resultStatus.getMessage());
		this.code = resultStatus.getCode();
		this.msg = resultStatus.getMessage();
	}
	
	public BallCatException(ResultStatus resultStatus, Throwable e) {
		super(resultStatus.getMessage(), e);
		this.code = resultStatus.getCode();
		this.msg = resultStatus.getMessage();
	}
	
	public BallCatException(int code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public BallCatException( int code, String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}
}