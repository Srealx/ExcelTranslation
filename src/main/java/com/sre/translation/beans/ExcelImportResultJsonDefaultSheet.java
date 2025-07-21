package com.sre.translation.beans;

import lombok.Data;

import java.util.List;

/**
 * excel导入-json结果默认数据结构
 * @author chen gang
 * @date 2025/4/10
 */
@Data
public class ExcelImportResultJsonDefaultSheet {
    /**
     * 行数
     */
    String sheetName;
    /**
     * 字段列表
     */
    List<ExcelImportResultJsonDefaultRow> rowList;
}
