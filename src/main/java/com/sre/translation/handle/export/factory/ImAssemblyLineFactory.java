package com.sre.translation.handle.export.factory;

import com.sre.translation.beans.ExcelImportFileResultHandlerParam;
import com.sre.translation.beans.ExcelImportResultHandlerParam;
import com.sre.translation.beans.ExcelTranslationImportParam;
import com.sre.translation.eumn.ExcelImportNoticeTypeEnum;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.exception.ExcelHandlerException;
import com.sre.translation.exception.ExcelTemplateInitException;
import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;
import com.sre.translation.joint.IExcelStorage;
import com.sre.translation.process.assemblyline.ExcelImportStructureAssemblyLine;
import com.sre.translation.process.assemblyline.ibase.ImportAssemblyLineInterface;
import com.sre.translation.template.ExcelImportTemplate;
import com.sre.translation.template.base.CommonImportTemplate;
import com.sre.translation.template.base.IImBase;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * 导入处理器工厂
 * @author chen gang
 * @date 2025/4/11
 */
@Slf4j
public class ImAssemblyLineFactory extends AssemblyLineFactory{
    private InputStream inputStream;
    private InputStream inputStream2;
    private File file;
    private String fileName;

    public ImAssemblyLineFactory(InputStream inputStream, String fileName){
        // 将输入流复制成两份
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int bytesRead;
        try {
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            buffer.flush();
        }catch (Exception e){
            throw new ExcelHandlerException("创建excel导入失败, 输入流读取失败");
        }
        byte[] byteArray = buffer.toByteArray();
        this.inputStream = new ByteArrayInputStream(byteArray);
        this.inputStream2 = new ByteArrayInputStream(byteArray);

        this.fileName = fileName;
    }

    public ImAssemblyLineFactory(File file){
        this.file = file;
    }

    @Override
    public ImAssemblyLineFactory setExcelProgressAsyLineBean(ExcelProgressAsyLineBean excelProgressAsyLineBean){
        super.setExcelProgressAsyLineBean(excelProgressAsyLineBean);
        return this;
    }

    /**
     * 组装流线器
     */
    public ImportAssemblyLineInterface assemble(IImBase imBase, ExcelTranslationImportParam param){
        if (this.file!=null){
            try {
                this.inputStream = new FileInputStream(file);
                this.fileName = this.file.getName();
            } catch (FileNotFoundException e) {
                log.error("导入模板创建失败, 导入文件获取io异常",e);
                throw new ExcelTemplateInitException("导入模板创建失败, 导入文件获取io异常");
            }
        }
        // 获取模板的第三个泛型类型，如果是输出文件, 需要提供一个输出流修改后处理参数
        ParameterizedType type = (ParameterizedType)((CommonImportTemplate)imBase).getClass().getGenericSuperclass();
        Type typeArg = type.getActualTypeArguments()[2];
        ExcelImportNoticeTypeEnum noticeTypeEnum = ExcelImportNoticeTypeEnum.findByResultType((Class) typeArg);
        Function<ExcelImportResultHandlerParam, ? extends ExcelImportResultHandlerParam> resultHandlerParamProgress = null;
        if (noticeTypeEnum == ExcelImportNoticeTypeEnum.FROM){
            IExcelStorage iExcelStorage = param.getIExcelStorage();
            if (iExcelStorage==null){
                throw new ExcelTemplateInitException("创建excel导入模板失败, 无法使用 ExcelImportNoticeTypeEnum为FROM的方法, OutputStream 获取为空, 请检查 IExcelStorage 的实例");
            }
            String errorFileName = this.fileName.endsWith(".xls")||this.fileName.endsWith(".xlsx") ? this.fileName.split(".")[0]+"异常数据.xlsx":this.fileName+"异常数据.xlsx";
            String storagePublicPath = iExcelStorage.getStoragePublicPath();
            OutputStream outputStream = iExcelStorage.getOutputStream(errorFileName);
            File folder = new File(storagePublicPath);
            if (Boolean.FALSE.equals(folder.exists())){
                throw new ExcelTemplateInitException("创建excel导入模板失败, "+storagePublicPath+"  没有该文件夹");
            }
            if (outputStream == null){
                throw new ExcelTemplateInitException("创建excel导入模板失败, outputStream 获取为空, 请检查 IExcelStorage 的实例");
            }
            resultHandlerParamProgress = excelImportResultHandlerParam ->
                    new ExcelImportFileResultHandlerParam(excelImportResultHandlerParam.getErrorDataMap(), outputStream, errorFileName, storagePublicPath);
        }
        ExcelSheetTypeEnum sheetType = imBase.getSheetType();
        ImportAssemblyLineInterface importAssemblyLineInterface;
        if (ExcelSheetTypeEnum.SINGLE.equals(sheetType)){
            ExcelImportStructureAssemblyLine excelImportStructureAssemblyLine =  ExcelImportStructureAssemblyLine.genAssemblyLine(inputStream, inputStream2,fileName, (ExcelImportTemplate) imBase, param);
            if (resultHandlerParamProgress!=null){
                excelImportStructureAssemblyLine.setResultHandlerParamProgress(resultHandlerParamProgress);
            }
            if (this.excelProgressAsyLineBean!=null){
                excelImportStructureAssemblyLine.setLineStructure(this.excelProgressAsyLineBean);
            }
            importAssemblyLineInterface = excelImportStructureAssemblyLine;
        }else if (ExcelSheetTypeEnum.MULTIPART.equals(sheetType)){
            return null;
        }else {
            throw new ExcelTemplateInitException("导入模板创建失败, 无法识别的sheet页类型");
        }
        return importAssemblyLineInterface;
    }
}
