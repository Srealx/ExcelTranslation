package com.sre.translation.handle.export;

import com.sre.commonBase.exception.CommonException;
import com.sre.translation.beans.ExcelTranslationExportAsyParam;
import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.beans.result.ExcelTranslationAsyResult;
import com.sre.translation.beans.progress.ExcelProgressBean;
import com.sre.translation.eumn.ExcelTypeEnum;
import com.sre.translation.exception.ExcelTemplateInitException;
import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;
import com.sre.translation.handle.export.factory.ExAssemblyLineFactory;
import com.sre.translation.joint.IExcelStorage;
import com.sre.translation.process.assemblyline.ibase.ExportAssemblyLineInterface;
import com.sre.translation.template.base.IExBase;
import com.sre.translation.utils.UuidUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 异步导出处理器
 * @author cheng
 * @date 2023/5/17
 */
@Component("excelExportAsyModeHandler")
public class ExcelExportAsyModeHandler implements ExcelExportModeInterface{

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
    public ExcelTranslationAsyResult start(ExcelTranslationExportParam param, IExBase model) {
        ExcelTranslationExportAsyParam asyParam = (ExcelTranslationExportAsyParam)param;
        ExcelProgressAsyLineBean lineStructure = asyParam.getLineStructure();
        // 生成uuid
        String uuid = UuidUtils.genFileUUid();
        ExcelProgressBean excelProgressBean = new ExcelProgressBean(uuid);
        asyParam.setProgressUuid(uuid);
        // 创建outputStream
        // 构造输出到存储位置的 outputStream
        // 从 param 或者 spring中获取 outputStream的提供者
        IExcelStorage storageBean = asyParam.getIExcelStorage();
        if (storageBean == null){
            throw new ExcelTemplateInitException("创建excel导出模板失败, 未找到 IExcelStorage 的实例, 无法开启异步导出任务");
        }
        String storagePublicPath = storageBean.getStoragePublicPath();
        File file = new File(storagePublicPath);
        if (Boolean.FALSE.equals(file.exists())){
            throw new ExcelTemplateInitException("创建excel导出模板失败, "+storagePublicPath+"  没有该文件夹");
        }
        String fileName = model.getFileName()+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        OutputStream outputStream = storageBean.getOutputStream(fileName);
        if (outputStream == null){
            throw new ExcelTemplateInitException("创建excel导出模板失败, outputStream 获取为空, 请检查 IExcelStorage 的实例");
        }
        // 构建工厂
        ExportAssemblyLineInterface assemblyLine = new ExAssemblyLineFactory().setExcelProgressAsyLineBean(lineStructure).assemble(model,asyParam,outputStream);
        // 异步启动
        threadPoolExecutor.execute(()-> {
            try {
                assemblyLine.init();
                assemblyLine.start();
                assemblyLine.finish(storagePublicPath);
            } catch (CommonException e) {
                e.printStackTrace();
                lineStructure.runException(excelProgressBean.toExceptionBean(e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
                lineStructure.runException(excelProgressBean.toExceptionBean("导出失败,系统异常"));
            }
        });
        return ExcelTranslationAsyResult.success(uuid, ExcelTypeEnum.EXPORT.getType());
    }
}
