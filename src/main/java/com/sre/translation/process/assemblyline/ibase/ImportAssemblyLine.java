package com.sre.translation.process.assemblyline.ibase;

import com.sre.translation.beans.ExcelImportResultHandlerParam;
import com.sre.translation.beans.ExcelTranslationImportParam;
import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;

import java.io.InputStream;
import java.util.function.Function;

/**
 * @author chen gang
 * @date 2025/4/14
 */
public abstract class ImportAssemblyLine implements ImportAssemblyLineInterface{

    public ImportAssemblyLine(InputStream fileInputStream,InputStream fileInputStream2,String fileName,ExcelTranslationImportParam excelTranslationImportParam){
        this.fileInputStream = fileInputStream;
        this.fileInputStream2 = fileInputStream2;
        this.fileName = fileName;
        this.excelTranslationImportParam = excelTranslationImportParam;
    }

    /**
     * 导入文件
     */
    protected InputStream fileInputStream;
    protected InputStream fileInputStream2;
    protected String fileName;
    /**
     * 基础参数
     */
    protected ExcelTranslationImportParam excelTranslationImportParam;
    /**
     * 异步流程
     */
    protected ExcelProgressAsyLineBean lineStructure = ExcelProgressAsyLineBean.build();
    /**
     * 使用errorData生成导入结果前， 调用方可前置处理对象，如添加额外的参数
     */
    protected Function<ExcelImportResultHandlerParam, ? extends ExcelImportResultHandlerParam> resultHandlerParamProgress = k->k;


    public void setResultHandlerParamProgress(Function<ExcelImportResultHandlerParam, ? extends ExcelImportResultHandlerParam> resultHandlerParamProgress){
        this.resultHandlerParamProgress = resultHandlerParamProgress;
    }

    public void setLineStructure(ExcelProgressAsyLineBean excelProgressAsyLineBean){
        this.lineStructure = excelProgressAsyLineBean;
    }
}
