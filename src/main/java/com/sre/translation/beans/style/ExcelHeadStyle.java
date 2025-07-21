package com.sre.translation.beans.style;


/**
 * 自定义表头样式
 * @author cheng
 * @date 2023/5/25
 */
public class ExcelHeadStyle extends ExcelStyleParam{
    /**
     * 自适应表头列宽
     */
    private boolean selfAdaptionWidth = true;
    /**
     * 自定义列宽
     */
    private int width;
    /**
     * 自动换行参数
     */
    private boolean autoWrap = false;
    private int autoWrapPrice = 0;

    /***
     * 自定义长度
     */
    public ExcelHeadStyle setWidth(int width){
        this.selfAdaptionWidth = false;
        this.width = width;
        return this;
    }

    /**
     * 表头自动换行(当达到一定长度时)
     */
    public ExcelHeadStyle setAutoWrap(int price){
        autoWrap = true;
        autoWrapPrice = price;
        return this;
    }

    public int getWidth(){return width;}

    public boolean getAutoWrap(){
        return autoWrap;
    }

    public int getAutoWrapPrice(){
        return autoWrapPrice;
    }

    /**
     * 私有化selfAdaptionWidth
     */
    public boolean isSelfAdaptionWidth(){return selfAdaptionWidth;}
}
