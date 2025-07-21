package com.sre.translation.template;

import com.sre.commonBase.beans.response.R;
import com.sre.translation.beans.ExcelExportMultipartResult;
import com.sre.translation.beans.ExcelMultipartBaseResult;
import com.sre.translation.beans.style.*;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.process.handler.writeHandlerProducer.WriteHandlerStaticFactory;
import com.sre.translation.template.base.CommonExportTemplate;
import com.sre.translation.template.base.IExBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * excel多sheet页导出模版
 * @author sre
 * @date 2023/5/15
 */
@Slf4j
public abstract class ExcelMultipartExportTemplate<T extends ExcelExportMultipartResult,K> extends CommonExportTemplate implements IExBase {
    public ExcelMultipartExportTemplate(){
        this.manySheetCountResultList = initBaseData();
        List<String> sheetNames = new ArrayList<>();
        int sysSheetNameIndex = 1;
        for (ExcelMultipartBaseResult item : manySheetCountResultList) {
            String sheetName = item.getSheetName();
            // 防空sheet名
            if (Strings.isEmpty(sheetName)){
                sheetName = "sheet"+sysSheetNameIndex++;
                item.setSheetName(sheetName);
            }
            sheetNames.add(sheetName);
            // 初始化单sheet页的collection属性
            initStyleCollection(sheetName);
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
        setSheetName(sheetNames);
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
    /**
     * sheet页基本信息(sheet页名, 记录数)
     */
    private List<ExcelMultipartBaseResult> manySheetCountResultList;



    public boolean getBatchFlag(){
         return this.batchFlag;
     }
    public int getBatchRowNum(){return this.batchRowNum;}

    @Override
    public final ExcelSheetTypeEnum getSheetType(){return ExcelSheetTypeEnum.MULTIPART;}
    protected K getParamData(){return this.paramData;}
    public List<ExcelMultipartBaseResult> getManySheetCountResultList(){return this.manySheetCountResultList;}

    public void setParamData(K paramData){
         this.paramData = paramData;
     }

    /**
     * 如果不开启分批处理, 单个sheet最多导出9999条
     */
    protected void openBatch(int batchRowNum){
        this.batchFlag = true;
        this.batchRowNum = batchRowNum;
    }

    /**
     * 配置(此方法会被最先加载)
     */
    public abstract void config();
    /**
     * 校验请求参数
     * @return R
     */
    public abstract R<Object> verifyParamData();

    /**
     * 初始化表头数据(多sheet页需实现此方法)
     */
    public abstract void initSheetHeads();
    /**
     * 初始化重要信息(sheet页名称, 每个sheet的总记录数)
     * @return result
     */
    public abstract List<ExcelMultipartBaseResult> initBaseData();

    /**
     * 查询数据列表
     * @param pageNum 页码
     * @param pageSize 每页条目
     * @param sheetName 页名
     * @return list
     */
    public abstract T queryData(String sheetName, int pageNum, int pageSize);

}
