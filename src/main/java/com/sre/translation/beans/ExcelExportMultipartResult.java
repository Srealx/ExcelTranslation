package com.sre.translation.beans;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * excel导出多sheet页对象
 * @author cheng
 * @date 2023/5/17
 */
@Data
@ToString
public class ExcelExportMultipartResult {
    protected String sheetName;
    protected List<?> dataList;
}
