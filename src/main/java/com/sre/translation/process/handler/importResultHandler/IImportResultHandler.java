package com.sre.translation.process.handler.importResultHandler;

import com.sre.translation.beans.ExcelImportResultHandlerParam;
import com.sre.translation.template.result.dto.ImportResultDTO;


/**
 * excel导入结果处理
 * @author chen gang
 * @date 2025/4/10
 */
public interface IImportResultHandler<P extends ExcelImportResultHandlerParam,R extends ImportResultDTO> {
    /**
     * 处理异常数据
     * @param excelImportResultHandlerParam pram
     * @return result Info
     */
     R handler(P excelImportResultHandlerParam);

}
