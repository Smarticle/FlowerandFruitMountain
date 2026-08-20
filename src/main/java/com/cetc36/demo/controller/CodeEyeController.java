package com.cetc36.demo.controller;

import com.cetc36.demo.common.enums.ErrorCodeEnum;
import com.cetc36.demo.common.Result;
import com.cetc36.demo.common.utils.MyBusinessValidator;
import com.cetc36.demo.request.CodeEyeTestRequest;
import com.cetc36.demo.vo.CodeEyeTestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Hound Web请求测试
 *
 * @author liuyang
 */
@RestController
@Slf4j
@Validated
@RequestMapping("/cetc36/codeeye/test")
@Api(tags = {"codeeye/test"})
public class CodeEyeController {

    @PostMapping("/success")
    @ApiOperation(value = "测试成功")
    public Result<CodeEyeTestVO> success(@RequestBody @Validated CodeEyeTestRequest request) {
        MyBusinessValidator.validate(request.getId() == 100, ErrorCodeEnum.SYSTEM_ERROR);
        CodeEyeTestVO vo = new CodeEyeTestVO();
        vo.setId(UUID.randomUUID().getLeastSignificantBits());
        log.info("mossad return success test");
        return Result.success(vo);
    }

    @PostMapping("/fail")
    @ApiOperation(value = "测试失败")
    public Result<Boolean> fail(@RequestBody @Validated CodeEyeTestRequest request) {
        log.info("mossad return fail test");
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.name());
    }


    @PostMapping("/exception")
    @ApiOperation(value = "测试异常")
    public Result<Boolean> exception(@RequestBody @Validated CodeEyeTestRequest request) {
        log.info("mossad return exception test");
        MyBusinessValidator.validate(request.getId() > 10, ErrorCodeEnum.SYSTEM_ERROR);
        MyBusinessValidator.validate(request.getId() < 10, ErrorCodeEnum.AUTH_ERROR);
        MyBusinessValidator.validate(request.getId() == 10, ErrorCodeEnum.LOGIN_FAIL);
        return Result.fail(ErrorCodeEnum.SYSTEM_ERROR, ErrorCodeEnum.SYSTEM_ERROR.name());
    }

    @PostMapping("/timeout")
    @ApiOperation(value = "测试超时")
    public Result<Boolean> timeout(@RequestBody @Validated CodeEyeTestRequest request) {
        log.info("mossad return 5s timeout");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Result.success(true);
    }
}