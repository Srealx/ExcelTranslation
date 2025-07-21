package com.sre.translation.beans;

import lombok.Data;

import java.io.OutputStream;

/**
 * excel导出参数对象
 * @author cheng
 * @date 2023/5/17
 */
@Data
public class ExcelTranslationExportParam extends ExcelTranslationBaseParam{
    /**
     * 指定输出流
     */
    private OutputStream outputStream;
    /**
     * 文件名
     */
    private String fileName;

}
