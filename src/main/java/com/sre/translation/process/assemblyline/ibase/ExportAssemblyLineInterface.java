package com.sre.translation.process.assemblyline.ibase;

import cn.hutool.core.text.StrPool;
import com.sre.translation.beans.ExcelExportResult;
import com.sre.translation.eumn.ExcelTypeEnum;
import com.github.pagehelper.util.StringUtil;
import com.sre.translation.beans.progress.ExcelProgressBean;
import com.sre.translation.beans.progress.ExcelProgressEndBean;

import java.util.function.Consumer;

/**
 * 导出-流程线抽象接口
 * @author cheng
 * @date 2023/5/17
 */
public interface ExportAssemblyLineInterface extends AssemblyLineBaseInterface{
    /**
     * 初始化
     */
    void init();

    /**
     * 开始方法
     */
    void start();

    /**
     * 结束
     * @param saveFileRootPath excel文件存储路径
     */
    void finish(String saveFileRootPath);

    /**
     * 开始进度
     * @param progressUuid 进度uuid
     * @param fileName 文件名称
     * @param consumer consumer
     */
    @Override
    default void start(String progressUuid, String fileName, Consumer<ExcelProgressBean> consumer){
        ExcelProgressBean excelProgressBean = new ExcelProgressBean(progressUuid);
        excelProgressBean.setProgressName(fileName+"导出");
        excelProgressBean.setExcelType(ExcelTypeEnum.EXPORT.getType());
        consumer.accept(excelProgressBean);
    }

    /**
     * 流程结束
     * @param saveFileRootPath 文件存储根路径
     * @param progressUuid 进度uuid
     * @param fileName 文件名
     * @param consumer consumer
     */
    @Override
    default void finish(String saveFileRootPath, String progressUuid, String fileName,  Consumer<ExcelProgressEndBean> consumer){
        if (StringUtil.isNotEmpty(saveFileRootPath)){
            // 流程结束
            ExcelProgressEndBean excelProgressBean = new ExcelProgressEndBean(progressUuid);
            excelProgressBean.setProgressName(fileName);
            ExcelExportResult result = new ExcelExportResult();
            result.setFileName(fileName);
            result.setFileRootPath(saveFileRootPath + StrPool.SLASH + fileName+".xlsx");
            excelProgressBean.setExcelExportResult(result);
            excelProgressBean.setExcelType(ExcelTypeEnum.EXPORT.getType());
            consumer.accept(excelProgressBean);
        }
    }
}
