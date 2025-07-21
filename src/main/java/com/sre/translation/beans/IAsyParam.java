package com.sre.translation.beans;

import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;

/**
 * @author chen gang
 * @date 2025/4/15
 */
public interface IAsyParam {
    /**
     * 设置异步进度对象
     * @param excelProgressAsyLineBean {@link ExcelProgressAsyLineBean}
     */
    void setExcelProgressAsyLineBean(ExcelProgressAsyLineBean excelProgressAsyLineBean);
}
