package com.sre.translation.eumn;

import com.sre.translation.constant.ExcelNoticeTypeConstant;
import com.sre.translation.process.handler.importResultHandler.IImportResultHandler;
import com.sre.translation.process.handler.importResultHandler.ImportFileResultHandler;
import com.sre.translation.process.handler.importResultHandler.ImportResultHandler;
import com.sre.translation.template.result.dto.FormImportResultDTO;
import com.sre.translation.template.result.dto.ImportResultDTO;
import com.sre.translation.template.result.dto.MsgImportResultDTO;

/**
 * @author cheng
 * @date 2023/5/16
 */
public enum ExcelImportNoticeTypeEnum {
    /**
     * excel导入结果类型
     */
    MSG(ExcelNoticeTypeConstant.TYPE_MSG,"推送消息", MsgImportResultDTO.class,ImportResultHandler.obtain()),
    FROM(ExcelNoticeTypeConstant.TYPE_FROM,"导出表格",FormImportResultDTO.class,ImportFileResultHandler.obtain()),
    ;
    private final int type;
    private final String typeName;
    private final Class<? extends ImportResultDTO> resultClass;
    private final IImportResultHandler importResultHandler;


    ExcelImportNoticeTypeEnum(int type, String modeName,Class<? extends ImportResultDTO> resultClass,IImportResultHandler importResultHandler) {
        this.type = type;
        this.typeName = modeName;
        this.resultClass = resultClass;
        this.importResultHandler = importResultHandler;
    }

    public static <I extends ImportResultDTO> ExcelImportNoticeTypeEnum findByResultType(Class<I> typeArg) {
        for (ExcelImportNoticeTypeEnum value : ExcelImportNoticeTypeEnum.values()) {
            if (value.getResultClass().isAssignableFrom(typeArg)){
                return value;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public IImportResultHandler getImportResultHandler() {
        return importResultHandler;
    }

    public Class<? extends ImportResultDTO> getResultClass() {
        return resultClass;
    }

    public static ExcelImportNoticeTypeEnum find(int type){
        for (ExcelImportNoticeTypeEnum value : ExcelImportNoticeTypeEnum.values()) {
            if (value.getType() == type){
                return value;
            }
        }
        return null;
    }
}
