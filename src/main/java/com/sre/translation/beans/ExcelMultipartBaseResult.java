package com.sre.translation.beans;

import lombok.Data;

/**
 * excel多sheet页面count
 * @author cheng
 * @date 2023/5/23
 */
@Data
public class ExcelMultipartBaseResult {
    private String sheetName;
    private long count;
}
