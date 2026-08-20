package com.cetc36.demo.common.exception;

import com.cetc36.demo.common.enums.ErrorCodeEnum;
import lombok.Getter;

import java.util.List;

/**
 * 业务异常
 *
 * @author liuyang
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误编码
     */
    private ErrorCodeEnum errorCode;

    /**
     * 错误变量列表
     */
    private List<String> errorMessageList;


    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        this.errorCode = errorCodeEnum;
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, List<String> errorMessageList) {
        this.errorCode = errorCodeEnum;
        this.errorMessageList = errorMessageList;
    }
}