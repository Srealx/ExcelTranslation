package com.sre.translation.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.OutputStream;
import java.util.Map;

/**
 * excel导入结果处理pram
 * @author chen gang
 * @date 2025/4/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelImportFileResultHandlerParam extends ExcelImportResultHandlerParam{
    /**
     * 输出流(用于生成文件)
     */
    private OutputStream outputStream;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 生成文件后的文件公开路径
     */
    private String filePublicPath;

    public ExcelImportFileResultHandlerParam(Map<String, Map<Integer,ExcelImportErrorDataResult<?>>> errorDataMap,OutputStream outputStream,String fileName ,String filePublicPath){
        this.errorDataMap = errorDataMap;
        this.outputStream = outputStream;
        this.fileName = fileName;
        this.filePublicPath = filePublicPath;
    }
}
