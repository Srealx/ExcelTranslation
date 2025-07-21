package com.sre.translation.beans.progress;

import com.sre.translation.eumn.ExcelProgressTypeEnum;
import lombok.Data;

/**
 * excel-进度bean
 * @author cheng
 * @date 2024/4/25
 */
@Data
public class ExcelProgressBean {
    /**
     * 调度uuid
     */
    private String uuid;
    /**
     * 调度名称
     */
    private String progressName;
    /**
     * 任务状态: {@link ExcelProgressTypeEnum}
     */
    private Integer status;

    /**
     * {@link com.sre.translation.eumn.ExcelTypeEnum}
     */
    private Integer excelType;

    public ExcelProgressBean(String uuid) {
        this.uuid = uuid;
        this.status=ExcelProgressTypeEnum.UN_START.getCode();
    }

    public ExcelProgressBean() {

    }

    public ExcelProgressExceptionBean toExceptionBean(String msg){
        ExcelProgressExceptionBean excelProgressExceptionBean = new ExcelProgressExceptionBean();
        excelProgressExceptionBean.setUuid(this.uuid);
        excelProgressExceptionBean.setProgressName(this.progressName);
        excelProgressExceptionBean.setStatus(ExcelProgressTypeEnum.ERROR.getCode());
        excelProgressExceptionBean.setMsg(msg);
        return excelProgressExceptionBean;
    }
}
