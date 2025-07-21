package com.sre.translation.beans.config;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.template.base.CommonExportTemplate;
import lombok.Data;

import java.util.Map;

/**
 * excel配置param
 * @author cheng
 * @date 2024/4/22
 */
@Data
public class ExcelConfigParam {
    private ExcelSheetTypeEnum sheetTypeEnum;
    private ExcelWriterBuilder writerBuilder;
    private Map<String,WriteSheet> sheet;
    private CommonExportTemplate commonExportTemplate;


    public ExcelConfigParam(ExcelSheetTypeEnum sheetTypeEnum,
                            CommonExportTemplate commonExportTemplate,
                            ExcelWriterBuilder writerBuilder,
                            Map<String,WriteSheet> sheet){
        this.sheetTypeEnum = sheetTypeEnum;
        this.commonExportTemplate = commonExportTemplate;
        this.writerBuilder = writerBuilder;
        this.sheet = sheet;

    }
}
