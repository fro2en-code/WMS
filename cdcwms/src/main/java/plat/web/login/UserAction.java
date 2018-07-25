package plat.web.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.Attr;
import com.plat.common.beans.UserBean;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserWarehouse;
import com.ymt.utils.UserUtils;

import cn.rtzltech.user.api.PlatKeyApi;
import cn.rtzltech.user.api.RoleApi;
import cn.rtzltech.user.api.UserApi;
import cn.rtzltech.user.model.Plat_Key;
import cn.rtzltech.user.model.Plat_Role;
import cn.rtzltech.user.model.Plat_User;
import cn.rtzltech.user.page.PageData;
import cn.rtzltech.user.result.ResultResp;
import cn.rtzltech.user.result.ResultRespT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import wms.business.biz.UserBiz;

@Controller
@RequestMapping(value = "/user")
public class UserAction extends BaseAction {
    @Resource
    private Attr attr;
    @Resource
    private PlatKeyApi platKeyApi;
    @Resource
    private RoleApi roleApi;
    @Resource
    private UserApi userApi;
    @Resource
    private UserBiz userBiz;

    @Resource
    private UserUtils userUtils;

    private ResultResp changeState(String loginname, int state) {
        Map<String, Object> params = new HashMap<>();
        params.put("platKey", attr.getWmsMark());
        params.put("loginname", loginname);
        params.put("state", state);
        return userApi.updateUserState(params);
    }

    /**
     *
     * 删除用户
     */
    @RequestMapping("/del.action")
    public void del(HttpServletResponse resp, HttpSession session, String loginname) {
        try {
            UserBean u = getUserInfo(session);
            Map<String, Object> params = new HashMap<>();
            params.put("creator", u.getLoginname());
            params.put("loginname", loginname);
            ResultResp ret = userApi.delPlatUser(params);
            print(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 停用用户
     */
    @RequestMapping("/disable_user.action")
    public void disable(HttpServletResponse resp, HttpSession session, String loginname) {
        try {
            UserBean u = getUserInfo(session);
            Map<String, Object> params = new HashMap<>();
            params.put("platKey", attr.getWmsMark());
            params.put("loginname", loginname);
            params.put("state", 1);
            params.put("creator", u.getLoginname());
            ResultResp ret = userApi.updateUserState(params);
            print(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/disable.action")
    @ResponseBody
    public ResultResp disable(String loginname) {
        return changeState(loginname, 0);
    }

    /**
     *
     * 启用用户
     */
    @RequestMapping("/enabled_user.action")
    public void enabled(HttpServletResponse resp, HttpSession session, String loginname) {
        try {
            UserBean u = getUserInfo(session);
            Map<String, Object> params = new HashMap<>();
            params.put("platKey", attr.getWmsMark());
            params.put("loginname", loginname);
            params.put("state", 0);
            params.put("creator", u.getLoginname());
            ResultResp ret = userApi.updateUserState(params);
            print(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/enabled.action")
    @ResponseBody
    public ResultResp enabled(String loginname) {
        return changeState(loginname, 0);
    }

    @RequestMapping("/getPlatKeyList.action")
    public void getPlatKeyList(HttpServletResponse resp, HttpSession session) {
        try {
            UserBean u = getUserInfo(session);
            ResultRespT<List<Plat_Key>> ret = platKeyApi.getPlatKeyListByLoginname(u.getLoginname());
            if (!ret.isSuc()) {
                logger.error(ret.getRetmsg());
                return;
            }
            printJson(resp, JSONArray.fromObject(ret.getT()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getRolesByUser.action")
    @ResponseBody
    public List<String> getRolesByUser(String loginName) {
        List<String> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("platKey", attr.getWmsMark());
        params.put("loginname", loginName);
        cn.rtzltech.user.result.ResultRespT<List<Plat_Role>> result = roleApi.getUserRoleList(params);
        if (result.isSuc()) {
            for (Plat_Role role : result.getT()) {
                list.add(role.getId());
            }
        }
        return list;
    }

    @RequestMapping(value = "/list.action")
    @ResponseBody
    public PageData<UserWarehouse> getUserPageData(HttpServletResponse resp, HttpSession session, int page, int rows,
            UserWarehouse userWarehouse) {
        userWarehouse.setWhCode(getBindWhCode(session));
        return userBiz.getUserPageData(page, rows, userWarehouse);
    }

    @RequestMapping("/modifyPwd.action")
    @ResponseBody
    public ResultResp modifyPwd(HttpSession session, String loginname, String pwd, String newPwd, String confirmPwd) {
        Map<String, Object> params = new HashMap<>();
        params.put("platKey", attr.getWmsMark());
        UserBean user = getUserInfo(session);
        params.put("loginname", user.getLoginname());
        params.put("pwd", pwd);
        params.put("newPwd", newPwd);
        return userApi.modifyMyPwd(params);
    }

    @RequestMapping("/resetPass.action")
    @ResponseBody
    public ResultResp resetPass(String loginname, String pwd) {
        Map<String, Object> params = new HashMap<>();
        params.put("platKey", attr.getWmsMark());
        params.put("loginname", loginname);
        params.put("newPwd", pwd);
        return userApi.modifyUserPwd(params);
    }

    /**
     * 新增、修改用户
     */
    @RequestMapping(value = "/save_user.action", method = RequestMethod.POST)
    public void save(HttpServletResponse resp, HttpSession session, Plat_User u) {
        try {
            UserBean userInfo = getUserInfo(session);
            u.setInsertUser(userInfo.getLoginname());
            u.setUpdateUser(userInfo.getLoginname());
            String id = u.getId();
            int operType = 0;
            if (!StringUtil.isEmpty(id)) {
                operType = 1;
            }
            Map<String, Object> params = new HashMap<>();

            params.put("roleIdList", Arrays.asList(u.getUserRoles().split(",")));
            params.put("loginname", userInfo.getLoginname());
            params.put("operType", operType);
            params.put("userInfo", u);
            ResultResp ret = userApi.saveRoleUser(params);
            print(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/save.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultResp save(HttpSession session, Plat_User u) {
        UserBean userInfo = getUserInfo(session);
        u.setInsertUser(userInfo.getLoginname());
        u.setUpdateUser(userInfo.getLoginname());
        String id = u.getId();
        int operType = 0;
        if (!StringUtil.isEmpty(id)) {
            operType = 1;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("roleIdList", u.getUserRoles());
        params.put("loginname", userInfo.getLoginname());
        params.put("operType", operType);
        params.put("userInfo", u);
        return userApi.saveRoleUser(params);
    }

    @RequestMapping(value = "/toList.action")
    public String toList() {
        return "userList";
    }

}