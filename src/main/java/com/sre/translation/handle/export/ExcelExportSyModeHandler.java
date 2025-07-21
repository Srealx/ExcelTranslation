package com.sre.translation.handle.export;

import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.beans.result.ExcelTranslationResult;
import com.sre.translation.eumn.ExcelTypeEnum;
import com.sre.translation.handle.export.factory.ExAssemblyLineFactory;
import com.sre.translation.process.assemblyline.ibase.ExportAssemblyLineInterface;
import com.sre.translation.template.base.IExBase;
import com.sre.translation.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 同步导出处理器
 * @author cheng
 * @date 2023/5/17
 */
@Component("excelExportSyModeHandler")
@Slf4j
public class ExcelExportSyModeHandler implements ExcelExportModeInterface{

    @Resource
    HttpServletResponse response;
    @Override
    public ExcelTranslationResult start(ExcelTranslationExportParam param, IExBase model) {
        String fileName = model.getFileName();
        ServletOutputStream outputStream = ExcelUtil.initOutPutStream(response, fileName);
        // 构建工厂
        ExportAssemblyLineInterface assemblyLine = new ExAssemblyLineFactory().assemble(model,param,outputStream);
        try {
            assemblyLine.init();
            assemblyLine.start();
            assemblyLine.finish(null);
        }catch (Exception e){
            e.printStackTrace();
            return ExcelTranslationResult.error("导出失败, 失败原因: "+e.getMessage());
        }
        return ExcelTranslationResult.success(ExcelTypeEnum.EXPORT.getType());
    }
}
