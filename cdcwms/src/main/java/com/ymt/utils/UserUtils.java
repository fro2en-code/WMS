package com.ymt.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.plat.common.beans.Attr;
import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultRespT;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserSessionID;
import com.wms.userRelation.UserWarehouse;

import cn.rtzltech.user.api.RoleApi;
import cn.rtzltech.user.api.UserApi;
import cn.rtzltech.user.model.Plat_User;
import cn.rtzltech.user.page.PageData;
import wms.business.biz.UserBiz;

@Service("userUtils")
public class UserUtils {
    // private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);
    @Resource
    private Attr attr;
    @Value("${reciverRole}")
    private String reciverRole;
    @Resource
    private RoleApi roleApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserApi userApi;
    @Resource
    private UserBiz userBiz;

    public String getLoginNameByTid(String tid) {
        return userBiz.getLoginNameByTid(tid);
    }

    /**
     * 根据登录名称获取角色列表
     *
     * @param loginName
     */
    public List<String> getRolesByUser(String loginName) {
        return userBiz.getRolesByUser(loginName);
    }

    /**
     * 根据角色获取角色下属用户
     */
    public List<String> getUserByRole(String roleName) {
        List<String> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("platKey", attr.getWmsMark());
        params.put("roleName", roleName);
        cn.rtzltech.user.result.ResultRespT<List<Plat_User>> result = userApi.getPlatUserByRoleName(params);
        if (result.isSuc()) {
            List<Plat_User> users = result.getT();
            for (Plat_User user : users) {
                list.add(user.getLoginname());
            }
        }
        // 这里暂时不控制是否已登录,如果需要,以后补充
        return list;
    }

    /**
     * 获取平台用户列表
     */
    public PageData<Plat_User> getUserPageData(int page, int rows, String searchKey) {
        Map<String, Object> params = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(attr.getWmsMark());
        params.put("pkList", list);
        if (null != searchKey) {
            params.put("searchKey", searchKey);
        }
        return userApi.getUserPageData(page, rows, params).getT();
    }

    /**
     * 是否为收货员
     */
    public boolean isReciver(String loginName) {
        List<String> roles = getRolesByUser(loginName);
        for (String string : roles) {
            if (reciverRole.equals(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据用户名登录
     */
    public ResultRespT<Plat_User> login(HttpSession session, String loginName) {
        Map<String, Object> params = new HashMap<>();
        params.put("platKey", attr.getWmsMark());
        params.put("loginname", loginName);
        ResultRespT<Plat_User> result = new ResultRespT<>(userApi.loginByL(params));
        sessionRefash(session, result);
        return result;
    }

    /**
     * 根据用户名,密码登录
     */
    public ResultRespT<Plat_User> login(HttpSession session, String loginName, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("platKey", attr.getWmsMark());
        params.put("loginname", loginName);
        params.put("pwd", password);
        ResultRespT<Plat_User> result = new ResultRespT<>(userApi.loginByLP(params));
        sessionRefash(session, result);
        return result;
    }

    /**
     * 删除指定用户session
     */
    public void remvoeSession(String userName) {
        UserSessionID userSessionID = userBiz.getUserInfo(userName);
        if (null == userSessionID) {
            return;
        }
        String oldsessonid = userSessionID.getSessionId();
        if (null != oldsessonid) {
            stringRedisTemplate.delete("spring:session:sessions:" + oldsessonid);
        }
    }

    /**
     * 刷新session信息
     */
    public void sessionRefash(HttpSession session, ResultRespT<Plat_User> result) {
        if (null == result || !"0".equals(result.getRetcode())) {
            return;
        }
        // 登录成功,更新session
        Plat_User user = result.getT();
        String sessionid = null;
        if (null != session) {
            sessionid = session.getId();
        }
        UserSessionID userSessionID = userBiz.getUserInfo(user.getLoginname());
        if (null == userSessionID) {
            userSessionID = new UserSessionID();
            userSessionID.setUserId(user.getId());
            userSessionID.setUserLoginname(user.getLoginname());
        } else {
            // redis删除旧session
            String oldsessonid = userSessionID.getSessionId();
            if (null != oldsessonid) {
                stringRedisTemplate.delete("spring:session:sessions:" + oldsessonid);
            }
        }
        userSessionID.setSessionId(sessionid);
        userBiz.saveOrUpdateEntity(userSessionID);
        // 写入Session
        if (null != session) {
            // 获取该用户绑定的仓库
            List<UserWarehouse> warehouseList = userBiz.getWarehouseMap(user.getLoginname());
            if (warehouseList.size() == 0) {
                throw new RuntimeException("当前用户未绑定仓库");
            }
            UserWarehouse userWarehouse = warehouseList.get(0);
            session.setAttribute(StringUtil.DEFAULT_WH_CODE, userWarehouse.getWhCode());
            session.setAttribute(StringUtil.DEFAULT_WH_NAME, userWarehouse.getWhName());

            UserBean userBean = new UserBean();
            userBean.setLoginname(user.getLoginname());
            userBean.setTruename(user.getTruename());
            userBean.setJsonMenuTree(user.getJsonMenuTree());
            userBean.setJsonMenuBtn(user.getJsonMenuBtn());
            session.setAttribute(StringUtil.USER_AUTH, userBean.getJsonMenuTree());
            session.setAttribute(StringUtil.USER_BUTTON, userBean.getJsonMenuBtn());
            session.setAttribute(StringUtil.USER_SESSION, userBean);
        }
    }
}
