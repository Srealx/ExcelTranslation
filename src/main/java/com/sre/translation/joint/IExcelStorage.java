package com.sre.translation.joint;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author chen gang
 * @date 2025/4/9
 */
public interface IExcelStorage {
    /**
     * 获取存储输出流
     * @param fileName 文件名
     * @return {@link OutputStream}
     */
    OutputStream getOutputStream(String fileName);

    /**
     * 根据文件名获取文件输入流
     * @param fileName 文件名
     * @return {@link InputStream}
     */
    InputStream getFileInputStream(String fileName);

    /**
     * 获取存储公开路径
     * @return {@link String}
     */
    String getStoragePublicPath();
}
