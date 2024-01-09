package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }


    /**
     * 用户名重复异常
     * @param d
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result duplicateUserName(DuplicateKeyException d){
        log.error("用户名重复",d.getMessage());
        return Result.error("用户名重复");
    }

    /**
     * 非预期异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result unexpectedAnomaly(Exception e){
        e.printStackTrace();
        return Result.error("服务器开小差了,请稍后重试");
    }

}
