package com.wjk;

import java.io.Serializable;

/**
 * @program: GTM-WJK
 * @description: 事务的状态
 * @author: junkang.wei
 * @create: 2020-08-23 15:36
 **/
public enum TransactionType implements Serializable {


    /**
     * 创建事务组
     */
    CREATE_TRM_GROUP,
    /**
     * 注册事务
     */
    REGISTER_TRM,
    /**
     * 提交事务
     */
    COMMIT,
    /**
     * 回滚事务
     */
    ROLLBACK,
    /**
     * 没有事务
     */
    NONE;
}
