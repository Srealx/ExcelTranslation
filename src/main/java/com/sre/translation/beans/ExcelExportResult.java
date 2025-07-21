package com.sre.translation.beans;

import lombok.Data;

/**
 * excel导入结果对象
 * @author chen gang
 * @date 2025/4/10
 */
@Data
public class ExcelExportResult extends ExcelResult{
    /**
     * 总数据条目
     */
    private String fileName;
    /**
     * 文件存储路径
     */
    private String fileRootPath;
}
