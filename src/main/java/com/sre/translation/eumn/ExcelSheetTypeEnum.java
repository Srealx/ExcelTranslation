package com.sre.translation.eumn;

import com.sre.translation.constant.ExcelSheetTypeConstant;

/**
 * @author cheng
 * @date 2023/6/9
 */
public enum ExcelSheetTypeEnum {
    /**
     * Excel sheet页类型枚举
     */
    SINGLE(ExcelSheetTypeConstant.SINGLE,"单sheet页"),
    MULTIPART(ExcelSheetTypeConstant.MULTIPART,"多sheet页"),
    ;
    private int code;
    private String name;

    ExcelSheetTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getModeName() {
        return name;
    }

    public static ExcelSheetTypeEnum find(int code){
        for (ExcelSheetTypeEnum value : ExcelSheetTypeEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}
