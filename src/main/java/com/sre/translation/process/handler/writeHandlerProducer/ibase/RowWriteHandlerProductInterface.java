package com.sre.translation.process.handler.writeHandlerProducer.ibase;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.sre.translation.beans.style.ExcelStyleParam;

/**
 * 行写入控制器生产接口
 * @author cheng
 * @date 2023/5/25
 */
public interface RowWriteHandlerProductInterface<C extends ExcelStyleParam> extends WriteHandlerProductInterface<C> {
    RowWriteHandler product(C c);
}
