package com.sre.translation.eumn;

import com.sre.translation.constant.ExcelExportTypeConstant;
import com.sre.translation.process.config.ExcelStructureConfig;
import com.sre.translation.process.config.ExcelTemplateConfig;
import com.sre.translation.process.config.ibase.ExcelConfigInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 * @date 2023/5/16
 */
public enum ExcelExportTypeEnum {
    /**
     * excel导出类型枚举
     */
    STRUCTURE(ExcelExportTypeConstant.TYPE_STRUCTURE,"构造模式", ExcelStructureConfig.obtain()),
    TEMPLATE(ExcelExportTypeConstant.TYPE_TEMPLATE,"模版模式", ExcelTemplateConfig.obtain()),
    ;
    private int type;
    private String typeName;
    private ExcelConfigInterface config;

    ExcelExportTypeEnum(int type, String typeName,ExcelConfigInterface config) {
        this.type = type;
        this.typeName = typeName;
        this.config = config;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public ExcelConfigInterface getConfig(){return config;}

    private static Map<String,String> initMap(String[] keys,String[] values){
        Map<String,String> map = new HashMap<>(16);
        for (int i = 0; i < (Math.min(keys.length, values.length)); i++) {
            map.put(keys[i],values[i]);
        }
        return map;
    }

    public ExcelExportTypeEnum find(int type){
        for (ExcelExportTypeEnum value : ExcelExportTypeEnum.values()) {
            if (value.getType() == type){
                return value;
            }
        }
        return null;
    }
}
