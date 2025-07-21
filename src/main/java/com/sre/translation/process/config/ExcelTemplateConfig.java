package com.sre.translation.process.config;

import com.github.pagehelper.util.StringUtil;
import com.sre.translation.exception.ExcelHandlerException;
import com.sre.translation.process.config.ibase.ExcelConfigInterface;
import com.sre.translation.beans.config.ExcelConfigParam;

import java.io.InputStream;

/**
 * excel-构造模式配置
 * @author cheng
 * @date 2024/4/22
 */
public class ExcelTemplateConfig implements ExcelConfigInterface {
    /**
     * 单例模式
     */
    private ExcelTemplateConfig(){}
    private static ExcelTemplateConfig excelTemplateConfig;
    public static synchronized ExcelTemplateConfig obtain(){
        if (excelTemplateConfig == null){
            excelTemplateConfig = new ExcelTemplateConfig();
        }
        return excelTemplateConfig;
    }

    @Override
    public void loadConfig(ExcelConfigParam param) {
        InputStream template = param.getCommonExportTemplate().getTemplate();
        if (template==null){
            throw new ExcelHandlerException("系统异常: 导出模板输入流为空");
        }
        param.getWriterBuilder().withTemplate(template);
        // 添加合并单元格
        cellMerge(param.getCommonExportTemplate(),param.getWriterBuilder());
    }
}
