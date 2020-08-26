package com.jack.miaosha.exception;

import com.jack.miaosha.result.CodeMsg;
import com.jack.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {
    /**
     * 写了一个全局异常的解析器，定义了一些，通用的异常的，这样异常返回，就能清晰定义到这是什么异常
     * @param request
     * @param e
     * @return
     */

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> allErrors = ex.getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String msg = objectError.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else if (e instanceof GlobleException) {
            GlobleException ex = (GlobleException) e;
            return Result.error(ex.getCm());
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

    }

}
