package com.sre.translation.beans.progress;

import com.sre.translation.beans.ExcelExportResult;
import com.sre.translation.beans.ExcelImportResult;
import com.sre.translation.eumn.ExcelProgressTypeEnum;
import lombok.Data;

/**
 * excel-进度bean
 * @author cheng
 * @date 2024/4/25
 */
@Data
public class ExcelProgressEndBean extends ExcelProgressNextBean{
    /**
     * (导出) 结果结构
     */
    private ExcelExportResult excelExportResult;

    /**
     * (导入) 结果结构
     */
    private ExcelImportResult excelImportResult;


    public ExcelProgressEndBean(){
        setStatus(ExcelProgressTypeEnum.END.getCode());
    }

    public ExcelProgressEndBean(String uuid){
        super(uuid);
        setStatus(ExcelProgressTypeEnum.END.getCode());
    }

}
