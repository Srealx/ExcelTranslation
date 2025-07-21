package com.sre.translation.beans;

import lombok.Data;

/**
 * excel导入-json结果默认数据结构
 * @author chen gang
 * @date 2025/4/10
 */
@Data
public class ExcelImportResultJsonDefaultField {
    /**
     * 字段名
     */
    String filedName;
    /**
     * 消息
     */
    String message;
}
