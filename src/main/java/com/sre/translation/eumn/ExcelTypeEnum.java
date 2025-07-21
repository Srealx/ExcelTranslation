package com.sre.translation.eumn;

import com.sre.translation.constant.ExcelTypeConstant;
import com.sre.translation.handle.exceltype.ExcelTypeInterface;
import org.springframework.context.ApplicationContext;

/**
 * @author cheng
 * @date 2023/5/17
 */
public enum ExcelTypeEnum {
    /**
     * excel任务类型枚举
     */
    EXPORT(ExcelTypeConstant.EXPORT,"excel导出","excelExportHandler"),
    IMPORT(ExcelTypeConstant.IMPORT,"excel导入","excelImportHandler"),
    ;
    private int type;
    private String typeName;
    private String serviceName;

    ExcelTypeEnum(int type, String typeName,String serviceName) {
        this.type = type;
        this.typeName = typeName;
        this.serviceName = serviceName;
    }

    public int getType() {
        return this.type;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getServiceName(){return this.serviceName;}

    public static ExcelTypeEnum find(int type){
        for (ExcelTypeEnum value : ExcelTypeEnum.values()) {
            if (value.getType() == type){
                return value;
            }
        }
        return null;
    }

    public ExcelTypeInterface getService(ApplicationContext context){
        return (ExcelTypeInterface)context.getBean(this.serviceName);
    }
}
