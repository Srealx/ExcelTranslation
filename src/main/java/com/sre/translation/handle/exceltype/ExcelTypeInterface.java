package com.sre.translation.handle.exceltype;

import com.sre.translation.beans.ExcelTranslationBaseParam;
import com.sre.translation.beans.result.ExcelTranslationResult;
import com.sre.translation.eumn.ExcelModeEnum;

/**
 * excel任务接口
 * @author cheng
 * @date 2023/5/17
 */
public interface ExcelTypeInterface {
    /**
     * 开启任务
     * @param param param
     * @param excelModeEnum excel模式
     * @return result
     */
    ExcelTranslationResult start(ExcelTranslationBaseParam param, ExcelModeEnum excelModeEnum);
}
