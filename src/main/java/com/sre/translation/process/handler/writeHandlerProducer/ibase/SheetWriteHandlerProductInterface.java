package com.sre.translation.process.handler.writeHandlerProducer.ibase;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.sre.translation.beans.style.ExcelStyleParam;

/**
 * sheet页写入控制器生产接口
 * @author cheng
 * @date 2023/5/25
 */
public interface SheetWriteHandlerProductInterface<C extends ExcelStyleParam> extends WriteHandlerProductInterface<C> {
    SheetWriteHandler product(C c);
}