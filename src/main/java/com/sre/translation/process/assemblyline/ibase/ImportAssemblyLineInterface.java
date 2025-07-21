package com.sre.translation.process.assemblyline.ibase;

import com.alibaba.fastjson.JSON;
import com.sre.translation.beans.ExcelImportResult;
import com.sre.translation.beans.progress.ExcelProgressBean;
import com.sre.translation.beans.progress.ExcelProgressEndBean;
import com.sre.translation.eumn.ExcelTypeEnum;

import java.util.function.Consumer;

/**
 * 导入-流程线抽象接口
 * @author cheng
 * @date 2023/5/17
 */
public interface ImportAssemblyLineInterface extends AssemblyLineBaseInterface{
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
     */
    ExcelImportResult finish();

    /**
     * 开始进度
     * @param progressUuid 进度uuid
     * @param fileName 文件名称
     * @param consumer 异步执行对象
     */
    @Override
    default void start(String progressUuid, String fileName, Consumer<ExcelProgressBean> consumer){
        ExcelProgressBean excelProgressBean = new ExcelProgressBean(progressUuid);
        excelProgressBean.setProgressName(fileName+"导入");
        excelProgressBean.setExcelType(ExcelTypeEnum.EXPORT.getType());
        consumer.accept(excelProgressBean);
    }

    /**
     * 流程结束
     * @param infoData 导入结果数据
     * @param progressUuid 进度uuid
     * @param fileName 文件名
     * @param consumer consumer
     */
    @Override
    default void finish(String infoData, String progressUuid, String fileName,  Consumer<ExcelProgressEndBean> consumer){
        // 流程结束
        ExcelProgressEndBean excelProgressBean = new ExcelProgressEndBean(progressUuid);
        excelProgressBean.setProgressName(fileName);
        excelProgressBean.setExcelImportResult(JSON.parseObject(infoData,ExcelImportResult.class));
        excelProgressBean.setExcelType(ExcelTypeEnum.IMPORT.getType());
        consumer.accept(excelProgressBean);
    }
}
