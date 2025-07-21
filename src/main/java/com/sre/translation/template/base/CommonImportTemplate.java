package com.sre.translation.template.base;

import com.alibaba.fastjson.JSON;
import com.sre.translation.beans.ExcelImportErrorDataResult;
import com.sre.translation.beans.ExcelImportResultJsonDefaultField;
import com.sre.translation.beans.ExcelImportResultJsonDefaultRow;
import com.sre.translation.beans.ExcelImportResultJsonDefaultSheet;
import com.sre.translation.eumn.ExcelImportNoticeTypeEnum;
import com.sre.translation.template.result.dto.FormImportResultDTO;
import com.sre.translation.template.result.dto.ImportResultDTO;
import com.sre.translation.template.result.dto.MsgImportResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 公共导入模板
 * @author chen gang
 * @date 2025/4/9
 */
@Slf4j
public abstract class CommonImportTemplate<T,I extends ImportResultDTO> {
    /**
     * 导入结果数据类型(默认json)
     */
    protected ExcelImportNoticeTypeEnum noticeType;
    /**
     * 分批处理条目数
     */
    protected int batchCount=500;

    /**
     * sheet列表
     */
    protected List<String> sheetNames = Lists.newArrayList("sheet1");

    /**
     * sheet页与表头行数
     */
    protected Map<String,Integer> sheetHeadRowMap = new HashMap<>();
    /**
     * 异常数据map
     * key: sheet页
     * value: key: 行数 ; value:异常对象
     */
    private Map<String,Map<Integer,ExcelImportErrorDataResult<T>>> errorDataMap = new HashMap<>(4);

    /**
     * 以下三个数据给框架使用, 用来判断异常数据的行数
     */
    private String nowSheetName;
    private Integer nowRowNumber;
    private Map<T,Integer> nowReadDataIndexMap;
    /**
     * 获取异常数据map
     * @return map
     */
    public Map<String,Map<Integer,ExcelImportErrorDataResult<T>>> getErrorDataMap() {
        return this.errorDataMap;
    }

    public Integer getErrorDataCount(String sheetName){
        Map<Integer, ExcelImportErrorDataResult<T>> map = errorDataMap.get(sheetName);
        return map==null? 0: map.size();
    }


    protected void addErrorData(T errObj, String filed, String msg){
        Integer rowNumber = this.nowRowNumber + this.nowReadDataIndexMap.get(errObj);
        log.info("添加异常数据:  当前页 {} ; 当前行数 {}",this.nowSheetName,rowNumber);
        Map<Integer, ExcelImportErrorDataResult<T>> map =
                this.errorDataMap.computeIfAbsent(this.nowSheetName, k -> new HashMap<>(2));
        ExcelImportErrorDataResult<T> tExcelImportErrorDataResult =
                map.computeIfAbsent(rowNumber, k -> new ExcelImportErrorDataResult<>(errObj,new HashMap<>(2)));
        tExcelImportErrorDataResult.getFieldErrorMap().put(filed,msg);
    }

    public ExcelImportNoticeTypeEnum getNoticeType() {
        return noticeType;
    }

    /**
     * 设置每次读多少条 (上限500)
     * @return count
     */
    protected int batchCount(){
        return this.batchCount;
    }

    public int getHeadRowNumber(String sheetName){
        return this.sheetHeadRowMap.get(sheetName);
    }


    public void setNowSheetName(String nowSheetName) {
        this.nowSheetName = nowSheetName;
    }

    public void setNowRowNumber(Integer nowRowNumber) {
        this.nowRowNumber = nowRowNumber;
    }

    public void setNowReadData(List<T> nowReadDataList) {
        this.nowReadDataIndexMap = IntStream.range(0, nowReadDataList.size())
                .boxed()
                .collect(Collectors.toMap(nowReadDataList::get, i -> i));
    }

    /**
     * 导入结束后返还的jsonString
     * @param result 异常数据
     * @return jsonString
     */
    public String returnResult(I result){
        // 处理返回json
        if (result instanceof MsgImportResultDTO){
            List<ExcelImportResultJsonDefaultSheet> list = Lists.newArrayList();
            MsgImportResultDTO resultDTO =  (MsgImportResultDTO)result;
            resultDTO.getErrorData().forEach((k,v)->{
                ExcelImportResultJsonDefaultSheet excelImportResultJsonDefaultSheet = new ExcelImportResultJsonDefaultSheet();
                excelImportResultJsonDefaultSheet.setSheetName(k);
                List<ExcelImportResultJsonDefaultRow> rowList = Lists.newArrayList();
                excelImportResultJsonDefaultSheet.setRowList(rowList);
                list.add(excelImportResultJsonDefaultSheet);
                v.forEach((k2,v2)->{
                    ExcelImportResultJsonDefaultRow excelImportResultJsonDefaultRow = new ExcelImportResultJsonDefaultRow();
                    excelImportResultJsonDefaultRow.setRowNumber(k2);
                    List<ExcelImportResultJsonDefaultField> fieldList = Lists.newArrayList();
                    excelImportResultJsonDefaultRow.setFieldList(fieldList);
                    rowList.add(excelImportResultJsonDefaultRow);
                    v2.getFieldErrorMap().forEach((k3,v3)->{
                        ExcelImportResultJsonDefaultField excelImportResultJsonDefaultField = new ExcelImportResultJsonDefaultField();
                        excelImportResultJsonDefaultField.setFiledName(k3);
                        excelImportResultJsonDefaultField.setMessage(v3);
                        fieldList.add(excelImportResultJsonDefaultField);
                    });
                });
            });
            return JSON.toJSONString(list);
        }
        //  处理返回文件
        if (result instanceof FormImportResultDTO){
            FormImportResultDTO fileResultImportResult =  (FormImportResultDTO)result;
            return fileResultImportResult.getResultFilepath();
        }
        return null;
    }


}
