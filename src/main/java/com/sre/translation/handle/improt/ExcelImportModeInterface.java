package com.sre.translation.handle.improt;

import com.sre.translation.beans.ExcelTranslationImportParam;
import com.sre.translation.beans.result.ExcelTranslationResult;
import com.sre.translation.template.base.IImBase;

/**
 * excel导入模式接口
 * @author cheng
 * @date 2023/5/17
 */
public interface ExcelImportModeInterface {
    /**
     * 启动导出
     * @param param param
     * @param model template实体
     * @return result
     */
    ExcelTranslationResult start(ExcelTranslationImportParam param, IImBase model);
}
