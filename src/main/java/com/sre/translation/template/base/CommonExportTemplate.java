package com.sre.translation.template.base;

import com.alibaba.excel.write.handler.WriteHandler;
import com.sre.commonBase.utils.SpringUtils;
import com.sre.translation.beans.style.*;
import com.sre.translation.eumn.ExcelExportTypeEnum;
import com.sre.translation.exception.ExcelTemplateInitException;
import com.sre.translation.joint.IExcelStorage;
import com.sre.translation.process.handler.writeHandlerProducer.WriteHandlerStaticFactory;
import com.sre.translation.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

/**
 * 公共基础导出模版
 * @author cheng
 * @date 2023/5/19
 */
@Slf4j
public abstract class CommonExportTemplate {
    /**
     * 是否使用模版文件(默认不用) & 模版文件名
     */
    private ExcelExportTypeEnum type = ExcelExportTypeEnum.STRUCTURE;
    private InputStream templateFileInputStream;
    /**
     * sheet列表
     */
    private List<String> sheetNames = Lists.newArrayList("sheet1");
    /**
     * 自定义表头
     */
    private final Map<String,List<List<String>>> head = new HashMap<>();
    private final Map<String,Boolean> needHead = new HashMap<>();
    /**
     * 合并单元格
     * String : sheetName
     * 格式：[[起始行,起始列,结束行,结束列]]
     * 示例：[[1,1,2,2],[3,3,5,9]]
     * 坐标值：从零开始
     */
    private final Map<String,Set<int[]>> cellMerge = new HashMap<>();
    /**
     * 导出样式执行器集合
     * object -> 调用方法的泛型
     * WriteHandlerProducerInterface -> 统一接口
     */
    private final Map<String,Map<String, WriteHandlerMapValuePojo<ExcelStyleParam>>> styleHandler = new HashMap<>();
    /**
     * 自定义写入控制器
     */
    private Map<String,List<WriteHandler>> writeHandlerList;




    /**
     * 获取对象内容(公共方法)
     */
    public ExcelExportTypeEnum getExportType(){return this.type;}
    public InputStream getTemplate(){return this.templateFileInputStream;}
    public List<String> getSheetNames(){return this.sheetNames;}
    public Set<int[]> getCellMerge(String sheetName){return this.cellMerge.get(sheetName);}
    public List<List<String>> getHead(String sheetName){return this.head.get(sheetName);}
    public Map<String,Map<String, WriteHandlerMapValuePojo<ExcelStyleParam>>> getStyleHandler(){return this.styleHandler;}
    public boolean isNeedHead(String sheetName){
        return this.needHead.get(sheetName);
    }


    public Map<String, WriteHandlerMapValuePojo<ExcelStyleParam>> getStyleHandler(String sheetNane) {return this.styleHandler.get(sheetNane);}
    public List<WriteHandler> getWriteHandlerList(String sheetNane){
        if (!CollectionUtils.isEmpty(this.writeHandlerList)){
            return this.writeHandlerList.get(sheetNane);
        }else {
            return Lists.newArrayList();
        }

    }


    protected void initStyleCollection(String sheetName){
        initCellMerge(sheetName);
        initStyleHandler(sheetName);
        initNeedHead(sheetName);
    }
    protected void initCellMerge(String sheetName){
        this.cellMerge.put(sheetName,new HashSet<>());
    }
    protected void initStyleHandler(String sheetName){
        this.styleHandler.put(sheetName,new HashMap<>(16));
    }
    protected void initNeedHead(String sheetName){this.needHead.put(sheetName,true);}
    /**
     * 配置模版文件
     * @param inputStream 文件输入流
     */
    protected void setTemplate(FileInputStream inputStream){
        if (inputStream == null){
            throw new ExcelTemplateInitException("无法配置文件导出模板, InputStream 获取为空");
        }
        this.type=ExcelExportTypeEnum.TEMPLATE;
        this.templateFileInputStream = inputStream;
        // 使用文件模版优先级更高, 将所有needHead置为false
        this.sheetNames.forEach(this::ignoreHead);
    }

    /**
     * 配置模板文件, 需要实现IExcelStorage 的 getInputStream方法
     * @param fileName 文件名
     */
    protected void setTemplate(String fileName){
        IExcelStorage storage = SpringUtils.getBean(IExcelStorage.class);
        if (storage == null){
            throw new ExcelTemplateInitException("无法配置文件导出模板, 未找到IExcelStorage的实例");
        }
        InputStream fileInputStream = storage.getFileInputStream(fileName);
        if (fileInputStream == null){
            throw new ExcelTemplateInitException("无法配置文件导出模板, InputStream 获取为空");
        }
        this.type=ExcelExportTypeEnum.TEMPLATE;
        this.templateFileInputStream = fileInputStream;
        this.sheetNames.forEach(this::ignoreHead);
    }

    /**
     * 设置表头
     */
    protected void setSheetName(List<String> sheetNames){
        this.sheetNames = sheetNames;
    }
    /**
     * 设置表头
     * @param easyHead 简易表头
     */
    protected void setHead(String sheetName,String[] easyHead){
        if (easyHead == null || easyHead.length == 0){
            return;
        }
        this.head.put(sheetName,ExcelUtil.changeHead(easyHead));
    }
    protected void setHead(String sheetName,List<List<String>> head){
        if (CollectionUtils.isEmpty(head)){
            return;
        }
        this.head.put(sheetName,head);
    }
    protected void ignoreHead(String sheetName){this.needHead.put(sheetName,false);}


    /**
     * 设置表头样式
     */
    protected void setHeadStyle(String sheetName,Supplier<ExcelHeadStyle> headStyleSupplier){
        ExcelHeadStyle excelHeadStyle = headStyleSupplier.get();
        if (!ObjectUtils.isEmpty(excelHeadStyle)) {
            excelHeadStyle.setSheetName(sheetName);
            WriteHandlerMapValuePojo<ExcelStyleParam> writeHandlerMapValuePojo =
                    new WriteHandlerMapValuePojo<>(excelHeadStyle, WriteHandlerStaticFactory.producerHeadStyleHandler());
            styleHandler.get(sheetName).put("headStyle",writeHandlerMapValuePojo);
        }
    }

    /**
     * 设置表格样式
     */
    protected void setCellStyle(String sheetName,Supplier<ExcelCellStyle> cellStyleSupplier){
        ExcelCellStyle excelCellStyle = cellStyleSupplier.get();
        if (!ObjectUtils.isEmpty(excelCellStyle)) {
            excelCellStyle.setSheetName(sheetName);
            WriteHandlerMapValuePojo<ExcelStyleParam> writeHandlerMapValuePojo =
                    new WriteHandlerMapValuePojo<>(excelCellStyle, WriteHandlerStaticFactory.producerCellStyleHandler());
            styleHandler.get(sheetName).put("cellStyle", writeHandlerMapValuePojo);
        }
    }

    protected void addWriteHandler(String sheetName,WriteHandler writeHandler){
        if (CollectionUtils.isEmpty(writeHandlerList)){
            writeHandlerList = new HashMap<>(16);
        }
        writeHandlerList.get(sheetName).add(writeHandler);
    }


    /** 设置对象内容 (继承方法)
     /**
     * 新增合并单元格
     * *** 注意事项:  如果合并单元格为动态算法添加, 那么推荐在queryData的末尾添加单元格合并;
     *               如果合并单元格为写死的值,那么建议写在init方法里;
     *               原因是分批处理的情况下queryData会被多次调用, 写死的值会产生重复值合并报错
     */
    protected void addCellMerge(String sheetName,int startRow,int endRow,int startCol,int endCol){
        if (startRow < 0 || startCol < 0 || endRow < 0 || endCol < 0 || endRow < startRow || endCol < startCol) {
            log.error("合并单元格参数异常，startRow：{},startCol：{},endRow：{},endCol：{},", startRow, startCol, endRow, endCol);
            return;
        }
        cellMerge.get(sheetName).add(new int[]{startRow, endRow, startCol, endCol});
    }
}
