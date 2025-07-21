package com.sre.translation.beans.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * excel处理通用返回对象
 * @author cheng
 * @date 2023/5/17
 */
@Data
@NoArgsConstructor
@ToString
public class ExcelTranslationResult {
    ExcelTranslationResult(Boolean success,String message){
        this.success = success;
        this.message = message;
    }
    /**
     * 枚举code
     */
    private Long excelBusinessEnumCode;
    /**
     * 本次调度名称
     */
    private String progressName;

    /**
     * excel任务类型
     */
    private Integer excelType;

    private Boolean success;
    private String message;

    public static ExcelTranslationResult error(String message){
        return new ExcelTranslationResult(false,message);
    }
    public static ExcelTranslationResult success(Integer excelType){
        ExcelTranslationResult excelTranslationResult = new ExcelTranslationResult();
        excelTranslationResult.setExcelType(excelType);
        excelTranslationResult.setSuccess(true);
        return excelTranslationResult;
    }



}
