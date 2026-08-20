package com.cetc36.demo.common.utils;


import com.cetc36.demo.common.enums.ErrorCodeEnum;
import com.cetc36.demo.common.exception.BusinessException;

import java.util.List;


/**
 * 业务验证器
 *
 * @author liuyang
 */
public final class MyBusinessValidator {

    /**
     * 断言执行表达式后的结果是 false，否则抛出业务异常信息
     */
    public static void validate(boolean expression, ErrorCodeEnum errorCodeEnum) {
        if (expression) {
            throw new BusinessException(errorCodeEnum);
        }
    }

    /**
     * 断言执行表达式后的结果是 false，否则抛出业务异常信息
     */
    public static void validate(boolean expression, ErrorCodeEnum errorCodeEnum, List<String> errorMessageList) {
        if (expression) {
            throw new BusinessException(errorCodeEnum, errorMessageList);
        }
    }
}