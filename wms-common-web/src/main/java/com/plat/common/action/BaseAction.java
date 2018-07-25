package com.plat.common.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.plat.common.beans.UserBean;
import com.plat.common.utils.StringUtil;

public abstract class BaseAction {
    protected Logger logger = Logger.getLogger(this.getClass());

    protected String get(HttpServletRequest req, String paramname) {
        return req.getParameter(paramname);
    }

    protected String getBindWhCode(HttpSession session) {
        String whCode = (String) session.getAttribute(StringUtil.DEFAULT_WH_CODE);
        if (StringUtils.isEmpty(whCode)) {
            throw new RuntimeException("绑定仓库不能为空");
        }
        return whCode;
    }

    protected Gson getGson() {
        return new Gson();
    }

    protected String getOpenId(HttpSession session) {
        Object obj = session.getAttribute("openId");
        if (obj != null) {
            return (String) obj;
        }
        return "";
    }

    protected String getToken(HttpSession session) {
        Object obj = session.getAttribute(StringUtil.USER_TOKEN);
        if (obj != null) {
            return (String) obj;
        }
        return "";
    }

    protected UserBean getUserInfo(HttpSession session) {
        return (UserBean) session.getAttribute(StringUtil.USER_SESSION);
    }

    @SuppressWarnings("unchecked")
    protected List<String> getWhCodes(HttpSession session) {
        List<String> list = new ArrayList<>();
        List<Map<String, String>> listMap = (List<Map<String, String>>) session.getAttribute("warehouseList");
        for (Map<String, String> map : listMap) {
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                list.add(it.next());
            }
        }
        return list;
    }

    public void print(HttpServletResponse resp, String str) {
        try {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter out = resp.getWriter();
            out.print(str);
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.error(null, e);
        }
    }

    protected void printJson(HttpServletResponse resp, String jsonStr) {
        try {
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = resp.getWriter();
            pw.print(jsonStr);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            logger.error(null, e);
        }
    }

    protected void setA(HttpServletRequest req, String attrname, Object attrval) {
        req.setAttribute(attrname, attrval);
    }
}
