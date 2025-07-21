package com.sre.translation.process.assemblyline;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.sre.commonBase.beans.response.R;
import com.sre.commonBase.utils.JsonUtil;
import com.sre.translation.beans.ExcelImportResult;
import com.sre.translation.beans.ExcelImportResultHandlerParam;
import com.sre.translation.beans.ExcelTranslationImportAsyParam;
import com.sre.translation.beans.ExcelTranslationImportParam;
import com.sre.translation.eumn.ExcelImportNoticeTypeEnum;
import com.sre.translation.exception.ExcelHandlerException;
import com.sre.translation.exception.ExcelImportReadException;
import com.sre.translation.process.assemblyline.ibase.ImportAssemblyLine;
import com.sre.translation.template.ExcelImportTemplate;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
/**
 * excel导入结构流线器
 * @author cheng
 * @date 2024/7/19
 */
@Slf4j
public class ExcelImportStructureAssemblyLine extends ImportAssemblyLine {
    /**
     * 导入模板
     */
    private ExcelImportTemplate excelImportTemplate;

    private String progressUuid;
    /**
     * 总数据量
     */
    private int totalDataCount = 0;
    private String resultInfoString;

    private ExcelImportStructureAssemblyLine(InputStream fileInputStream,InputStream fileInputStream2,String fileName,ExcelImportTemplate excelImportTemplate,ExcelTranslationImportParam excelTranslationImportParam){
        super(fileInputStream,fileInputStream2,fileName,excelTranslationImportParam);
        this.excelImportTemplate = excelImportTemplate;
        // 初始化请求参数
        Object customParamData = JsonUtil.map2Bean((HashMap)excelTranslationImportParam.getParamData(),(Class) ((ParameterizedType)excelImportTemplate.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
        this.excelTranslationImportParam.setParamData(customParamData);
        if (excelTranslationImportParam instanceof ExcelTranslationImportAsyParam){
            this.progressUuid = ((ExcelTranslationImportAsyParam)excelTranslationImportParam).getProgressUuid();
        }
    }

    public static ExcelImportStructureAssemblyLine genAssemblyLine(InputStream fileInputStream,InputStream fileInputStream2,String fileName,ExcelImportTemplate excelImportTemplate,ExcelTranslationImportParam excelTranslationImportParam){
        return new ExcelImportStructureAssemblyLine(fileInputStream,fileInputStream2,fileName,excelImportTemplate,excelTranslationImportParam);
    }



    @Override
    public void init() {
        log.info("开始初始化excel导入:  {}",progressUuid);
        // 校验参数
        R<Object> r = excelImportTemplate.verifyParamData(excelTranslationImportParam.getParamData());
        if (!ObjectUtils.isEmpty(r) && !r.isSuccess()){
            throw new ExcelHandlerException("参数校验失败: "+r.getMsg());
        }
        log.info("excel初始化成功");
        // 流程开始
        start(progressUuid,fileName, lineStructure::runStart);
    }

    @Override
    public void start() {
        log.info("开始执行excel导出");
        int headRowNumber = excelImportTemplate.getHeadRowNumber(excelImportTemplate.getSheetName());
        // 先读一次获取总数据行数
        List<Object> dataList = EasyExcelFactory.read(fileInputStream).headRowNumber(headRowNumber).doReadAllSync();
        if (CollUtil.isEmpty(dataList)){
            throw new ExcelImportReadException("excel读取数据失败, 文件数据为空");
        }
        this.totalDataCount = dataList.size();
        // 提前释放,避免数据量过大占用内存
        dataList.clear();
        // 读取 importFile (固定分批读取, 默认每次读500条)
        int batchCount = excelImportTemplate.getBatchCount();
        int totalBatch = this.totalDataCount / batchCount + (this.totalDataCount % batchCount > 0 ? 1:0 );
        Class importDataClass =  (Class) ((ParameterizedType)excelImportTemplate.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ExcelTypeEnum excelTypeEnum=null;
        for (ExcelTypeEnum value : ExcelTypeEnum.values()) {
            if (value.getValue().equals(StrPool.DOT +excelTranslationImportParam.getFileExtend())){
                excelTypeEnum = value;
            }
        }
        if (excelTypeEnum == null){
            throw new ExcelImportReadException("读取excel失败，fileExtend无法识别");
        }
        try {
            EasyExcelFactory.read(fileInputStream2, new AnalysisEventListener() {
                        List<Object> readDataList = Lists.newArrayList();
                        int nowBatch = 0;
                        int startRow  = headRowNumber;
                        // 表示每个批次读取的下标
                        int readIndex = 0;
                        @Override
                        public void invoke(Object data, AnalysisContext context) {
                            readDataList.add(data);
                            readIndex++;
                            if (readDataList.size()>=batchCount){
                                doProgress();
                                readDataList.clear();
                                // 将起始行数的下标置为本批次读取的最后一行
                                startRow+=readIndex;
                                // 将下标清零
                                readIndex = 0;
                            }
                        }
                        /**
                         * 由于invoke里是通过是否达到单次批次最大处理量来判断的，最后一个批次很可能未执行退出，所以在这里也要判断执行一次
                         */
                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            if (!readDataList.isEmpty()){
                                doProgress();
                                readDataList.clear();
                            }
                        }
                        private void doProgress() {
                            // startRow-1  因为程序处理的excel行数下标从0开始
                            excelImportTemplate.setNowSheetName(excelImportTemplate.getSheetName());
                            excelImportTemplate.setNowReadData(readDataList);
                            excelImportTemplate.setNowRowNumber(startRow);
                            excelImportTemplate.read(readDataList);
                            // 进度+1
                            nextProgress(lineStructure::runNextProgress, fileName, progressUuid, totalBatch, ++nowBatch, Boolean.TRUE);
                        }
                    }).excelType(excelTypeEnum).ignoreEmptyRow(Boolean.TRUE)
                    .head(importDataClass).sheet(0).headRowNumber(headRowNumber)
                    .doRead();
        }catch (Exception e){
            e.printStackTrace();
            throw new ExcelImportReadException("excel读取数据失败, 请检查excel格式是否有误");
        }
        log.info("excel导入完成");
        // 获取错误数据并交给模板算法处理
        ExcelImportNoticeTypeEnum noticeType = excelImportTemplate.getNoticeType();
        ExcelImportResultHandlerParam excelImportResultHandlerParam = new ExcelImportResultHandlerParam();
        excelImportResultHandlerParam.setErrorDataMap(excelImportTemplate.getErrorDataMap());
        // 执行参数后处理器
        excelImportResultHandlerParam = resultHandlerParamProgress.apply(excelImportResultHandlerParam);
        this.resultInfoString = noticeType.getImportResultHandler().handler(excelImportResultHandlerParam)
                                                                   .toResultString(excelImportTemplate::returnResult);
    }

    @Override
    public ExcelImportResult finish() {
        ExcelImportResult excelImportResult = new ExcelImportResult();
        excelImportResult.setInfoData(this.resultInfoString);
        excelImportResult.setInfoDataType(excelImportTemplate.getNoticeType().getType());
        excelImportResult.setTotalCount(this.totalDataCount);
        excelImportResult.setFailCount(excelImportTemplate.getErrorDataCount(excelImportTemplate.getSheetName()));
        excelImportResult.setSuccessCount(excelImportResult.getTotalCount() - excelImportResult.getFailCount());
        finish(JSON.toJSONString(excelImportResult),progressUuid,this.fileName,lineStructure::runEnd);
        return excelImportResult;
    }
}
