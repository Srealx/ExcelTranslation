package com.sre.translation.template;

import com.sre.commonBase.beans.response.R;
import com.sre.translation.eumn.ExcelImportNoticeTypeEnum;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.exception.ExcelTemplateInitException;
import com.sre.translation.template.base.CommonImportTemplate;
import com.sre.translation.template.base.IImBase;
import com.sre.translation.template.result.dto.ImportResultDTO;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * excel导入模板-单sheet页导入
 * @author sre
 * @date 2024/7/18
 * @param <T> 导入的数据类型
 * @param <K> 请求参数
 * @param <I> 导入结束后的结果类型, 用来控制将导入的错误数据生成什样的结果, 必须继承自 ImportResultDTO
 */
public abstract class ExcelImportTemplate<T,K,I extends ImportResultDTO>  extends CommonImportTemplate<T,I> implements IImBase {

    public ExcelImportTemplate(){
        // 初始化分批处理数量(单次不超过500条)
        int count = batchCount();
        if (count<=500 && count>0){
            batchCount=count;
        }
        this.sheetName = getSheetName();
        // 导入结果类型
        ParameterizedType type = (ParameterizedType)getClass().getGenericSuperclass();
        Type typeArg = type.getActualTypeArguments()[2];
        this.noticeType = ExcelImportNoticeTypeEnum.findByResultType((Class<I>)typeArg);
        if (this.noticeType == null){
            String className = ((Class<?>) typeArg).getName();
            throw new ExcelTemplateInitException("excel模板初始化失败, 无法通过"+className+"找到对应的结果通知枚举, 请检查泛型参数");
        }
        // 表头行数
        int headRowNumber = getHeadRowNumber();
        this.sheetHeadRowMap.put(this.sheetName,headRowNumber);
    }

    /**
     * 请求数据
     */
    private K paramData;

    /**
     * sheet页名称
     */
    private String sheetName;

    @Override
    public final ExcelSheetTypeEnum getSheetType(){return ExcelSheetTypeEnum.SINGLE;}

    public K getParamData() {
        return paramData;
    }

    public void setParamData(K paramData) {
        this.paramData = paramData;
    }

    public int getBatchCount(){
        return this.batchCount;
    }

    public String getSheetName(){
        // (单sheet页固定为Sheet1) 如需修改可重写
        return "Sheet1";
    }
    /**
     * 校验请求参数
     * @param paramData 参数
     * @return R
     */
    public abstract R<Object> verifyParamData(K paramData);

    /**
     * 获取表头行数
     * @return int
     */
    protected abstract int getHeadRowNumber();

    /**
     * 读取到数据后的操作
     * @param dataList 数据
     */
    public abstract void read(List<T> dataList);

}
