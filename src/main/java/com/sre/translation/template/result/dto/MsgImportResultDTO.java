package com.sre.translation.template.result.dto;

import com.sre.translation.beans.ExcelImportErrorDataResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 导入结果dto-消息对象
 * @author chen gang
 * @date 2025/4/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class MsgImportResultDTO extends ImportResultDTO {

    /**
     * 异常数据列表
     */
    Map<String,Map<Integer,ExcelImportErrorDataResult<?>>> errorData;
}
