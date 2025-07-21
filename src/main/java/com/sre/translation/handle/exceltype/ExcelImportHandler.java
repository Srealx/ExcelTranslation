package com.sre.translation.handle.exceltype;

import com.sre.translation.beans.ExcelTranslationBaseParam;
import com.sre.translation.beans.ExcelTranslationImportParam;
import com.sre.translation.beans.result.ExcelTranslationResult;
import com.sre.translation.eumn.ExcelImportModeEnum;
import com.sre.translation.eumn.ExcelModeEnum;
import com.sre.translation.exception.ExcelHandlerException;
import com.sre.translation.handle.improt.ExcelImportModeInterface;
import com.sre.translation.template.base.IImBase;
import com.sre.translation.utils.SpringContextUtil;
import org.springframework.stereotype.Component;

/**
 * excel导入控制器
 * @author cheng
 * @date 2023/5/17
 */
@Component("excelImportHandler")
public class ExcelImportHandler implements ExcelTypeInterface{
    @Override
    public ExcelTranslationResult start(ExcelTranslationBaseParam param,ExcelModeEnum excelModeEnum) {
        ExcelTranslationImportParam excelTranslationImportParam;
        try {
            excelTranslationImportParam = (ExcelTranslationImportParam)param;
        }catch (Exception e){
            throw new ExcelHandlerException("系统异常, 导出参数类型转换失败");
        }
        IImBase template = SpringContextUtil.getBeansWithExcelConductAnnotation(IImBase.class,param.getExcelBusinessEnumCode());
        ExcelImportModeInterface service = ExcelImportModeEnum.find(excelModeEnum).getService(SpringContextUtil.getApplicationContext());
        return service.start(excelTranslationImportParam,template);
    }
}
