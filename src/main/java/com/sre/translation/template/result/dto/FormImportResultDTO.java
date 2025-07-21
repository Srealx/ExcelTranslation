package com.sre.translation.template.result.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

/**
 *
 * @author chen gang
 * @date 2025/4/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class FormImportResultDTO extends ImportResultDTO{
    /**
     * 导入结果文件
     */
    File resultFile;

    /**
     * 结果文件的临时存储路径, 结构是publicPath + / + 文件名
     */
    String resultFilepath;
}
