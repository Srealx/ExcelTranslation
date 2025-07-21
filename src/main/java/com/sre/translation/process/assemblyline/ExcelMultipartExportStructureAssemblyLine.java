package com.sre.translation.process.assemblyline;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sre.commonBase.beans.response.R;
import com.sre.commonBase.utils.JsonUtil;
import com.sre.translation.beans.ExcelExportMultipartResult;
import com.sre.translation.beans.ExcelMultipartBaseResult;
import com.sre.translation.beans.ExcelTranslationExportAsyParam;
import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.eumn.ExcelExportTypeEnum;
import com.sre.translation.exception.ExcelHandlerException;
import com.sre.translation.beans.config.ExcelConfigParam;
import com.sre.translation.process.assemblyline.ibase.ExportAssemblyLine;
import com.sre.translation.template.ExcelMultipartExportTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * excel导出-构造模式-多sheet页处理线
 * @author cheng
 * @date 2023/5/17
 */
@Slf4j
public class ExcelMultipartExportStructureAssemblyLine extends ExportAssemblyLine {
    /**
     * 导出模版
     */
    private ExcelMultipartExportTemplate excelExportTemplate;

    /**
     * sheet信息
     */
    private List<ExcelMultipartBaseResult> sheetResults;

    private String progressUuid;

    private ExcelMultipartExportStructureAssemblyLine(ExcelTranslationExportParam param, ExcelMultipartExportTemplate model, OutputStream outputStream){
        super(param,outputStream);
        this.excelExportTemplate = model;
        // 初始化请求参数
        Object customParamData = JsonUtil.map2Bean((HashMap)param.getParamData(),(Class) ((ParameterizedType)excelExportTemplate.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
        model.setParamData(customParamData);
        excelTranslationParam.setParamData(customParamData);
        if (param instanceof ExcelTranslationExportAsyParam){
            this.progressUuid =  ((ExcelTranslationExportAsyParam)param).getProgressUuid();
        }
    }

    public static ExcelMultipartExportStructureAssemblyLine genAssemblyLine(ExcelTranslationExportParam param, ExcelMultipartExportTemplate model, OutputStream outputStream){
        return new ExcelMultipartExportStructureAssemblyLine(param,model,outputStream);
    }

    @Override
    public void init() {
        log.info("开始初始化excel导出");
        // 校验参数
        R<Object> r = excelExportTemplate.verifyParamData();
        if (!ObjectUtils.isEmpty(r) && !r.isSuccess()){
            throw new ExcelHandlerException("参数校验失败: "+r.getMsg());
        }
        // sheet信息
        List<ExcelMultipartBaseResult> list = excelExportTemplate.getManySheetCountResultList();
        if (CollectionUtils.isEmpty(list)){
            throw new ExcelHandlerException("数据为空无法导出");
        }
        this.sheetResults = list;
        // 开启分批处理的情况下校验数据状态
        if (excelExportTemplate.getBatchFlag()){
            boolean dataExist = false;
            for (ExcelMultipartBaseResult item : list) {
                if (item.getCount() > 0) {
                    dataExist = true;
                    break;
                }
            }
            if (!dataExist){
                throw new ExcelHandlerException("数据为空, 无法导出");
            }
        }
        // 初始化表头
        excelExportTemplate.initSheetHeads();
        // 初始化其他配置
        excelExportTemplate.config();
        log.info("excel初始化成功");
        this.fileName=excelExportTemplate.getFileName();
        // 开始进度
        start(progressUuid,this.fileName,lineStructure::runStart);
    }

    @Override
    public void start() {
        log.info("开始执行excel多sheet页导出");
        // 构建导出对象
        ExcelWriterBuilder writerBuilder;
        writerBuilder = EasyExcelFactory.write(this.outputStream);
        ExcelWriter excelWriter = writerBuilder.build();
        // 标记sheet页index
        int sheetNo = 1;

        // 预生成多个sheet
        Map<String,WriteSheet> sheetMap = new HashMap<>(4);
        for (ExcelMultipartBaseResult sheetResult : sheetResults) {
            WriteSheet sheet = EasyExcelFactory.writerSheet(sheetNo++, sheetResult.getSheetName()).build();
            sheetMap.put(sheetResult.getSheetName(),sheet);
        }
        // 拿到导出模式
        ExcelExportTypeEnum exportType = excelExportTemplate.getExportType();
        // 执行该模式下的配置
        exportType.getConfig().loadConfig(new ExcelConfigParam(ExcelSheetTypeEnum.MULTIPART,
                excelExportTemplate,
                writerBuilder,
                sheetMap));

        // 分批处理
        if (excelExportTemplate.getBatchFlag()){
            // 遍历sheet页
            int pageSize = excelExportTemplate.getBatchRowNum()==0 ? 500 : excelExportTemplate.getBatchRowNum();
            // 计算总进度次数
            int totalProgress=0;
            int nowProgress=1;
            for (ExcelMultipartBaseResult sheetResult : sheetResults) {
                totalProgress+=(int) (sheetResult.getCount()/pageSize)+(sheetResult.getCount()%pageSize==0?0:1);
            }
            for (ExcelMultipartBaseResult sheetResult : sheetResults) {
                WriteSheet sheet = sheetMap.get(sheetResult.getSheetName());
                // 每个sheet页分批
                int totalNum = (int) (sheetResult.getCount()/pageSize)+(sheetResult.getCount()%pageSize==0?0:1);
                for (int i = 1; i <=totalNum ; i++) {
                    ExcelExportMultipartResult data = excelExportTemplate.queryData(sheetResult.getSheetName(), i, pageSize);
                    if (ObjectUtils.isEmpty(data)){
                        data.setDataList(new ArrayList<>());
                    }
                    excelWriter.write(data.getDataList(),sheet);
                    // 进度更新
                    nextProgress(lineStructure::runNextProgress,this.fileName,progressUuid,totalProgress,nowProgress,nowProgress==1?Boolean.TRUE:Boolean.FALSE);
                    nowProgress++;
                }
            }
            excelWriter.finish();
            log.info("excel导出完成");
        }else {
            // 非分批处理
            int totalProgress = sheetResults.size();
            int nowProgress=1;
            for (ExcelMultipartBaseResult sheetResult : sheetResults) {
                log.info("非分批处理, sheet: {} 最多导出 9999 条数据",sheetResult.getSheetName());
                ExcelExportMultipartResult data = excelExportTemplate.queryData(sheetResult.getSheetName(), 1, 9999);
                if (ObjectUtils.isEmpty(data)){
                    data.setDataList(new ArrayList<>());
                }
                // 新建excel sheet
                WriteSheet sheet = sheetMap.get(sheetResult.getSheetName());
                excelWriter.write(data.getDataList(),sheet);
                // 进度更新
                nextProgress(lineStructure::runNextProgress,this.fileName,progressUuid,totalProgress,nowProgress,nowProgress==1?Boolean.TRUE:Boolean.FALSE);
                nowProgress++;
            }
            excelWriter.finish();
            log.info("excel导出完成");
        }
    }

    @Override
    public void finish(String saveFileRootPath) {
        finish(saveFileRootPath,progressUuid,this.fileName,lineStructure::runEnd);
    }
}
