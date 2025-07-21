package com.sre.translation.handle.asy;

import com.sre.translation.beans.progress.ExcelProgressBean;
import com.sre.translation.beans.progress.ExcelProgressEndBean;
import com.sre.translation.beans.progress.ExcelProgressExceptionBean;
import com.sre.translation.beans.progress.ExcelProgressNextBean;

/**
 * excel导出-异步模式流程结构
 * @author cheng
 * @date 2024/4/25
 */
public interface ExcelProgressAsyLineStructure {
    /**
     * 开始函数
     */
    void whenStart(ExcelProgressBean excelProgressBean);
    /**
     * 进度函数
     */
    void whenNextProgress(ExcelProgressNextBean excelProgressNextBean);
    /**
     * 结束函数
     */
    void whenEnd(ExcelProgressEndBean excelProgressEndBean);
    /**
     * 异常函数
     */
    void whenException(ExcelProgressExceptionBean excelProgressExceptionBean);
}
