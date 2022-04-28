package com.DLY.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 用来对规定字段的自动填充  自定义元数据对象处理器
 */
@Component
public class MyMetaObjecthandler implements MetaObjectHandler {

    /**
     * 在数据插入时进行自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    /**
     * 在数据更新时进行自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
