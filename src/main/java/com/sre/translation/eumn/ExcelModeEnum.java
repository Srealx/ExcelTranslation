package com.sre.translation.eumn;

import com.sre.translation.constant.ExcelModeConstant;

/**
 * @author cheng
 * @date 2023/5/16
 */
public enum ExcelModeEnum {
    /**
     * excel任务模式枚举
     */
    SY(ExcelModeConstant.MODE_SY,"同步模式"),
    ASY(ExcelModeConstant.MODE_ASY,"异步模式"),
    ;
    private int mode;
    private String modeName;

    ExcelModeEnum(int mode, String modeName) {
        this.mode = mode;
        this.modeName = modeName;
    }

    public int getMode() {
        return mode;
    }

    public String getModeName() {
        return modeName;
    }

    public static ExcelModeEnum find(int mode){
        for (ExcelModeEnum value : ExcelModeEnum.values()) {
            if (value.getMode() == mode){
                return value;
            }
        }
        return null;
    }
}
