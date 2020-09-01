package com.wjk.server;

import com.alibaba.fastjson.JSON;
import com.wjk.TransactionType;

import com.wjk.cache.TransactionCache;
import com.wjk.pojo.TransactionPojo;
import com.wjk.rpc.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final ReentrantLock reentrantLock = new ReentrantLock ();


    //接受client发送的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // 将消息强转成我们自己的数据
        TransactionPojo transactionPojo = (TransactionPojo) msg;
        if (transactionPojo == null){
            System.out.println("消息内容不存在");
            return;
        }
        // 判断消息的类型
        TransactionType transactionType = transactionPojo.getTransactionType ();
        if(transactionType ==  null){
            System.out.println("消息类型不存在");
            return;
        }
        if(transactionPojo.getGroupId() == null){
            System.out.println("groupId不存在");
            return;
        }
        //如果是创建一个事务
        if(transactionType.equals(TransactionType.CREATE_TRM_GROUP)){
            reentrantLock.lock ();
            try {
                // 创建事务组
                List<RpcResponse> rpcResponses = new ArrayList<>();
                rpcResponses.add (new RpcResponse (TransactionType.NONE, transactionPojo.getTrmId (), ctx,
                        transactionPojo.getGroupId ()));
                // 将事务组数据存入缓存中
                TransactionCache.TRM_GROUP_CACHE.put (transactionPojo.getGroupId (),
                        rpcResponses);
                // 将创建事务的id,存入缓存当中，这个其实就是事务管理器的最终形态，他说回滚就胡滚，他说提交就是提交
                TransactionCache.FINAL_TRMID_CACHE.put (transactionPojo.getGroupId (), transactionPojo.getTrmId ());
                System.out.println ("创建事务组---->>>" + JSON.toJSONString (transactionType));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                reentrantLock.unlock();
            }
        }else if(transactionType.equals(TransactionType.REGISTER_TRM)){
            reentrantLock.lock();
            try {
                //如果是注册事务
                List<RpcResponse> rpcResponses = TransactionCache.TRM_GROUP_CACHE.get(transactionPojo.getGroupId());
                if(rpcResponses != null){
                    rpcResponses.add(new RpcResponse (TransactionType.REGISTER_TRM, transactionPojo.getTrmId (), ctx,
                            transactionPojo.getGroupId ()));
                }
                System.out.println ("注册事务组---->>>" + JSON.toJSONString (transactionType));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                reentrantLock.unlock();
            }
        }else if(transactionType.equals(TransactionType.COMMIT)){
            //如果是提交事务的话
            /**
             * 1. 如果是提交事务，存到缓存当中一个
             * 2. 去判断当前这个事务的状态，如果事务都提交了，就发送给客户端一个消息，提交
             * 3. 如果不是都提交了，就把这个存下来
             */
            reentrantLock.lock();
            try {
                // 判断当前事务组是否已经进行了处理，null处理
                if (TransactionCache.CURRENT_TRM_GROUP_IS_ROLLBACK.get (transactionPojo.getGroupId ()) != null &&
                        TransactionCache.CURRENT_TRM_GROUP_IS_ROLLBACK.get (transactionPojo.getGroupId ()) != null &&
                        TransactionCache.CURRENT_TRM_GROUP_IS_ROLLBACK.get (transactionPojo.getGroupId ())) {
                    System.out.println ("---->>>>>> 事务组" + transactionPojo.getGroupId () + "--已经进行了ROLLBACK...");
                    return;
                }
                System.out.println("当前的时间是:" + System.currentTimeMillis() + "--trm是:" + transactionPojo.getTrmId());
                //首先先设置当前事务的状态
                String trmId = TransactionCache.FINAL_TRMID_CACHE.get (transactionPojo.getGroupId ());
                if (trmId != null && !"".equals (trmId) && trmId.equals (transactionPojo.getTrmId ())) {
                    //判断最终一致性
                    TransactionType finalTrmType = TransactionCache.FINAL_TRM_TYPE.get (transactionPojo.getGroupId ());
                    if (finalTrmType == null || !finalTrmType.equals (TransactionType.ROLLBACK)) {
                        // 进行COMMIT
                        setOneType (transactionPojo.getGroupId (), transactionPojo.getTrmId (), TransactionType.COMMIT);
                        send (transactionPojo.getGroupId (), TransactionType.COMMIT);
                        TransactionCache.CURRENT_TRM_GROUP_IS_ROLLBACK.put (transactionPojo.getGroupId (), true);
                    } else {
                        setOneType (transactionPojo.getGroupId (), transactionPojo.getTrmId (), TransactionType.ROLLBACK);
                        // 进行ROLLBACK
                        send (transactionPojo.getGroupId (), TransactionType.ROLLBACK);
                    }
                }else{
                    //修改子事务的状态
                    setOneType (transactionPojo.getGroupId (), transactionPojo.getTrmId (), TransactionType.COMMIT);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                reentrantLock.unlock();
            }
        }else if(transactionType.equals(TransactionType.ROLLBACK)){
            /**
             * 1. 类似于提交，不过是一个回滚就回滚
             */
            reentrantLock.lock();
            try {
                Boolean aBoolean = TransactionCache.CURRENT_TRM_GROUP_IS_ROLLBACK.get(transactionPojo.getGroupId());
                if(aBoolean != null && aBoolean){
                    //判断事务是否已经执行完了，如果执行完了就在执行了
                    return;
                }
                //首先先设置当前事务的状态
                setOneType(transactionPojo.getGroupId(),transactionPojo.getTrmId(),transactionPojo.getTransactionType());
                /**
                 * 需要判断是否是最终事务发过来的ROLLBACK，如果是ROLLBACK的话才发出rollback信息
                 * 如果不是最终事务发过来的ROLLBACK只需要将这个事务组的最终状态改为ROLLBACK就行
                 */
                String trmId = TransactionCache.FINAL_TRMID_CACHE.get (transactionPojo.getGroupId ());
                if (trmId != null && !"".equals (trmId) && trmId.equals (transactionPojo.getTrmId ())) {
                    System.out.println("RM是进行回滚了:" + transactionPojo.getGroupId() + "-----TMId是:" + trmId);
                    // 告诉系统这个事务组已经进行处理
                    TransactionCache.CURRENT_TRM_GROUP_IS_ROLLBACK.put (transactionPojo.getGroupId (), true);
                    // 进行ROLLBACK
                    send (transactionPojo.getGroupId (), TransactionType.ROLLBACK);
                    System.out.println("RM是进行回滚结束了:" + transactionPojo.getGroupId() + "-----TMId是:" + trmId);
                    //其实还可以清除缓存，这边其实可以存储下来
                } else {
                    // 修改最终一致性事务的状态
                    TransactionCache.FINAL_TRM_TYPE.put (transactionPojo.getGroupId (), TransactionType.ROLLBACK);
                    System.out.println ("下游系统发出ROLLBACK指令 >>>>>>>");
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                reentrantLock.unlock();
            }
        }else {
            return;
        }

    }

    //通知处理器最后的channelRead()是当前批处理中的最后一条消息时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println ("服务端接收数据完毕..");
        ctx.flush ();
    }

    //读操作时捕获到异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close ();
    }

    //客户端去和服务端连接成功时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println ("连接成功");
    }


    /**
     * 设置当前事务的状态
     *
     * @param groupId
     * @param trmId
     * @param transactionType
     */
    private static void setOneType(String groupId, String trmId, TransactionType transactionType) {
        List<RpcResponse> rpcResponses = TransactionCache.TRM_GROUP_CACHE.get (groupId);
        if (rpcResponses != null && rpcResponses.size () > 0) {
            for (RpcResponse rpcRespons : rpcResponses) {
                if (rpcRespons.getTrmId ().equals (trmId)) {
                    rpcRespons.setOneType (transactionType);
                    break;
                }
            }
        }
    }


    /**
     * 根据groupId拿出所有的事务，然后发出类型
     *
     * @param groupId
     * @param sendType
     */
    private static void send(String groupId, TransactionType sendType) {
        // 进行COMMIT
        for (RpcResponse rpcResponse : TransactionCache.TRM_GROUP_CACHE.get (groupId)) {
            rpcResponse.setFinalTransactionType (sendType);
            rpcResponse.getChx ().writeAndFlush (rpcResponse);
            System.out.println ("发出" + sendType + "---->>>" + JSON.toJSONString (rpcResponse));
        }
    }
}
