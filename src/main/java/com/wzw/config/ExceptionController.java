package com.wzw.config;

import com.wzw.common.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController
{
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public CommonResult<String> foo(Exception e)
    {
        return CommonResult.errorMsg("全局捕获异常：{}", e.getMessage());
    }
}
