package com.sre.translation.beans;

import lombok.Data;

import java.util.Map;

/**
 * excel导入结果处理pram
 * @author chen gang
 * @date 2025/4/10
 */
@Data
public class ExcelImportResultHandlerParam {
    /**
     * 异常数据列表
     */
    Map<String,Map<Integer,ExcelImportErrorDataResult<?>>> errorDataMap;
}
