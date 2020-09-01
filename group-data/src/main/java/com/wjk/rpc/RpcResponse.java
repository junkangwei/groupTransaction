package com.wjk.rpc;

import com.wjk.TransactionType;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * @program: GTM-WJK
 * @description: nettyServer返回的对象
 * @author: junkang.wei
 * @create: 2020-08-23 15:35
 **/
public class RpcResponse implements Serializable {


    /**
     * 本次事务的最终状态，这是迎合全部的一个状态
     */
    private TransactionType finalTransactionType;

    /**
     * 他自己的一个状态
     */
    private TransactionType oneType = TransactionType.COMMIT;
    /**
     * 本次事务的id
     */
    private String trmId;
    /**
     * 通讯的管道
     */
    private ChannelHandlerContext chx;
    /**
     * 这次事务的分组ID
     */
    private String groupId;

    public RpcResponse(TransactionType finalTransactionType, String trmId, ChannelHandlerContext chx, String groupId) {
        this.finalTransactionType = finalTransactionType;
        this.trmId = trmId;
        this.chx = chx;
        this.groupId = groupId;
    }

    public TransactionType getOneType() {
        return oneType;
    }

    public void setOneType(TransactionType oneType) {
        this.oneType = oneType;
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

    public ChannelHandlerContext getChx() {
        return chx;
    }

    public void setChx(ChannelHandlerContext chx) {
        this.chx = chx;
    }

    public TransactionType getFinalTransactionType() {
        return finalTransactionType;
    }

    public void setFinalTransactionType(TransactionType finalTransactionType) {
        this.finalTransactionType = finalTransactionType;
    }
}
