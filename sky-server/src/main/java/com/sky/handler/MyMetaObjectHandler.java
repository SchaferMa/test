/**
 * FileName: MyMetaObjectHandler
 * Author:   mayuchao
 * Date:     2024/1/10 18:57
 * Description: mp自动填充实现类
 */
package com.sky.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sky.context.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 〈功能简述〉<br>
 * 〈mp自动填充实现类〉
 * @author mayuchao
 * @create 2024/1/10
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("createUser", BaseContext.getCurrentId(),metaObject);
        this.setFieldValByName("updateUser", BaseContext.getCurrentId(),metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateUser", BaseContext.getCurrentId(),metaObject);

    }
}
