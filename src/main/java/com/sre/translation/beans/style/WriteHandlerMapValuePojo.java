package com.sre.translation.beans.style;

import com.sre.translation.process.handler.writeHandlerProducer.ibase.WriteHandlerProductInterface;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 写入执行器map value 实体类
 * @author cheng
 * @date 2023/5/25
 */
@Data
@AllArgsConstructor
public class WriteHandlerMapValuePojo<P extends ExcelStyleParam> {
    private ExcelStyleParam methodParam;
    private WriteHandlerProductInterface<P> writeHandlerProduct;
}
