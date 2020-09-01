package com.wjk.pojo;

import com.wjk.TransactionType;

import java.io.Serializable;

/**
 * @program: GTM-WJK
 * @description: 事务实体类
 * 1、包含了全局事务id
 * 2、包含了当前事务的id
 * 3、当前事务的状态，
 * @author: junkang.wei
 * @create: 2020-08-23 15:35
 **/
public class TransactionPojo implements Serializable {

    /**
     * 本次事务组的id
     */
    private String groupId;
    /**
     * 本次事务的id
     */
    private String trmId;
    /**
     * 本次事务的状态
     */
    private TransactionType transactionType;


    /**
     * 任务阻塞,用来唤醒事务
     */
    private Task task;

    /**
     * 有参数的构造方法
     * @param groupId
     * @param trmId
     * @param transactionType
     */
    public TransactionPojo(String groupId, String trmId, TransactionType transactionType) {
        this.groupId = groupId;
        this.trmId = trmId;
        this.transactionType = transactionType;
        this.task = new Task ();
    }

    /**
     * 无参构造
     */
    public TransactionPojo() {
    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTrmId() {
        return trmId;
    }

    public void setTrmId(String trmId) {
        this.trmId = trmId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
