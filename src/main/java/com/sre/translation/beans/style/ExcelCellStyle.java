package com.sre.translation.beans.style;

import lombok.Data;
import org.apache.poi.ss.usermodel.*;

/**
 * 自定义单元格风格
 * @author cheng
 * @date 2023/5/25
 */
@Data
public class ExcelCellStyle extends ExcelStyleParam{
    /**
     * 对齐方式(默认水平垂直居中对齐)
     */
    private HorizontalAlignment alignment = HorizontalAlignment.CENTER;
    private VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;
    /**
     * 字体
     */
    private Font font;
    /**
     * 边框(上下左右统一样式)
     */
    private BorderStyle border;
    /**
     * 是否隐藏
     */
    private boolean hidden = false;
    /**
     * 是否锁定单元格
     */
    private boolean locked = false;
    /**
     * 是否自动换行
     */
    private boolean wrapped = false;
}
