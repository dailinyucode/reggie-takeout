package com.DLY.common;

/**
 * 基于ThreadLocal封装工具类，用来保存和获取当前登入用户的id
 * 以线程为范围  以线程单独保存副本
 *
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
