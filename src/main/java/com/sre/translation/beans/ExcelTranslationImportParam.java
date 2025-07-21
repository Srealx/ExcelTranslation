package com.sre.translation.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;

/**
 * excel导入参数对象
 * @author cheng
 * @date 2023/5/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelTranslationImportParam extends ExcelTranslationBaseParam{
    /**
     * 文件输入流
     */
    private InputStream fileInputStream;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件格式后缀
     */
    private String fileExtend;
}
