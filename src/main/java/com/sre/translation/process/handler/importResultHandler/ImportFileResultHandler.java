package com.sre.translation.process.handler.importResultHandler;

import com.sre.translation.beans.ExcelImportFileResultHandlerParam;
import com.sre.translation.template.result.dto.FormImportResultDTO;

/**
 * @author chen gang
 * @date 2025/4/10
 */
public class ImportFileResultHandler implements IImportResultHandler<ExcelImportFileResultHandlerParam,FormImportResultDTO>{
    private ImportFileResultHandler(){}

    private static ImportFileResultHandler importFileResultHandler;

    public static ImportFileResultHandler obtain(){
        if (importFileResultHandler == null){
            importFileResultHandler = new ImportFileResultHandler();
        }
        return importFileResultHandler;
    }


    @Override
    public FormImportResultDTO handler(ExcelImportFileResultHandlerParam excelImportResultHandlerParam) {
        // TODO 生成异常excel文件

        return null;
    }
}
