package com.cetc36.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 *
 * @author liuyang
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

        SYSTEM_ERROR(5000,"系统开小差了，喝杯咖啡歇一歇把"),
        FILE_ERROR(5001, "文件错误"),
        LOGIN_INVALID(4000,"登陆失效，请重新登陆"),
        LOGIN_FAIL(4001,"登陆失败，请检查用户名密码是否正确"),
        AUTH_ERROR(3000,"权限错误，请检查您的权限"),
    ;

    /**
     * 错误类型码
     */
    private Integer code;
    /**
     * 描述
     */
    private final String desc;

    public static ErrorCodeEnum getByCode(int code) {
        for(ErrorCodeEnum codeEnum : ErrorCodeEnum.values()){
            if(codeEnum.getCode() == code){
                return codeEnum;
            }
        }
        return null;
    }
}
