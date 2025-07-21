package com.sre.translation.beans;

import lombok.Data;

/**
 * excel导入结果对象
 * @author chen gang
 * @date 2025/4/10
 */
@Data
public class ExcelImportResult extends ExcelResult{
    /**
     * 总数据条目
     */
    Integer totalCount;
    /**
     * 成功数量
     */
    Integer successCount;
    /**
     * 失败数量
     */
    Integer failCount;
    /**
     * 详情结果类型: {@link com.sre.translation.constant.ExcelNoticeTypeConstant}
     */
    Integer infoDataType;
    /**
     * 详情结果字符串
     */
    String infoData;
}
