package com.sre.translation.process.config.ibase;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.sre.translation.beans.config.ExcelConfigParam;
import com.sre.translation.beans.style.ExcelStyleParam;
import com.sre.translation.beans.style.WriteHandlerMapValuePojo;
import com.sre.translation.template.base.CommonExportTemplate;

import java.util.List;
import java.util.Map;

/**
 * excel配置接口
 * @author cheng
 * @date 2024/4/22
 */
public interface ExcelConfigInterface {
    /**
     * 加载配置
     * @param param param
     */
    void loadConfig(ExcelConfigParam param);

    /**
     * 添加合并单元格与自定义的配置
     * @param commonExportTemplate 模板
     * @param writerBuilder 写入器
     */
    default void cellMerge(CommonExportTemplate commonExportTemplate, ExcelWriterBuilder writerBuilder){
        commonExportTemplate.getSheetNames().forEach(item->{
            Map<String, WriteHandlerMapValuePojo<ExcelStyleParam>> styleHandler = commonExportTemplate.getStyleHandler(item);
            for (Map.Entry<String, WriteHandlerMapValuePojo<ExcelStyleParam>> handler: styleHandler.entrySet()) {
                // 配置样式忽略合并单元格
                if (handler.getKey().contains("cellMerge")){
                    writerBuilder.registerWriteHandler(handler.getValue().getWriteHandlerProduct().product((handler.getValue().getMethodParam())));
                }
            }
            // 自定义写控器
            List<WriteHandler> writeHandlerList = commonExportTemplate.getWriteHandlerList(item);
            writeHandlerList.forEach(writerBuilder::registerWriteHandler);
        });
    }
}
