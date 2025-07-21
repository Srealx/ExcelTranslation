package com.sre.translation.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * excel导入多sheet页对象
 * @author cheng
 * @date 2023/5/17
 */
@Data
@ToString
@AllArgsConstructor
public class ExcelImportErrorDataResult<T> {
    protected T data;
    /**
     * 字段异常集合
     * k: 字段名
     * v: 异常信息
     */
    protected Map<String,String> fieldErrorMap;
}
