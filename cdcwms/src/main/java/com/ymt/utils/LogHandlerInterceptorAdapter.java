package com.ymt.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.plat.common.beans.UserBean;
import com.plat.common.utils.StringUtil;

public class LogHandlerInterceptorAdapter extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LogHandlerInterceptorAdapter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 非控制器请求直接跳出
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        WmsLog wmsLog = handlerMethod.getMethodAnnotation(WmsLog.class);
        if (null == wmsLog) {
            return true;
        }
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute(StringUtil.USER_SESSION);
        logger.info("用户:{} 请求方法:{} 相关ID:{}",
                ArrayUtils.toArray((null != user ? user.getLoginname() : request.getParameter("loginname")),
                        handlerMethod.toString(), request.getParameter(wmsLog.value())));
        return true;
    }

}
