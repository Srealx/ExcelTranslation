package com.sre.translation.handle.export.factory;

import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.eumn.ExcelSheetTypeEnum;
import com.sre.translation.exception.ExcelFactoryException;
import com.sre.translation.handle.asy.ExcelProgressAsyLineBean;
import com.sre.translation.process.assemblyline.ExcelExportStructureAssemblyLine;
import com.sre.translation.process.assemblyline.ExcelMultipartExportStructureAssemblyLine;
import com.sre.translation.process.assemblyline.ibase.ExportAssemblyLineInterface;
import com.sre.translation.template.ExcelExportTemplate;
import com.sre.translation.template.ExcelMultipartExportTemplate;
import com.sre.translation.template.base.IExBase;

import java.io.OutputStream;

/**
 * 线处理工厂
 * @author cheng
 * @date 2023/6/9
 */
public class ExAssemblyLineFactory extends AssemblyLineFactory{


    @Override
    public ExAssemblyLineFactory setExcelProgressAsyLineBean(ExcelProgressAsyLineBean excelProgressAsyLineBean){
        super.setExcelProgressAsyLineBean(excelProgressAsyLineBean);
        return this;
    }

    /**
     * 组装流线器
     */
    public ExportAssemblyLineInterface assemble(IExBase exBase, ExcelTranslationExportParam param, OutputStream outputStream){
        ExcelSheetTypeEnum sheetType = exBase.getSheetType();
        if (ExcelSheetTypeEnum.SINGLE.equals(sheetType)){
            ExcelExportStructureAssemblyLine excelExportStructureAssemblyLine = ExcelExportStructureAssemblyLine.genAssemblyLine(param, (ExcelExportTemplate) exBase, outputStream);
            if (this.excelProgressAsyLineBean!=null){
                excelExportStructureAssemblyLine.setLineStructure(this.excelProgressAsyLineBean);
            }
            return excelExportStructureAssemblyLine;
        }else if (ExcelSheetTypeEnum.MULTIPART.equals(sheetType)){
            ExcelMultipartExportStructureAssemblyLine excelMultipartExportStructureAssemblyLine = ExcelMultipartExportStructureAssemblyLine.genAssemblyLine(param, (ExcelMultipartExportTemplate) exBase, outputStream);
            if (this.excelProgressAsyLineBean!=null){
                excelMultipartExportStructureAssemblyLine.setLineStructure(this.excelProgressAsyLineBean);
            }
            return excelMultipartExportStructureAssemblyLine;
        }else {
            throw new ExcelFactoryException("框架异常, 该导出父级实现类提供了非法的sheet类型");
        }
    }
}
