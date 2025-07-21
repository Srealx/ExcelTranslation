package com.sre.translation.process.handler.writeHandlerProducer.product;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.sre.translation.beans.style.ExcelHeadStyle;
import com.sre.translation.process.handler.writeHandlerProducer.ibase.CellWriteHandlerProductInterface;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * excel自定义表头样式处理器
 * @author cheng
 * @date 2023/5/25
 */
@Slf4j
@EqualsAndHashCode
public class ExcelHeadStyleHandlerProduct implements CellWriteHandlerProductInterface<ExcelHeadStyle> {
    /**
     * 单例模式
     */
    private ExcelHeadStyleHandlerProduct(){}
    private static ExcelHeadStyleHandlerProduct excelHeadStyleHandlerProduct;
    public static synchronized ExcelHeadStyleHandlerProduct obtain(){
        if (excelHeadStyleHandlerProduct ==null){
            excelHeadStyleHandlerProduct = new ExcelHeadStyleHandlerProduct();
        }
        return excelHeadStyleHandlerProduct;
    }


    /**
     * 表头宽度界限值: 当表头字符串长度小于等于该值时, 会给一个较大的宽度倍率来提高显示效果
     */
    private static final int COLUMN_WIDTH_BIG_MAGNIFICATION_LIMIT = 8;
    private static final float SMALL_MAGNIFICATION = 1.3F;
    private static final float BIG_MAGNIFICATION = 2.0F;

    @Override
    public CellWriteHandler product(ExcelHeadStyle excelHeadStyle) {
        return new CellWriteHandler() {
            @Override
            public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
                String sheetName = writeSheetHolder.getSheet().getSheetName();
                if (!sheetName.equals(excelHeadStyle.getSheetName()) || head==null){
                    return;
                }
                float magnification = SMALL_MAGNIFICATION;
                if (excelHeadStyle.isSelfAdaptionWidth()){
                    // 动态表头宽度
                    // 设定8个字符以下的表头长度翻倍
                    if (head.getHeadNameList().get(0).length()<COLUMN_WIDTH_BIG_MAGNIFICATION_LIMIT){
                        magnification = BIG_MAGNIFICATION;
                    }
                    writeSheetHolder.getSheet().setColumnWidth(head.getColumnIndex(),(int)(head.getHeadNameList().get(0).getBytes().length*320*magnification));
                }else {
                    // excelHeadStyle.getWidth()* 3 * 330   用户设置的长度 * 每个汉字的长度 * 基本单元长度
                    writeSheetHolder.getSheet().setColumnWidth(head.getColumnIndex(),excelHeadStyle.getWidth()* 3 * 320);
                    // 表头文字的自动换行
                    if (excelHeadStyle.getAutoWrap() && head.getHeadNameList().get(0).length()>=excelHeadStyle.getAutoWrapPrice()){
                        String headString = head.getHeadNameList().get(0);
                        int headStringLength = head.getHeadNameList().get(0).length();
                        String firstHalf = headString.substring(0, headStringLength / 2);
                        String lastHalf = headString.substring(headStringLength / 2);
                        head.getHeadNameList().set(0,firstHalf+"\n"+lastHalf);
                    }
                }
            }

            @Override
            public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

            }

            @Override
            public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

            }

            @Override
            public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

            }
        };
    }
}
