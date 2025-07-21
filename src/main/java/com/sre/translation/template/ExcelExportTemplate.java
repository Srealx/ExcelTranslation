package com.sre.translation.template;

import com.alibaba.excel.write.handler.WriteHandler;
import com.sre.commonBase.beans.response.R;
import com.sre.translation.beans.style.*;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.process.handler.writeHandlerProducer.WriteHandlerStaticFactory;
import com.sre.translation.template.base.CommonExportTemplate;
import com.sre.translation.template.base.IExBase;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

/**
 * excel导出模版
 * @author sre
 * @date 2023/5/15
 *
 * @param <T>     返回参数的泛型 (当导出动态列数据时, 自定义表头并 设T 为  List<String>,  以设置queryData的返回值为二维集合, 外部list代表list.size行, 内部的每一个list代表一行)
 * @param <K>     接口请求参数, 用来查询列表与做验证
 */
@Slf4j
public abstract class ExcelExportTemplate<T,K> extends CommonExportTemplate implements IExBase {
    public ExcelExportTemplate(){
        String sheetName = getSheetNames().get(0);
        // 初始化单sheet页的collection属性
        initStyleCollection(sheetName);
        // 初始化合并单元格执行器对象
        // 初始化合并单元格执行器
        ExcelMergeParam excelMergeParam = new ExcelMergeParam();
        excelMergeParam.setSheetName(sheetName);
        excelMergeParam.setCellMerge(this.getCellMerge(sheetName));
        WriteHandlerMapValuePojo<ExcelStyleParam> writeHandlerMapValuePojo =
                new WriteHandlerMapValuePojo<>(excelMergeParam, WriteHandlerStaticFactory.producerCellMergeHandler());
        // 初始化合并单元格集合对象
        getStyleHandler().get(sheetName).put("cellMerge",writeHandlerMapValuePojo);
        // 初始化表头风格与表格风格
        setHeadStyle(sheetName, ExcelHeadStyle::new);
        setCellStyle(sheetName, ExcelCellStyle::new);
    }

    /**
     * 请求数据
     */
    private K paramData;
    /**
     * 是否开启批量处理
     */
    private boolean batchFlag = false;
    /**
     * 分批处理每次记录量
     */
    private int batchRowNum;


     public boolean getBatchFlag(){
         return this.batchFlag;
     }
     public int getBatchRowNum(){return this.batchRowNum;}

    @Override
    public final ExcelSheetTypeEnum getSheetType(){return ExcelSheetTypeEnum.SINGLE;}

    /**
     * 获取列表数据的class信息
     */
    public Class<T> getWriteDataClass(){
        ParameterizedType type = (ParameterizedType)getClass().getGenericSuperclass();
        Type typeArg = type.getActualTypeArguments()[0];
        return (Class<T>) typeArg;
    }
    protected K getParamData(){return this.paramData;}

    /**
     * 注意: 如果导出数据达到1万条,要求必须开启分批处理。否则无法导出1万条以后的数据
     */
    protected void openBatch(int batchRowNum){
        this.batchFlag = true;
        this.batchRowNum = batchRowNum;
    }
    protected void setHeadStyle(Supplier<ExcelHeadStyle> headStyleSupplier){
        setHeadStyle(getSheetNames().get(0),headStyleSupplier);
    }
    protected void setCellStyle(Supplier<ExcelCellStyle> cellStyleSupplier){
        setCellStyle(getSheetNames().get(0),cellStyleSupplier);
    }
    protected void setHead(List<List<String>> headList){
        setHead(getSheetNames().get(0),headList);
    }
    protected void setHead(String[] headList){
        setHead(getSheetNames().get(0),headList);
    }
    protected void addCellMerge(int startRow,int endRow,int startCol,int endCol){
        addCellMerge(getSheetNames().get(0),startRow,endRow,startCol,endCol);
    }
    protected void addWriteHandler(WriteHandler writeHandler){
        addWriteHandler(getSheetNames().get(0),writeHandler);
    }
    public void setParamData(K paramData){
        this.paramData = paramData;
    }

    /**
     * 配置方法(此方法会被最初加载)
     */
    public abstract void config();

    /**
     * 校验请求参数
     * @return R
     */
    public abstract R<Object> verifyParamData(K paramData);

    /**
     * 查询总条目
     * @return total
     */
    public abstract long calculateDataCount();

    /**
     * 查询数据列表
     * @param pageNum 页码
     * @param pageSize 每页条目
     * @return list
     */
    public abstract List<T> queryData(int pageNum,int pageSize);

}
