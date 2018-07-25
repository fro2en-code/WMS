package com.ymt.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.plat.common.utils.StringUtil;

/**
 * 这里做了两件事:
 * 1 释放 RedissonClient 对象,因为相同的 RedissonClient 在不同的线程里无法锁住同一个key,所以这里每个线程都创建了 RedissonClient,需要关闭.
 * 2 根据注解锁定对象
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年9月11日
 */
public class LockHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LockHandlerInterceptorAdapter.class);
    private static final ThreadLocal<String> locks = new ThreadLocal<>();
    private static final long LOCK_TIME = 60 * 2;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 非控制器请求直接跳出
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Lock lock = handlerMethod.getMethodAnnotation(Lock.class);
        if (null == lock) {
            return true;
        }
        // 根据注解锁定操作
        LockUtils lockUnit = SpringContex.getBean("lockUtils");
        HttpSession session = request.getSession();
        String whCode = (String) session.getAttribute(StringUtil.DEFAULT_WH_CODE);
        String key = null;
        if (Lock.LOCK_request.equals(lock.type())) {
            key = String.format("lock:%s:%s", whCode, request.getParameter(lock.value()));
        } else if (Lock.LOCK_static.equals(lock.type())) {
            key = String.format("lock:%s:%s", whCode, lock.value());
        }
        lockUnit.lock(key, handlerMethod.toString(), LOCK_TIME);
        locks.set(key);
        if (logger.isDebugEnabled()) {
            logger.debug("锁定key:{}", key);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String key = locks.get();
        if (null != key) {
            if (logger.isDebugEnabled()) {
                logger.debug("释放key:{}", key);
            }
            LockUtils lockUnit = SpringContex.getBean("lockUtils");
            lockUnit.releaseLock(key);
            locks.remove();
        }
    }

}
