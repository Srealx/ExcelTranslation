package com.sre.translation.handle.exceltype;

import com.sre.translation.beans.ExcelTranslationBaseParam;
import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.beans.result.ExcelTranslationResult;
import com.sre.translation.eumn.ExcelExportModeEnum;
import com.sre.translation.eumn.ExcelModeEnum;
import com.sre.translation.exception.ExcelHandlerException;
import com.sre.translation.handle.export.ExcelExportModeInterface;
import com.sre.translation.template.base.IExBase;
import com.sre.translation.utils.SpringContextUtil;
import org.springframework.stereotype.Component;

/**
 * excel导出控制器
 * @author cheng
 * @date 2023/5/17
 */
@Component("excelExportHandler")
public class ExcelExportHandler implements ExcelTypeInterface{
    @Override
    public ExcelTranslationResult start(ExcelTranslationBaseParam param, ExcelModeEnum excelModeEnum) {
        ExcelTranslationExportParam excelTranslationExportParam;
        try {
            excelTranslationExportParam = (ExcelTranslationExportParam)param;
        }catch (Exception e){
            throw new ExcelHandlerException("系统异常, 导出参数类型转换失败");
        }
        IExBase template = SpringContextUtil.getBeansWithExcelConductAnnotation(IExBase.class,param.getExcelBusinessEnumCode());
        ExcelExportModeInterface service = ExcelExportModeEnum.find(excelModeEnum).getService(SpringContextUtil.getApplicationContext());
        return service.start(excelTranslationExportParam,template);
    }
}
