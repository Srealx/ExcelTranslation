package com.sre.translation.eumn;


/**
 * @author cheng
 * @date 2024/4/26
 */
public enum ExcelProgressTypeEnum {
    /**
     * Excel 进度类型枚举
     */
    UN_START(0,"未开始"),
    START(1,"进行中"),
    END(2,"已完成"),
    ERROR(3,"失败"),
    ;
    private int code;
    private String name;

    ExcelProgressTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ExcelProgressTypeEnum find(int code){
        for (ExcelProgressTypeEnum value : ExcelProgressTypeEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}
