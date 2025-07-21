package com.sre.translation.beans;

import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * excel异步导入参数对象
 * @author cheng
 * @date 2023/5/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelTranslationImportAsyParam extends ExcelTranslationImportParam{
    /**
     * 本次调度uuid
     */
    private String progressUuid;
    /**
     * 进度结构
     */
    private ExcelProgressAsyLineBean lineStructure;

}
