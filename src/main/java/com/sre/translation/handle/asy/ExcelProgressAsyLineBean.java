package com.sre.translation.handle.asy;

import com.sre.translation.beans.progress.ExcelProgressBean;
import com.sre.translation.beans.progress.ExcelProgressEndBean;
import com.sre.translation.beans.progress.ExcelProgressExceptionBean;
import com.sre.translation.beans.progress.ExcelProgressNextBean;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * excel任务-异步模式流程bean
 * @author cheng
 * @date 2024/4/25
 */
@Slf4j
public class ExcelProgressAsyLineBean {
    /**
     * 开始函数
     */
    public Consumer<ExcelProgressBean> whenStart;
    /**
     * 进度函数
     */
    public Consumer<ExcelProgressNextBean> whenNextProgress;
    /**
     * 结束函数
     */
    public Consumer<ExcelProgressEndBean> whenEnd;
    /**
     * 异常函数
     */
    public Consumer<ExcelProgressExceptionBean> whenException;

    private ExcelProgressAsyLineBean(Consumer<ExcelProgressBean> whenStart,
                                     Consumer<ExcelProgressNextBean> whenNextProgress,
                                     Consumer<ExcelProgressEndBean> whenEnd,
                                     Consumer<ExcelProgressExceptionBean> whenException) {
        this.whenStart = whenStart;
        this.whenNextProgress = whenNextProgress;
        this.whenEnd = whenEnd;
        this.whenException = whenException;
    }

    public static ExcelProgressAsyLineBean build(){
        return new ExcelProgressAsyLineBean(item->log.info("progress: start"), item->log.info("progress: next"), item->log.info("progress: end"), item->log.info("progress: exception: {}",item.getMsg()));
    }

    public ExcelProgressAsyLineBean whenStart(Consumer<ExcelProgressBean> whenStart){
        this.whenStart = whenStart;
        return this;
    }

    public ExcelProgressAsyLineBean whenNextProgress(Consumer<ExcelProgressNextBean> whenNextProgress){
        this.whenNextProgress = whenNextProgress;
        return this;
    }

    public ExcelProgressAsyLineBean whenEnd(Consumer<ExcelProgressEndBean> whenEnd){
        this.whenEnd = whenEnd;
        return this;
    }

    public ExcelProgressAsyLineBean whenException(Consumer<ExcelProgressExceptionBean> whenException){
        this.whenException = whenException;
        return this;
    }

    public void runStart(ExcelProgressBean excelProgressBean){
        this.whenStart.accept(excelProgressBean);
    }

    public void runNextProgress(ExcelProgressNextBean excelProgressBean){
        this.whenNextProgress.accept(excelProgressBean);
    }

    public void runEnd(ExcelProgressEndBean excelProgressBean){
        this.whenEnd.accept(excelProgressBean);
    }

    public void runException(ExcelProgressExceptionBean excelProgressBean){
        this.whenException.accept(excelProgressBean);
    }

}
