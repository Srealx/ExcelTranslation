package com.sre.translation.handle.improt;

import com.sre.translation.beans.ExcelImportResult;
import com.sre.translation.beans.ExcelTranslationImportParam;
import com.sre.translation.beans.result.ExcelTranslationImportResult;
import com.sre.translation.eumn.ExcelTypeEnum;
import com.sre.translation.handle.export.factory.ImAssemblyLineFactory;
import com.sre.translation.process.assemblyline.ibase.ImportAssemblyLineInterface;
import com.sre.translation.template.base.IImBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 同步导入处理器
 * @author cheng
 * @date 2023/5/17
 */
@Component("excelImportSyModeHandler")
@Slf4j
public class ExcelImportSyModeHandler implements ExcelImportModeInterface {

    @Override
    public ExcelTranslationImportResult start(ExcelTranslationImportParam param, IImBase model) {
        // 转真实模版
        ImportAssemblyLineInterface importLine = new ImAssemblyLineFactory(param.getFileInputStream(), param.getFileName()).assemble(model, param);
        ExcelImportResult excelImportResult;
        try {
            importLine.init();
            importLine.start();
            excelImportResult = importLine.finish();
        }catch (Exception e){
            e.printStackTrace();
            return ExcelTranslationImportResult.error("导入失败, 失败原因: "+e.getMessage());
        }
        return ExcelTranslationImportResult.success(excelImportResult, ExcelTypeEnum.IMPORT.getType());
    }
}
