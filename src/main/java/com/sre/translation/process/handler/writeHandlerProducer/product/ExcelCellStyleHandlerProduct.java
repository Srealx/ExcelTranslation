package com.sre.translation.process.handler.writeHandlerProducer.product;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.sre.translation.beans.style.ExcelCellStyle;
import com.sre.translation.process.handler.writeHandlerProducer.ibase.CellWriteHandlerProductInterface;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

/**
 * excel自定义全局单元格样式处理器
 * @author cheng
 * @date 2023/5/25
 */
@Slf4j
@EqualsAndHashCode
public class ExcelCellStyleHandlerProduct implements CellWriteHandlerProductInterface<ExcelCellStyle> {
    /**
     * 单例模式
     */
    private ExcelCellStyleHandlerProduct(){}
    private static ExcelCellStyleHandlerProduct excelCellStyleHandlerProduct;
    public static synchronized ExcelCellStyleHandlerProduct obtain(){
        if (excelCellStyleHandlerProduct ==null){
            excelCellStyleHandlerProduct = new ExcelCellStyleHandlerProduct();
        }
        return excelCellStyleHandlerProduct;
    }


    @Override
    public CellWriteHandler product(ExcelCellStyle excelCellStyle) {

        return new CellWriteHandler() {
            @Override
            public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {}
            @Override
            public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {
                Sheet sheet = writeSheetHolder.getSheet();
                if (sheet.getSheetName().equals(excelCellStyle.getSheetName()) && Boolean.TRUE.equals(!aBoolean)){
                    // 拿到cell
                    Workbook workbook = sheet.getWorkbook();
                    CellStyle cellStyle = workbook.createCellStyle();
                    cell.setCellStyle(cellStyle);
                    // 复制内容
                    cellStyle.setAlignment(excelCellStyle.getAlignment());
                    cellStyle.setVerticalAlignment(excelCellStyle.getVerticalAlignment());
                    cellStyle.setHidden(excelCellStyle.isHidden());
                    cellStyle.setLocked(excelCellStyle.isLocked());
                    cellStyle.setWrapText(excelCellStyle.isWrapped());
                    if (ObjectUtils.isNotEmpty(excelCellStyle.getFont())){
                        cellStyle.setFont(excelCellStyle.getFont());
                    }
                    if (ObjectUtils.isNotEmpty(excelCellStyle.getBorder())){
                        cellStyle.setBorderLeft(excelCellStyle.getBorder());
                        cellStyle.setBorderRight(excelCellStyle.getBorder());
                        cellStyle.setBorderTop(excelCellStyle.getBorder());
                        cellStyle.setBorderBottom(excelCellStyle.getBorder());
                    }

                }
            }
            @Override
            public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {}
            @Override
            public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

            }
        };
    }
}
