package com.sre.translation.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * excel工具类
 * @author cheng
 * @date 2023/5/22
 */
@Component
@Slf4j
public class ExcelUtil {
    private ExcelUtil() {}

    /**
     * 简单表头转为标准表头格式
     */
    public static List<List<String>> changeHead(String[] easyHead){
        List<List<String>> list = new ArrayList<>();
        for (String item : easyHead) {
            List<String> headUnit = new ArrayList<>();
            headUnit.add(item);
            list.add(headUnit);
        }
        return list;
    }

    /**
     * 文件名置为UTF8字符集
     */
    public static String fileNameUTF8(String fileName){
        try {
            return URLEncoder.encode(fileName,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return fileName;
        }
    }
    /**
     * 获取输出流
     */
    public static ServletOutputStream initOutPutStream(HttpServletResponse response,String fileName){
        String fileNameUtf8 = ExcelUtil.fileNameUTF8(fileName);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileNameUtf8 + ".xlsx");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            log.error("============>>>>>>>>>>>     获取输出流失败");
            e.printStackTrace();
            return null;
        }
        return outputStream;
    }


}
