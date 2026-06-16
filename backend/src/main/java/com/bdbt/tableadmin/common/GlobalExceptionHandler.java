package com.bdbt.tableadmin.common;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理：统一返回 ApiResponse 结构。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusiness(BusinessException ex, HttpServletResponse resp) {
        resp.setStatus(ex.getStatus());
        return ApiResponse.fail(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex, HttpServletResponse resp) {
        resp.setStatus(400);
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ApiResponse.fail(400, msg);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<?> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletResponse resp) {
        resp.setStatus(409);
        log.warn("数据完整性冲突: {}", ex.getMostSpecificCause().getMessage());
        return ApiResponse.fail(409, "数据冲突：编码可能已存在或存在引用约束");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleAny(Exception ex, HttpServletResponse resp) {
        resp.setStatus(500);
        log.error("未处理异常", ex);
        return ApiResponse.fail(500, "服务器内部错误：" + ex.getMessage());
    }
}
