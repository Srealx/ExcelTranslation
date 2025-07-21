package com.sre.translation.beans.result;

import com.sre.translation.beans.ExcelImportResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * excel处理通用返回对象
 * @author cheng
 * @date 2023/5/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
public class ExcelTranslationImportResult extends ExcelTranslationResult{
    /**
     * excel导入结果对象
     */
    private ExcelImportResult excelImportResult;


    ExcelTranslationImportResult(Boolean success,String message){
        super(success,message);
    }


    public static ExcelTranslationImportResult error(String message){
        return new ExcelTranslationImportResult(false,message);
    }

    public static ExcelTranslationImportResult success(ExcelImportResult excelImportResult,Integer excelType){
        ExcelTranslationImportResult excelTranslationResult = new ExcelTranslationImportResult();
        excelTranslationResult.setSuccess(true);
        excelTranslationResult.setExcelType(excelType);
        excelTranslationResult.setExcelImportResult(excelImportResult);
        return excelTranslationResult;
    }

}
