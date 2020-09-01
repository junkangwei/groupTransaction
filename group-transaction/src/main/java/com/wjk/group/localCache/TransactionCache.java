package com.wjk.group.localCache;

import com.wjk.TransactionType;
import com.wjk.pojo.TransactionPojo;
import com.wjk.rpc.RpcResponse;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @program: GTM-WJK
 * @description: 本地缓存
 * @author: junkang.wei
 * @create: 2020-08-23 16:05
 **/
public class TransactionCache {

    private static TransactionCache ourInstance = new TransactionCache ();

    public static TransactionCache getInstance() {
        return ourInstance;
    }

    private TransactionCache() {

    }

    /**
     * 当前线程的ThreadLocal
     */
    public static final ThreadLocal<TransactionPojo> CURRENT_TD =
            new ThreadLocal<> ();

    /**
     * 事务组的最终状态
     * groupId -> type
     */
    public static final Map<String, TransactionType> FINAL_TRM_TYPE =
            new ConcurrentHashMap<String, TransactionType> ();

    /**
     * 事务缓存
     * 主要用于后面根据事务id取出本次的数据
     */
    public static final Map<String, TransactionPojo> TRM_POJO_CACHE =
            new ConcurrentHashMap<>();


    /**
     * 存放着本次事务组的所有通讯管道
     * 主要用于，出现了回滚或者最终的COMMIT，需要通过事务组Id获取到所有的通讯管道，并发出对应的消息
     * group->RpcResponse
     */
    public static final Map<String, List<RpcResponse>> TRM_GROUP_CACHE =
            new ConcurrentHashMap<String, List<RpcResponse>> ();


    /**
     * 最终的事务id
     * 主要用于，区分是否是最终的一个COMMIT_ID
     * groupId->trmId
     */
    public static final Map<String, String> FINAL_TRMID_CACHE =
            new ConcurrentHashMap<String, String> ();


    /**
     * 当前事务组是否已经进行了ROLLBACK
     * groupId -> boolean
     */
    public static final Map<String, Boolean> CURRENT_TRM_GROUP_IS_ROLLBACK =
            new ConcurrentHashMap<String, Boolean> ();


    /**
     * 当前线程的groupId
     */
    public static final ThreadLocal<String> CURRENT_GROUP_ID =
            new ThreadLocal<> ();

    /**
     * 读写锁
     */
    public static final ReentrantReadWriteLock rwLock =
            new ReentrantReadWriteLock ();

    /**
     * 事务的最终性ID
     */
    public static final Map<String, Boolean> FINAL_TRM_ID =
            new ConcurrentHashMap<> ();

    /**
     * spring容器
     */
    public static ApplicationContext SPRING_CONTEXT;
}
