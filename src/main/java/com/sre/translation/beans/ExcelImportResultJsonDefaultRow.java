package com.sre.translation.beans;

import lombok.Data;

import java.util.List;

/**
 * excel导入-json结果默认数据结构
 * @author chen gang
 * @date 2025/4/10
 */
@Data
public class ExcelImportResultJsonDefaultRow {
    /**
     * 行数
     */
    Integer rowNumber;
    /**
     * 字段列表
     */
    List<ExcelImportResultJsonDefaultField> fieldList;
}
