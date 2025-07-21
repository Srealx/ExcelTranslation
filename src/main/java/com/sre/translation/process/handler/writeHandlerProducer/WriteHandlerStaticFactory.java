package com.sre.translation.process.handler.writeHandlerProducer;

import com.sre.translation.beans.style.ExcelStyleParam;
import com.sre.translation.eumn.WriteHandlerEnum;
import com.sre.translation.process.handler.writeHandlerProducer.ibase.WriteHandlerProductInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * excel写入执行器提供者
 * @author cheng
 * @date 2023/5/25
 */
@Slf4j
public class WriteHandlerStaticFactory {
    private WriteHandlerStaticFactory(){}

    /**
     * 获取合并单元格控制器
     */
    public static WriteHandlerProductInterface<ExcelStyleParam> producerCellMergeHandler(){
        return WriteHandlerEnum.CELL_MERGE.getWriteHandlerProductHandler();
    }

    /**
     * 获取单元格样式控制器
     */
    public static WriteHandlerProductInterface<ExcelStyleParam> producerCellStyleHandler(){
        return WriteHandlerEnum.CELL_STYLE.getWriteHandlerProductHandler();
    }

    /**
     * 获取表头样式控制器
     */
    public static WriteHandlerProductInterface<ExcelStyleParam> producerHeadStyleHandler(){
        return WriteHandlerEnum.HEAD_STYLE.getWriteHandlerProductHandler();
    }


}
