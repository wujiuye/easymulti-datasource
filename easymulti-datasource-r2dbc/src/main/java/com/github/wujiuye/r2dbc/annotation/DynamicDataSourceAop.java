package com.github.wujiuye.r2dbc.annotation;

import com.github.wujiuye.r2dbc.EasyMutiR2dbcRoutingConnectionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * r2dbc动态数据源切面
 *
 * @author wujiuye 2020/11/03
 */
@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicDataSourceAop {

    @Pointcut(value = "@annotation(com.github.wujiuye.r2dbc.annotation.R2dbcDataBase)")
    public void point() {
    }

    @Around(value = "point()")
    public Object aroudAop(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        R2dbcDataBase dataSource = method.getAnnotation(R2dbcDataBase.class);
        if (method.getReturnType() == Mono.class) {
            return EasyMutiR2dbcRoutingConnectionFactory.putDataSource((Mono<?>) pjp.proceed(), dataSource.value());
        } else {
            return EasyMutiR2dbcRoutingConnectionFactory.putDataSource((Flux<?>) pjp.proceed(), dataSource.value());
        }
    }

}
