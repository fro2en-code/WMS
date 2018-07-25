package com.plat.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;

import net.sf.json.JSONObject;

public class WebInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object chain, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object chain, ModelAndView mv)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object chain) throws Exception {
        HttpSession session = req.getSession();
        UserBean user = (UserBean) session.getAttribute(StringUtil.USER_SESSION);
        if (user == null) {
            String agent = req.getHeader("User-Agent");
            if (StringUtil.isEmpty(agent)) {
                return false;
            }
            if (agent.toLowerCase().indexOf("android") > -1 || agent.toLowerCase().indexOf("iphone") > -1) {
                ResultResp ret = new ResultResp();
                ret.setRetcode("99");
                ret.setRetmsg("会话已失效,请退出应用重新登录。");
                PrintWriter pw = resp.getWriter();
                pw.print(JSONObject.fromObject(ret).toString());
                pw.flush();
                pw.close();
            } else {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            }
            return false;
        }
        return true;
    }
}
