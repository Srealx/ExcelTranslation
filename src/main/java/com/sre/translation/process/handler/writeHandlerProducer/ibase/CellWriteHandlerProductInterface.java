package com.sre.translation.process.handler.writeHandlerProducer.ibase;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.sre.translation.beans.style.ExcelStyleParam;


/**
 * 单元格写入控制器生产接口
 * @author cheng
 * @date 2023/5/25
 */
public interface CellWriteHandlerProductInterface<C extends ExcelStyleParam> extends WriteHandlerProductInterface<C> {
    CellWriteHandler product(C c);
}
