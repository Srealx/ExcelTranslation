package com.sre.translation.joint.export;

import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.eumn.ExcelModeEnum;

import java.io.OutputStream;

/**
 * excel导出输出流提供器
 * @author chen gang
 * @date 2025/4/18
 */
public interface IExportOutputStreamProvider {
    /**
     * 获取输出流
     * @param param {@link ExcelTranslationExportParam}
     * @param fileName 文件名
     * @param excelModeEnum {@link ExcelModeEnum}
     * @return {@link OutputStream}
     */
    OutputStream getOutputStream(ExcelTranslationExportParam param, String fileName ,ExcelModeEnum excelModeEnum);
}
