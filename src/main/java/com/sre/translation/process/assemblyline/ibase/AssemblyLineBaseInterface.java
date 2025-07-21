package com.sre.translation.process.assemblyline.ibase;

import com.sre.translation.beans.progress.ExcelProgressBean;
import com.sre.translation.beans.progress.ExcelProgressEndBean;
import com.sre.translation.beans.progress.ExcelProgressNextBean;

import java.util.function.Consumer;

/**
 * 行为基础接口
 * @author cheng
 * @date 2023/6/9
 */
public interface AssemblyLineBaseInterface {
    /**
     * 开始进度
     * @param progressUuid 进度uuid
     * @param fileName 文件名称
     * @param consumer consumer
     */
    default void start(String progressUuid, String fileName, Consumer<ExcelProgressBean> consumer){

    }

    /**
     * 流程结束
     * @param infoData 结束的主要数据
     * @param progressUuid 进度uuid
     * @param fileName 文件名
     * @param consumer consumer
     */
    default void finish(String infoData, String progressUuid, String fileName, Consumer<ExcelProgressEndBean> consumer){

    }

    /**
     * 进度+1
     * @param consumer consumer
     * @param fileName 文件名
     * @param progressUuid  进度uuid
     * @param totalProgress 总进度
     * @param nowProgress 当前进度
     * @param first 是否是第一次执行
     */
    default void nextProgress(Consumer<ExcelProgressNextBean> consumer, String fileName, String progressUuid, Integer totalProgress, Integer nowProgress, Boolean first){
        ExcelProgressNextBean excelProgressNextBean = new ExcelProgressNextBean(progressUuid);
        excelProgressNextBean.setProgressName(fileName);
        excelProgressNextBean.setTotalProgress(totalProgress);
        excelProgressNextBean.setNowProgress(nowProgress);
        excelProgressNextBean.setFirst(first);
        consumer.accept(excelProgressNextBean);
    }
}
