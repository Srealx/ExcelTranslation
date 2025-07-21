package com.sre.translation.template.result.dto;

import java.util.function.Function;

/**
 * 导入结果对象
 * @author chen gang
 * @date 2025/4/9
 */
public abstract class ImportResultDTO {
    protected ImportResultDTO(){}

    /**
     * 将结果对象转为string，算法由外界提供
     */
    public String toResultString(Function<? super ImportResultDTO,String> function){
        return function.apply(this);
    }
}
