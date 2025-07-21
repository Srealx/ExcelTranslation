package com.sre.translation.process.handler.importResultHandler;

import com.sre.translation.beans.ExcelImportResultHandlerParam;
import com.sre.translation.template.result.dto.MsgImportResultDTO;

/**
 * @author chen gang
 * @date 2025/4/10
 */
public class ImportResultHandler implements IImportResultHandler<ExcelImportResultHandlerParam,MsgImportResultDTO>{
    private ImportResultHandler(){}

    private static ImportResultHandler importResultHandler;

    public static ImportResultHandler obtain(){
        if (importResultHandler == null){
            importResultHandler = new ImportResultHandler();
        }
        return importResultHandler;
    }


    @Override
    public MsgImportResultDTO handler(ExcelImportResultHandlerParam excelImportResultHandlerParam) {
        MsgImportResultDTO msgImportResultDTO = new MsgImportResultDTO();
        msgImportResultDTO.setErrorData(excelImportResultHandlerParam.getErrorDataMap());
        return msgImportResultDTO;
    }

}
