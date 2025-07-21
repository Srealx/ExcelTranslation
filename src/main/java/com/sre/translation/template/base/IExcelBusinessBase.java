package com.sre.translation.template.base;

import com.sre.translation.eumn.ExcelSheetTypeEnum;

/**
 * @author chen gang
 * @date 2025/4/9
 */
public interface IExcelBusinessBase {

    /**
     * 获取sheet页类型
     */
    ExcelSheetTypeEnum getSheetType();
}
