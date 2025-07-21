package com.sre.translation.process.handler.writeHandlerProducer.ibase;

import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.sre.translation.beans.style.ExcelStyleParam;

/**
 * 单元格写入控制器生产接口
 * @author cheng
 * @date 2023/5/25
 */
public interface TableWriteHandlerProductInterface<C extends ExcelStyleParam> extends WriteHandlerProductInterface<C> {
    WorkbookWriteHandler Product(C c);
}
