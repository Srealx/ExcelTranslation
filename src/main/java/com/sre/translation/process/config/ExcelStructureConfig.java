package com.sre.translation.process.config;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sre.translation.beans.style.ExcelStyleParam;
import com.sre.translation.beans.style.WriteHandlerMapValuePojo;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.process.config.ibase.ExcelConfigInterface;
import com.sre.translation.beans.config.ExcelConfigParam;
import com.sre.translation.template.ExcelExportTemplate;
import com.sre.translation.template.base.CommonExportTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * excel-构造模式配置
 * @author cheng
 * @date 2024/4/22
 */
public class ExcelStructureConfig implements ExcelConfigInterface {
    /**
     * 单例模式
     */
    private ExcelStructureConfig(){}
    private static ExcelStructureConfig excelStructureConfig;
    public static synchronized ExcelStructureConfig obtain(){
        if (excelStructureConfig == null){
            excelStructureConfig = new ExcelStructureConfig();
        }
        return excelStructureConfig;
    }

    @Override
    public void loadConfig(ExcelConfigParam param) {
        configHead(param.getCommonExportTemplate(),param.getSheet(),param.getWriterBuilder(),param.getSheetTypeEnum());
        configStyle(param.getCommonExportTemplate(),param.getWriterBuilder());
        cellMerge(param.getCommonExportTemplate(),param.getWriterBuilder());
    }


    /**
     * 配置表头
     */
    private void configHead(CommonExportTemplate excelExportTemplate, Map<String,WriteSheet> sheetMap, ExcelWriterBuilder writerBuilder, ExcelSheetTypeEnum sheetTypeEnum){
        excelExportTemplate.getSheetNames().forEach(item->{
            boolean needHead = excelExportTemplate.isNeedHead(item);
            WriteSheet sheet = sheetMap.get(item);
            sheet.setNeedHead(needHead);
            if (needHead){
                List<List<String>> head = excelExportTemplate.getHead(item);
                if (!CollectionUtils.isEmpty(head)){
                    sheet.setHead(head);
                }else if (sheetTypeEnum.equals(ExcelSheetTypeEnum.SINGLE)
                          && excelExportTemplate instanceof ExcelExportTemplate){
                    // 单sheet页下可以使用单导出对象的注解配置
                    writerBuilder.head(((ExcelExportTemplate)excelExportTemplate).getWriteDataClass());
                }
            }
        });
    }

    /**
     * 配置excel样式
     */
    private void configStyle(CommonExportTemplate commonExportTemplate,ExcelWriterBuilder writerBuilder){
        commonExportTemplate.getSheetNames().forEach(item->{
            Map<String, WriteHandlerMapValuePojo<ExcelStyleParam>> styleHandler = commonExportTemplate.getStyleHandler(item);
            for (Map.Entry<String, WriteHandlerMapValuePojo<ExcelStyleParam>> handler: styleHandler.entrySet()) {
                // 配置样式忽略合并单元格
                if (handler.getKey().contains("cellMerge")){
                    continue;
                }
                writerBuilder.registerWriteHandler(handler.getValue().getWriteHandlerProduct().product((handler.getValue().getMethodParam())));
            }
        });
    }
}
