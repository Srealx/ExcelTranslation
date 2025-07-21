package com.sre.translation.eumn;

import com.sre.translation.process.handler.writeHandlerProducer.product.ExcelCellMergeHandlerProduct;
import com.sre.translation.process.handler.writeHandlerProducer.product.ExcelCellStyleHandlerProduct;
import com.sre.translation.process.handler.writeHandlerProducer.product.ExcelHeadStyleHandlerProduct;
import com.sre.translation.process.handler.writeHandlerProducer.ibase.WriteHandlerProductInterface;

/**
 * @author cheng
 * @date 2023/5/26
 */
public enum WriteHandlerEnum {
    /**
     * excel写入执行器生产类枚举
     */
    CELL_MERGE("合并单元格", ExcelCellMergeHandlerProduct.obtain()),
    CELL_STYLE("表格样式", ExcelCellStyleHandlerProduct.obtain()),
    HEAD_STYLE("表头样式", ExcelHeadStyleHandlerProduct.obtain()),
    ;
    private final String name;
    private final WriteHandlerProductInterface writeHandlerProductHandler;

    WriteHandlerEnum(String name, WriteHandlerProductInterface writeHandlerProductHandler) {
        this.name = name;
        this.writeHandlerProductHandler = writeHandlerProductHandler;
    }

    public String getName() {
        return name;
    }

    public WriteHandlerProductInterface getWriteHandlerProductHandler() {
        return writeHandlerProductHandler;
    }
}
