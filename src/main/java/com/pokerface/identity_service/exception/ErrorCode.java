package com.pokerface.identity_service.exception;


public enum ErrorCode {
	UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
	INVALID_KEY(1001, "Invalid message key"),
	USER_EXISTED(1002, "Username existed"),
	USERNAME_INVALID(1003, "Username must be at least 5 characters"),
	 INVALID_PASSWORD(1004, "Password must be at least 8 characters"),
	 USER_NOT_EXISTED(1005, "Username not existed"),
	 UNAUTHENTICATED(1006, "Unauthenticated"),
	;
	
	
	 ErrorCode(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    private int code;
	    private String message;

	    public int getCode() {
	        return code;
	    }

	    public String getMessage() {
	        return message;
	    }
	
}
