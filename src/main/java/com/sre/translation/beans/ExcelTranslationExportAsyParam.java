package com.sre.translation.beans;

import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * excel导出参数对象
 * @author cheng
 * @date 2023/5/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelTranslationExportAsyParam extends ExcelTranslationExportParam {
    /**
     * 本次调度uuid
     */
    private String progressUuid;
    /**
     * 进度结构
     */
    private ExcelProgressAsyLineBean lineStructure;
}
