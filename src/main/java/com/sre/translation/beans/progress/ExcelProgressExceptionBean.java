package com.sre.translation.beans.progress;

import lombok.Data;

/**
 * excel-进度bean
 * @author cheng
 * @date 2024/4/25
 */
@Data
public class ExcelProgressExceptionBean extends ExcelProgressBean{
    /**
     * 信息
     */
    private String msg;
}
