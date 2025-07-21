package com.sre.translation.eumn;

import com.sre.translation.handle.export.ExcelExportModeInterface;
import org.springframework.context.ApplicationContext;

/**
 * @author cheng
 * @date 2023/5/16
 */
public enum ExcelExportModeEnum implements IExcelMode{
    /**
     * excel导出模式枚举
     */
    SY(ExcelModeEnum.SY,"excelExportSyModeHandler"),
    ASY(ExcelModeEnum.ASY,"excelExportAsyModeHandler"),
    ;
    private ExcelModeEnum mode;
    private String serviceName;

    ExcelExportModeEnum(ExcelModeEnum mode, String serviceName) {
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

    public static ExcelExportModeEnum find(ExcelModeEnum mode){
        for (ExcelExportModeEnum value : ExcelExportModeEnum.values()) {
            if (value.getMode().equals(mode)){
                return value;
            }
        }
        return null;
    }

    public ExcelExportModeInterface getService(ApplicationContext context){
        return (ExcelExportModeInterface)context.getBean(this.serviceName);
    }
}
