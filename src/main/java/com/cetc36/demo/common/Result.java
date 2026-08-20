package com.cetc36.demo.common;

import com.cetc36.demo.common.enums.ErrorCodeEnum;
import lombok.*;

import java.io.Serializable;


/**
 * 返回结果
 *
 * @author liuyang
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -828357066742441358L;

    private boolean success = true;

    private T data;

    private Integer code;

    private String message;

    private T errors;

    private static final Result<Void> SUCCESS = new Result();

    static {
        SUCCESS.setCode(200);
    }

    public Result(T data) {
        this.code = 200;
        this.data = data;
    }

    public Result(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public static Result<Void> success() {
        return SUCCESS;
    }

    public static <T> Result<T> success(T t) {
        return new Result<>(t);
    }

    public static <T> Result<T> fail(ErrorCodeEnum codeEnum, String message) {
        return new Result<>(false, codeEnum.getCode(), message);
    }

}