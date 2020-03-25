package com.github.wujiuye.datasource.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 多数据源切面
 *
 * @author wujiuye 2020/03/15
 */
@EnableAspectJAutoProxy
@Aspect
@Order(1)
public class EasyMutiDataSourceAspect {

    @Pointcut("@annotation(com.github.wujiuye.datasource.annotation.EasyMutiDataSource)")
    public void dataSourcePointCut() {

    }

    /**
     * 切换数据源
     *
     * @param point 切点
     * @return
     * @throws Throwable
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        EasyMutiDataSource ds = method.getAnnotation(EasyMutiDataSource.class);
        if (ds == null) {
            DataSourceContextHolder.setDataSource(null);
        } else {
            DataSourceContextHolder.setDataSource(ds.value());
        }
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.clearDataSource();
        }
    }

}
