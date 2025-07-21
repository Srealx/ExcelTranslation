package com.sre.translation.handle;

import com.sre.commonBase.utils.CommonUtil;
import com.sre.commonBase.utils.SpringUtils;
import com.sre.translation.beans.*;
import com.sre.translation.beans.result.ExcelTranslationAsyResult;
import com.sre.translation.beans.result.ExcelTranslationResult;
import com.sre.translation.eumn.ExcelModeEnum;
import com.sre.translation.eumn.ExcelTypeEnum;
import com.sre.translation.exception.ExcelHandlerException;
import com.sre.translation.handle.exceltype.ExcelTypeInterface;
import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;
import com.sre.translation.handle.asy.ExcelProgressAsyLineStructure;
import com.sre.translation.joint.IExcelStorage;
import com.sre.translation.utils.SpringContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

/**
 * excel执行器
 * @author cheng
 * @date 2023.5/17
 */
public class ExcelHandlePerformer {
    IExcelStorage iExcelStorage;

    private ExcelHandlePerformer(){}

    public static ExcelHandlePerformer build(){
        ExcelHandlePerformer excelHandlePerformer = new ExcelHandlePerformer();
        // 在spring中查找是否有IExcelStorage的实现
        IExcelStorage iExcelStorage = SpringUtils.getBean(IExcelStorage.class);
        if (iExcelStorage!=null){
            excelHandlePerformer.setStorage(iExcelStorage);
        }
        return excelHandlePerformer;
    }

    public ExcelHandlePerformer setStorage(IExcelStorage excelStorage){
        this.iExcelStorage = excelStorage;
        return this;
    }

    /**
     * 开始excel任务
     * @param param: 导出 {@link com.sre.translation.beans.ExcelTranslationExportParam}  ;
     *               导入 {@link com.sre.translation.beans.ExcelTranslationImportParam}
     *
     * @return  导出: {@link ExcelTranslationResult} 导出以spring响应流的形式直接输出文件
     *          导入: {@link com.sre.translation.beans.result.ExcelTranslationImportResult}
     *                导入即使返回结果为文件，也不会直接以spring流的形式返回,因为需要先展示成功与失败条目，由用户决定是否下载文件
     */
    public ExcelTranslationResult start(ExcelTranslationBaseParam param){
        ExcelTypeInterface service;
        try {
            service = checkParam(param);
        }catch (ExcelHandlerException e){
            return ExcelTranslationResult.error(e.getMessage());
        }
        if (this.iExcelStorage!=null){
            param.setIExcelStorage(this.iExcelStorage);
        }
        if (Boolean.FALSE.equals(param instanceof ExcelTranslationExportParam || param instanceof ExcelTranslationImportParam)){
            throw new ExcelHandlerException("开启异步excel任务失败, 接收的 ExcelTranslationBaseParam 非导入或导出参数");
        }
        ExcelTranslationResult excelTranslationResult = service.start(param, ExcelModeEnum.SY);
        excelTranslationResult.setProgressName(SpringContextUtil.getBusinessName(param.getExcelBusinessEnumCode()));
        excelTranslationResult.setExcelBusinessEnumCode(param.getExcelBusinessEnumCode());
        return excelTranslationResult;
    }

    /**
     * 以异步模式开启任务
     * @param param:  导出 {@link ExcelTranslationExportAsyParam}
     *                导入 {@link com.sre.translation.beans.ExcelTranslationImportAsyParam}
     *
     * @return 导出: {@link ExcelTranslationAsyResult}
     *               导出的主要数据为uuid, 根据uuid查询任务进度, 需实现ExcelProgressAsyLineStructure接口来决定如何存储任务进度
     */
    public ExcelTranslationAsyResult startAsy(ExcelTranslationBaseParam param,
                                              ExcelProgressAsyLineStructure lineStructure){
        ExcelTypeInterface service;
        try {
            service = checkParam(param);
        }catch (ExcelHandlerException e){
            return ExcelTranslationAsyResult.error(e.getMessage());
        }
        if (this.iExcelStorage!=null){
            param.setIExcelStorage(this.iExcelStorage);
        }
        ExcelProgressAsyLineBean excelExportAsyLineBean = ExcelProgressAsyLineBean.build();
        excelExportAsyLineBean.whenStart(lineStructure::whenStart);
        excelExportAsyLineBean.whenNextProgress(lineStructure::whenNextProgress);
        excelExportAsyLineBean.whenEnd(lineStructure::whenEnd);
        excelExportAsyLineBean.whenException(lineStructure::whenException);
        if (param instanceof ExcelTranslationExportParam){
            param = CommonUtil.copy(param,ExcelTranslationExportAsyParam.class);
            ((ExcelTranslationExportAsyParam) param).setLineStructure(excelExportAsyLineBean);
        }else if (param instanceof ExcelTranslationImportParam){
            param = CommonUtil.copy(param,ExcelTranslationImportAsyParam.class);
            ((ExcelTranslationImportAsyParam) param).setLineStructure(excelExportAsyLineBean);
        }else {
            throw new ExcelHandlerException("开启异步excel任务失败, 接收的 ExcelTranslationBaseParam 非导入或导出参数");
        }
        ExcelTranslationResult excelTranslationResult = service.start(param, ExcelModeEnum.ASY);
        excelTranslationResult.setProgressName(SpringContextUtil.getBusinessName(param.getExcelBusinessEnumCode()));
        excelTranslationResult.setExcelBusinessEnumCode(param.getExcelBusinessEnumCode());
        return (ExcelTranslationAsyResult)excelTranslationResult;
    }

    private static ExcelTypeInterface checkParam(ExcelTranslationBaseParam param){
        if (ObjectUtils.isEmpty(param.getTypeCode())){
            throw new ExcelHandlerException("excelTypeCode必填");
        }
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.find(param.getTypeCode());
        ExcelTypeInterface service = excelTypeEnum.getService(applicationContext);
        if (ObjectUtils.isEmpty(service)){
            throw new ExcelHandlerException("excelTypeCode不正确");
        }
        return service;
    }

}
