package com.wjk.group.netty;

import com.wjk.group.localCache.TransactionCache;
import com.wjk.group.threadPoll.ThreadPool;
import com.wjk.pojo.Task;
import com.wjk.pojo.TransactionPojo;
import com.wjk.rpc.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @program: GTM-WJK
 * @description: netty客户端处理
 * 客户端处理类
 * 1.处理全局事务的通知，回滚还是提交
 * @author: junkang.wei
 * @create: 2020-08-23 15:57
 **/
public class NettyClientHandle extends SimpleChannelInboundHandler<RpcResponse> {


    /**
     * 通道是否活动开启
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("当前通道可用channelActive");
        super.channelActive (ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("当前通道出现了异常" + cause);
        ctx.close ();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) throws Exception {
        //客户端接收到服务端的请求进行处理
        /**
         * 这边使用多线程来处理请求，提高并发性...也许会有问题...待验证
         * 收到处理的情况 1.回滚
         *             2. 提交
         * 注意这边的回滚还是提交都是TM发过来的，就是TransactionType为CREATE_TRM_GROUP(创建事务组)
         */
        ThreadPool.POOL.execute (new Runnable () {
            @Override
            public void run() {
                try {
                    Thread.sleep (200);
                    response.setChx (null);
                    // 处理信息
                    TransactionPojo transactionPojo = null;

                    // 加锁获取信息
                    ReentrantReadWriteLock.ReadLock readLock = TransactionCache.rwLock.readLock ();
                    readLock.lock ();
                    try {
                        /*// 判断是否是ROLLBACK，如果是ROLLBACK的话需要将最终性的
                        if (!TransactionCache.ROLLBACK_NO_COMMIT.containsKey (response.getGroupId ()))
                            TransactionCache.ROLLBACK_NO_COMMIT.put (response.getGroupId (), response.getGroupId ());*/
                        transactionPojo = TransactionCache.TRM_POJO_CACHE.get (response.getTrmId ());
                    } catch (Exception e) {
                        e.printStackTrace ();
                    } finally {
                        readLock.unlock ();
                    }
                    transactionPojo.setTransactionType(response.getFinalTransactionType ());
                    Task task = transactionPojo.getTask ();
                    if (!task.getFlag ()) {
                        System.out.println ("先发送了消息");
                    }
                    task.signalTask ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }
        });
    }
}
