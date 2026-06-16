package com.bdbt.tableadmin.common;

/**
 * 业务异常，携带 HTTP 状态码与提示信息。
 */
public class BusinessException extends RuntimeException {

    private final int status;

    public BusinessException(int status, String message) {
        super(message);
        this.status = status;
    }

    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(400, message);
    }

    public int getStatus() {
        return status;
    }
}
