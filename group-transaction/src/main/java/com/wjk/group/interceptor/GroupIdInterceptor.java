package com.wjk.group.interceptor;

import com.wjk.group.localCache.TransactionCache;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: GTM-WJK
 * @description: 简单的拦截器，拿到groupId
 * @author: junkang.wei
 * @create: 2020年08月24日14:06:24
 **/
public class GroupIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取到本次的一个路径 供后面调用错误时检查
        String requestURL = request.getRequestURL ().toString ();
        System.out.println("当前请求的url是" + requestURL);
        // 从头部获取groupId
        String groupId = request.getHeader ("GTM_GROUP_ID");
        if (groupId == null) {
            groupId = request.getParameter ("GTM_GROUP_ID");
        }
        TransactionCache.CURRENT_GROUP_ID.set (groupId);
        return true;
    }
}
