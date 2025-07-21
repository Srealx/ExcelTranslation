package com.sre.translation.handle.export.factory;

import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;

/**
 * @author chen gang
 * @date 2025/4/14
 */
public class AssemblyLineFactory {

    protected ExcelProgressAsyLineBean excelProgressAsyLineBean;

    public AssemblyLineFactory setExcelProgressAsyLineBean(ExcelProgressAsyLineBean excelProgressAsyLineBean){
        this.excelProgressAsyLineBean = excelProgressAsyLineBean;
        return this;
    }
}
