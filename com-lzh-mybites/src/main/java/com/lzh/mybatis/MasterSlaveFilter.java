package com.lzh.mybatis;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by kevin.tan on 2017/8/22.
 */

@Aspect
@Component
public class MasterSlaveFilter implements Ordered {


    @Around("")
    public Object process(ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DbContextHolder.setDbType(DbContextHolder.DbType.WINPOS_SLAVE);
        if (method.isAnnotationPresent(WinposMaster.class)) {
            DbContextHolder.setDbType(DbContextHolder.DbType.WINPOS_MASTER);
        }
        try {
            return point.proceed();
        } finally {
            DbContextHolder.clearDbType();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }


}
