package com.sre.translation.beans.result;

import com.sre.translation.beans.ExcelResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * excel任务结果—异步结果
 * @author chen gang
 * @date 2025/4/14
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ExcelTranslationAsyResult extends ExcelTranslationResult{
    /**
     * 本次调度uuid
     */
    private String uuid;

    /**
     * 进度
     */
    private Float progress;

    /**
     * 详情结果-导入或导出的结果结构体
     */
    private ExcelResult infoResult;

    ExcelTranslationAsyResult(Boolean success,String message){
       super(success,message);
    }


    public static ExcelTranslationAsyResult success(String uuid,Integer excelType){
        ExcelTranslationAsyResult excelTranslationResult = new ExcelTranslationAsyResult();
        excelTranslationResult.setSuccess(true);
        excelTranslationResult.setUuid(uuid);
        excelTranslationResult.setExcelType(excelType);
        return excelTranslationResult;
    }

    public static ExcelTranslationAsyResult error(String message){
        return new ExcelTranslationAsyResult(false,message);
    }

}
