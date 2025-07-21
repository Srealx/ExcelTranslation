package com.sre.translation.beans;

import com.sre.translation.joint.IExcelStorage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * excel基础参数对象
 * @author cheng
 * @date 2023/5/17
 */
@Data
public class ExcelTranslationBaseParam {
    /**
     * 业务code
     */
    private Long excelBusinessEnumCode;
    /**
     * {@link com.sre.translation.eumn.ExcelTypeEnum}
     */
    private Integer typeCode;
    /**
     * 业务参数
     */
    private Object paramData;

    /**
     * 文件存储接口
     */
    @JsonIgnore
    private IExcelStorage iExcelStorage;

}
