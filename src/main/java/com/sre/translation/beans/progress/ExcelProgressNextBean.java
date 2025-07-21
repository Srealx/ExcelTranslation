package com.sre.translation.beans.progress;

import com.sre.translation.eumn.ExcelProgressTypeEnum;
import lombok.Data;

/**
 * excel-进度bean
 * @author cheng
 * @date 2024/4/25
 */
@Data
public class ExcelProgressNextBean extends ExcelProgressBean{
    /**
     * 总进度数
     */
    private Integer totalProgress;
    /**
     * 当前进度数
     */
    private Integer nowProgress;
    /**
     * 是否是第一次进度调度
     */
    private Boolean first;

    public ExcelProgressNextBean(String uuid){
        super(uuid);
        setStatus(ExcelProgressTypeEnum.START.getCode());
    }

    public ExcelProgressNextBean(){

    }
}
