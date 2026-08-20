package com.cetc36.demo.common;

import com.cetc36.demo.common.enums.ErrorCodeEnum;
import com.cetc36.demo.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.naming.SizeLimitExceededException;
import javax.xml.bind.ValidationException;

/**
 * 全局异常处理
 *
 * @author liuyang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

   // 设置通用前端异常拦截类
    @ExceptionHandler(value = {BusinessException.class})
    public Result businessException(BusinessException ex) {
        log.error("business exception:{}", ex.getMessage(), ex);
        return Result.fail(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public Result missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("missing servlet request parameter exception:{}", ex.getMessage(), ex);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

    @ExceptionHandler(value = {MultipartException.class})
    public Result uploadFileLimitException(MultipartException ex) {
        log.error("upload file size limit:{}", ex.getMessage(), ex);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

    /**
     * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常，无法经过自定义HandlerMethodArgumentResolver处理过的异常，如参数类型异常
     */
    @ExceptionHandler(value = {BindException.class})
    public Result bindException(BindException ex) {
        log.error("method bindException argument not valid exception:{}", ex.getMessage(), ex);
        BindingResult bindingResult = ex.getBindingResult();
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

    /**
     * post/get:请求接口处理请求参数类型错误 validate失败后抛出的异常是MethodArgumentTypeMismatchException异常
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public Result methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("method argument type mismatch exception:{}", ex.getMessage(), ex);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

    @ExceptionHandler(value = {SizeLimitExceededException.class})
    public Result sizeLimitExceededException(SizeLimitExceededException e) {
        log.debug("sizeLimitExceededException exception:{}", e.getMessage(), e);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, "SizeLimitExceededException");
    }

    @ExceptionHandler(value = {ValidationException.class})
    public Result validationException(ValidationException ex) {
        log.debug("validation exception:{}", ex.getMessage(), ex);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, "ValidationException");
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Result webExchangeBindException(WebExchangeBindException e) {
        log.error("webExchangeBindException:{}", e.getMessage(), e);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    public Result duplicateKeyException(DuplicateKeyException ex) {
        log.error("duplication key exception:{}", ex.getMessage());
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, "DuplicateKeyException");
    }

    @ExceptionHandler(value = {Exception.class})
    public Result exception(Exception e) {
        log.error("system error", e);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

    @ExceptionHandler(value = {Throwable.class})
    public Result throwable(Throwable t) {
        log.error("system error", t);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public Result exceptionHandler(NullPointerException e) {
        log.error("NullPointerException:{}", e.getMessage(), e);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public Result httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("system request param match error", e);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.getDesc());
    }

}