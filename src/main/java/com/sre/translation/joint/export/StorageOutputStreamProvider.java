package com.sre.translation.joint.export;

import com.sre.commonBase.utils.SpringUtils;
import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.eumn.ExcelModeEnum;
import com.sre.translation.joint.IExcelStorage;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

/**
 * @author chen gang
 * @date 2025/4/18
 */
@Component("storageOutputStreamProvider")
public class StorageOutputStreamProvider implements IExportOutputStreamProvider{

    @Override
    public OutputStream getOutputStream(ExcelTranslationExportParam param, String fileName, ExcelModeEnum excelModeEnum) {
        if (excelModeEnum == ExcelModeEnum.ASY){
            IExcelStorage excelStorage = SpringUtils.getBean(IExcelStorage.class);
        }
        return null;
    }

}

