package com.sre.translation.beans.style;

import lombok.Data;

import java.util.Set;

/**
 * excel合并单元格param
 * @author cheng
 * @date 2023/5/25
 */
@Data
public class ExcelMergeParam extends ExcelStyleParam{
    private Set<int[]> cellMerge;
}
