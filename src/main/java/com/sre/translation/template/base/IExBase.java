package com.sre.translation.template.base;


/**
 * export抽象接口
 * @author cheng
 * @date 2023/5/16
 */
public interface IExBase extends IExcelBusinessBase{
    /**
     * 初始化导出文件名
     * @return fileName
     */
    String getFileName();
}
