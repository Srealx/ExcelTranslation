package com.sre.translation.process.assemblyline.ibase;

import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;

import java.io.OutputStream;

/**
 * @author chen gang
 * @date 2025/4/14
 */
public abstract class ExportAssemblyLine implements ExportAssemblyLineInterface{
    /**
     * 导出文件名
     */
    protected String fileName;
    /**
     * 任务请求参数
     */
    protected ExcelTranslationExportParam excelTranslationParam;
    /**
     * 输出流
     */
    protected OutputStream outputStream;
    /**
     * 异步流程
     */
    protected ExcelProgressAsyLineBean lineStructure = ExcelProgressAsyLineBean.build();

    public ExportAssemblyLine(ExcelTranslationExportParam excelTranslationParam,OutputStream outputStream){
        this.outputStream = outputStream;
        this.excelTranslationParam = excelTranslationParam;
    }

    public void setLineStructure(ExcelProgressAsyLineBean lineStructure){
        this.lineStructure = lineStructure;
    }
}
