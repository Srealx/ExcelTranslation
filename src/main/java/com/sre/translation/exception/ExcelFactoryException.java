package com.sre.translation.exception;

import com.sre.commonBase.exception.CommonException;

/**
 * excel运行时异常
 * @author cheng
 * @date 2023/5/16
 */
public class ExcelFactoryException extends CommonException {
    public ExcelFactoryException(String message){
        super(message);
    }
}
