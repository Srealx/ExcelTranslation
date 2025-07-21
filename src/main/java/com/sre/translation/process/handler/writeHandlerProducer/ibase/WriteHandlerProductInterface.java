package com.sre.translation.process.handler.writeHandlerProducer.ibase;

import com.alibaba.excel.write.handler.WriteHandler;
import com.sre.translation.beans.style.ExcelStyleParam;

/**
 * 写入控制器生产接口
 * @author cheng
 * @date 2023/5/25
 */
public interface WriteHandlerProductInterface<C extends ExcelStyleParam>{
    WriteHandler product(C c);
}
