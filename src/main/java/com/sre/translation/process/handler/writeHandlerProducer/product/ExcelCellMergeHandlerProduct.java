package com.sre.translation.process.handler.writeHandlerProducer.product;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.sre.translation.beans.style.ExcelMergeParam;
import com.sre.translation.process.handler.writeHandlerProducer.ibase.SheetWriteHandlerProductInterface;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * excel自定义单元格合并处理器
 * @author cheng
 * @date 2023/5/25
 */
@Slf4j
@EqualsAndHashCode
public class ExcelCellMergeHandlerProduct implements SheetWriteHandlerProductInterface<ExcelMergeParam> {

    private ExcelCellMergeHandlerProduct(){}
    private static ExcelCellMergeHandlerProduct excelCellMergeHandlerProduct;
    public static synchronized ExcelCellMergeHandlerProduct obtain(){
        if (excelCellMergeHandlerProduct ==null){
            excelCellMergeHandlerProduct = new ExcelCellMergeHandlerProduct();
        }
        return excelCellMergeHandlerProduct;
    }

    @Override
    public SheetWriteHandler product(ExcelMergeParam param) {
        Set<int[]> cellMerge = param.getCellMerge();
        return new SheetWriteHandler() {
            @Override
            public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

            }

            @Override
            public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                Sheet sheet = writeSheetHolder.getSheet();
                if (!sheet.getSheetName().equals(param.getSheetName()) || CollectionUtils.isEmpty(cellMerge)){
                    return;
                }
                cellMerge.forEach(item->{
                    try {
                        sheet.addMergedRegion(new CellRangeAddress(item[0],item[1],item[2],item[3]));
                    }catch (Exception e){
                        log.error("合并单元格失败: {}",e.getMessage());
                    }
                });
                if (!CollectionUtils.isEmpty(cellMerge)){
                    cellMerge.clear();
                }
            }
        };
    }
}
