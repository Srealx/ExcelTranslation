package com.sre.translation.handle.improt;

import com.sre.commonBase.exception.CommonException;
import com.sre.translation.beans.ExcelTranslationImportAsyParam;
import com.sre.translation.beans.ExcelTranslationImportParam;
import com.sre.translation.beans.progress.ExcelProgressBean;
import com.sre.translation.beans.result.ExcelTranslationAsyResult;
import com.sre.translation.eumn.ExcelTypeEnum;
import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;
import com.sre.translation.handle.export.factory.ImAssemblyLineFactory;
import com.sre.translation.process.assemblyline.ibase.ImportAssemblyLineInterface;
import com.sre.translation.template.base.IImBase;
import com.sre.translation.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步导入处理器
 * @author cheng
 * @date 2023/5/17
 */
@Component("excelImportAsyModeHandler")
@Slf4j
public class ExcelImportAsyModeHandler implements ExcelImportModeInterface {
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            3,
            5,
            30L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(50),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    @Override
    public ExcelTranslationAsyResult start(ExcelTranslationImportParam param, IImBase model) {
        ExcelTranslationImportAsyParam asyParam = (ExcelTranslationImportAsyParam) param;
        ExcelProgressAsyLineBean lineStructure = asyParam.getLineStructure();
        // 生成uuid
        String uuid = UuidUtils.genFileUUid();
        ExcelProgressBean excelProgressBean = new ExcelProgressBean(uuid);
        asyParam.setProgressUuid(uuid);
        // 转真实模版
        ImportAssemblyLineInterface importLine = new ImAssemblyLineFactory(param.getFileInputStream(), param.getFileName())
                .setExcelProgressAsyLineBean(lineStructure)
                .assemble(model, asyParam);
        // 异步启动
        threadPoolExecutor.execute(()-> {
            try {
                importLine.init();
                importLine.start();
                importLine.finish();
            } catch (CommonException e) {
                e.printStackTrace();
                lineStructure.runException(excelProgressBean.toExceptionBean(e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
                lineStructure.runException(excelProgressBean.toExceptionBean("导出失败,系统异常"));
            }
        });
        return ExcelTranslationAsyResult.success(uuid, ExcelTypeEnum.IMPORT.getType());
    }
}
