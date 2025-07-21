package com.sre.translation.exception;


import com.sre.commonBase.exception.CommonException;

/**
 * excel模版注解异常
 * @author cheng
 * @date 2023/5/16
 */
public class ExcelConductAnnotationException extends CommonException {
    public ExcelConductAnnotationException(String message){
        super(message);
    }
}
