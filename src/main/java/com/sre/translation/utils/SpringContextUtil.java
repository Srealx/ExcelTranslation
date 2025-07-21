package com.sre.translation.utils;

import com.sre.translation.annotation.ExcelBusinessCode;
import com.sre.translation.exception.ExcelConductAnnotationException;
import com.sre.translation.template.base.IExcelBusinessBase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * spring context 工具类
 * @author cheng
 * @date 2023
 */
@Component("jar-ExcelTranslation/springContextUtil")
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String beanId) throws BeansException {
        return applicationContext.getBean(beanId);
    }

    public static Object getBeansWithAnnotation(Class<? extends Annotation> annotation) throws BeansException {
        return applicationContext.getBeansWithAnnotation(annotation);
    }

    public static <T> T getBeansWithAnnotation(Class<T> manager, Class<? extends ExcelBusinessCode> annotation, Long code){
        if (ObjectUtils.isEmpty(code)){
            throw new ExcelConductAnnotationException("Excel业务code枚举配置异常");
        }
        Collection<T> tCollection = applicationContext.getBeansOfType(manager).values();
        for (T t : tCollection) {
            ExcelBusinessCode excelBusinessCode = t.getClass().getAnnotation(annotation);
            if (ObjectUtils.isEmpty(excelBusinessCode)) {
                continue;
            }
            if (code.equals(excelBusinessCode.value())) {
                return t;
            }
        }
        throw new ExcelConductAnnotationException("未找到该code修饰的实现:  " + code);
    }

    public static <T> T getBeansWithExcelConductAnnotation(Class<T> manager, Long code) throws BeansException {
        return getBeansWithAnnotation(manager, ExcelBusinessCode.class, code);
    }

    public static String getBusinessName(Long businessCode){
        if (ObjectUtils.isEmpty(businessCode)){
            throw new ExcelConductAnnotationException("Excel业务code枚举配置异常");
        }
        Collection<IExcelBusinessBase> collection = applicationContext.getBeansOfType(IExcelBusinessBase.class).values();
        for (IExcelBusinessBase iExcelBusinessBase : collection) {
            ExcelBusinessCode excelBusinessCode = iExcelBusinessBase.getClass().getAnnotation(ExcelBusinessCode.class);
            if (ObjectUtils.isEmpty(excelBusinessCode)) {
                continue;
            }
            if (businessCode.equals(excelBusinessCode.value())) {
                return excelBusinessCode.businessName();
            }
        }
        throw new ExcelConductAnnotationException("未找到该code修饰的实现:  " + businessCode);
    }

}
