package com.lzh.mybatis;

/**
 * Created by kevin.tan on 2017/8/18.
 */
public class DbContextHolder {

    public enum DbType {
        WINPOS_MASTER, WINPOS_SLAVE
    }

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public static void setDbType(DbType dbType) {
        if (dbType == null) {
            throw new NullPointerException();
        }
        contextHolder.set(dbType);
    }

    public static DbType getDbType() {
        return contextHolder.get() == null ? DbType.WINPOS_MASTER : contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }

}