package com.sre.translation.eumn;

import com.sre.translation.handle.improt.ExcelImportModeInterface;
import org.springframework.context.ApplicationContext;

/**
 * @author cheng
 * @date 2024/4/25
 */
public enum ExcelImportModeEnum implements IExcelMode{
    /**
     * excel导入模式枚举
     */
    SY(ExcelModeEnum.SY,"excelImportSyModeHandler"),
    ASY(ExcelModeEnum.ASY,"excelImportAsyModeHandler"),
    ;
    private ExcelModeEnum mode;
    private String serviceName;

    ExcelImportModeEnum(ExcelModeEnum mode, String serviceName) {
        this.mode = mode;
        this.serviceName = serviceName;
    }

    @Override
    public ExcelModeEnum getMode() {
        return mode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public static ExcelImportModeEnum find(ExcelModeEnum mode){
        for (ExcelImportModeEnum value : ExcelImportModeEnum.values()) {
            if (value.getMode().equals(mode)){
                return value;
            }
        }
        return null;
    }

    public ExcelImportModeInterface getService(ApplicationContext context){
        return (ExcelImportModeInterface)context.getBean(this.serviceName);
    }
}
