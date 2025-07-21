package com.sre.translation.utils;

import com.fasterxml.uuid.Generators;

/**
 * uuid工具类
 * @author cheng
 * @date 2023/9/12
 */
public class UuidUtils {

    public static String genFileUUid(){
        return Generators.timeBasedGenerator().generate().toString();
    }

}
