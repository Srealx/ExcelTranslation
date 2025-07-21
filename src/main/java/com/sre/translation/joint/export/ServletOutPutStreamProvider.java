package com.sre.translation.joint.export;

import com.sre.translation.beans.ExcelTranslationExportParam;
import com.sre.translation.eumn.ExcelModeEnum;
import com.sre.translation.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author chen gang
 * @date 2025/4/18
 */
@Slf4j
@Component("servletOutPutStreamProvider")
public class ServletOutPutStreamProvider implements IExportOutputStreamProvider{
    @Resource
    HttpServletResponse response;

    @Override
    public OutputStream getOutputStream(ExcelTranslationExportParam param, String fileName ,ExcelModeEnum excelModeEnum) {
        if (excelModeEnum == ExcelModeEnum.SY){
            return  ExcelUtil.initOutPutStream(response, fileName);
        }
        return null;
    }
}
