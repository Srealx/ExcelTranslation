package com.sre.translation.process.assemblyline;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sre.commonBase.beans.response.R;
import com.sre.commonBase.utils.JsonUtil;
import com.sre.translation.beans.ExcelTranslationExportAsyParam;
import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.eumn.ExcelExportTypeEnum;
import com.sre.translation.exception.ExcelHandlerException;
import com.sre.translation.beans.config.ExcelConfigParam;
import com.sre.translation.process.assemblyline.ibase.ExportAssemblyLine;
import com.sre.translation.template.ExcelExportTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel导出-构造模式-单sheet页处理线
 * @author cheng
 * @date 2023/5/17
 */
@Slf4j
public class ExcelExportStructureAssemblyLine extends ExportAssemblyLine {
    /**
     * 导出模版
     */
    private ExcelExportTemplate excelExportTemplate;
    /**
     * 处理器数据
     */
    private Long dataTotal;

    private String progressUuid;

    private ExcelExportStructureAssemblyLine(ExcelTranslationExportParam param, ExcelExportTemplate model, OutputStream outputStream){
        super(param,outputStream);
        this.excelExportTemplate = model;
        // 初始化请求参数
        Object customParamData = JsonUtil.map2Bean((HashMap)param.getParamData(),(Class) ((ParameterizedType)excelExportTemplate.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
        this.excelExportTemplate.setParamData(customParamData);
        excelTranslationParam.setParamData(customParamData);
        if (param instanceof ExcelTranslationExportAsyParam){
            this.progressUuid =  ((ExcelTranslationExportAsyParam)param).getProgressUuid();
        }
    }

    public static ExcelExportStructureAssemblyLine genAssemblyLine(ExcelTranslationExportParam param, ExcelExportTemplate model, OutputStream outputStream){
        return new ExcelExportStructureAssemblyLine(param,model,outputStream);
    }

    @Override
    public void init() {
        log.info("开始初始化excel导出:   {}",progressUuid);
        // 校验参数
        R<Object> r = excelExportTemplate.verifyParamData(excelTranslationParam.getParamData());
        if (!ObjectUtils.isEmpty(r) && !r.isSuccess()){
            throw new ExcelHandlerException("参数校验失败: "+r.getMsg());
        }
        // 初始化其他配置
        excelExportTemplate.config();
        // 如果开启分批处理, 则查询总条目
        if (excelExportTemplate.getBatchFlag()){
            log.info("开启分批处理, 初始化本次任务数据总条目");
            dataTotal = excelExportTemplate.calculateDataCount();
            if (dataTotal ==0){
                throw new ExcelHandlerException("数据为空, 无法导出");
            }
        }
        log.info("excel初始化成功");
        this.fileName=excelExportTemplate.getFileName();
        // 流程开始
        start(progressUuid,this.fileName,e->lineStructure.runStart(e));
    }

    @Override
    public void start() {
        log.info("开始执行excel导出");
        // 构建导出对象
        ExcelWriterBuilder writerBuilder = EasyExcelFactory.write(this.outputStream);
        ExcelWriter excelWriter = writerBuilder.build();
        WriteSheet sheet = EasyExcelFactory.writerSheet(1, excelExportTemplate.getSheetNames().get(0)).build();
        // 拿到导出模式
        ExcelExportTypeEnum exportType = excelExportTemplate.getExportType();
        // 生成加载配置的sheet参数
        Map<String,WriteSheet> sheetMap = new HashMap<>(4);
        sheetMap.put(excelExportTemplate.getSheetNames().get(0),sheet);
        // 加载配置
        exportType.getConfig().loadConfig(new ExcelConfigParam(ExcelSheetTypeEnum.SINGLE,
                excelExportTemplate,
                writerBuilder,
                sheetMap));
        // 分批处理
        if (excelExportTemplate.getBatchFlag()){
            int pageSize = excelExportTemplate.getBatchRowNum() == 0 ? 500 : excelExportTemplate.getBatchRowNum();
            int totalNum = (int) (dataTotal/pageSize)+(dataTotal%pageSize==0?0:1);
            // 分批处理数据
            List<Object> list;
            for (int i = 1; i <= totalNum; i++) {
                list = excelExportTemplate.queryData(i, pageSize);
                excelWriter.write(list, sheet);
                // 进度+1
                Boolean first=Boolean.TRUE;
                if (i>1){
                    first=Boolean.FALSE;
                }
                nextProgress(lineStructure::runNextProgress,this.fileName,progressUuid,totalNum,i,first);
            }
            excelWriter.finish();
            log.info("excel导出完成");
        }else {
            // 不分批处理(最多只能导出9999条数据)
            log.info("非分批处理, 最多导出9999条数据.... ");
            List list = excelExportTemplate.queryData(1, 9999);
            excelWriter.write(list, sheet);
            excelWriter.finish();
            // 进度+1
            nextProgress(lineStructure::runNextProgress,this.fileName,progressUuid,1,1,Boolean.TRUE);
            log.info("excel导出完成");
        }
    }


    @Override
    public void finish(String saveFileRootPath) {
        finish(saveFileRootPath,progressUuid,this.fileName,lineStructure::runEnd);
    }

}
