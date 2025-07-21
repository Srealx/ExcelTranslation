package com.sre.translation.handle.export;

import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.beans.result.ExcelTranslationResult;
import com.sre.translation.template.base.IExBase;

/**
 * excel导出模式接口
 * @author cheng
 * @date 2023/5/17
 */
public interface ExcelExportModeInterface {
    /**
     * 启动导出
     * @param param param
     * @param model template实体
     * @return result
     */
    ExcelTranslationResult start(ExcelTranslationExportParam param, IExBase model);
}
