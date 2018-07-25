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

import com.plat.common.action.BaseAction;
import com.plat.common.beans.Attr;
import com.plat.common.beans.UserBean;
import com.plat.common.utils.StringUtil;

import cn.rtzltech.user.api.MenuApi;
import cn.rtzltech.user.api.RoleApi;
import cn.rtzltech.user.bean.Tree;
import cn.rtzltech.user.model.Plat_Menu;
import cn.rtzltech.user.model.Plat_Role;
import cn.rtzltech.user.page.PageData;
import cn.rtzltech.user.result.ResultResp;
import cn.rtzltech.user.result.ResultRespT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import wms.business.biz.UserBiz;

@Controller
@RequestMapping("/role")
public class RoleAction extends BaseAction {

    /**
     * 角色管理页面
     */
    @RequestMapping("toList.action")
    public String toList() {
        return "roleList";
    }

    @Resource
    private UserBiz userBiz;

    /**
     * 角色列表
     */
    @RequestMapping("/list.action")
    public void list(HttpServletResponse resp, int page, int rows, Plat_Role r) {
        try {
            List<String> pkList = new ArrayList<String>();
            pkList.add(attr.getWmsMark());
            Map<String, Object> params = new HashMap<>();
            params.put("pkList", pkList);
            params.put("searchKey", r.getName());
            ResultRespT<PageData<Plat_Role>> ret = roleApi.getRolePageData(page, rows, params);
            if (!ret.isSuc()) {
                logger.error(ret.getRetmsg());
                printJson(resp, JSONObject.fromObject(new PageData<>()).toString());
                return;
            }
            PageData<Plat_Role> pageData = ret.getT();
            printJson(resp, JSONObject.fromObject(pageData).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 保存、修改角色
     */
    @RequestMapping(value = "/save.action", method = RequestMethod.POST)
    public void save(HttpServletResponse resp, HttpSession session, String platKey, Plat_Role r) {
        try {
            UserBean userInfo = getUserInfo(session);
            String loginname = userInfo.getLoginname();
            r.setInsertUser(loginname);
            r.setUpdateUser(loginname);
            Map<String, Object> params = new HashMap<>();
            params.put("platKey", platKey);
            params.put("loginname", loginname);
            params.put("roleInfo", r);
            ResultResp ret = roleApi.savePlatRole(params);
            print(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @RequestMapping("/del.action")
    public void del(HttpServletResponse resp, String id) {
        try {
            ResultResp ret = roleApi.delUserRoleByRoleId(id);
            print(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping("/saveRoleMenu.action")
    public void saveRoleMenu(HttpServletResponse resp, String roleId, String menuIds) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("roleId", roleId);
            List<String> menuIdList = new ArrayList<>();
            if (!StringUtil.isEmpty(menuIds)) {
                menuIdList = Arrays.asList(menuIds.split(","));
            }
            params.put("menuIdList", menuIdList);
            ResultResp ret = roleApi.savePlatRoleMenuRela(params);
            print(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping("/menuSort.action")
    public void menuSort(HttpServletResponse resp, int sortFlag, String roleId, String menuId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("sortFlag", sortFlag);
            params.put("roleId", roleId);
            params.put("menuId", menuId);
            ResultResp ret = menuApi.sortMenusForRole(params);
            printJson(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping("/getRoleListByPlat.action")
    public void getRoleListByPlat(HttpServletResponse resp) {
        try {
            List<String> pkList = new ArrayList<String>();
            pkList.add(attr.getWmsMark());
            Map<String, Object> params = new HashMap<>();
            params.put("pkList", pkList);
            ResultRespT<PageData<Plat_Role>> ret = roleApi.getRolePageData(1, 100, params);
            if (!ret.isSuc()) {
                logger.error(ret.getRetmsg());
                return;
            }
            printJson(resp, JSONArray.fromObject(ret.getT().getRows()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getMenuListByRole.action")
    public void getMenuListByRole(HttpServletResponse resp, int page, int rows, String roleId) {
        try {
            ResultRespT<PageData<Plat_Menu>> ret = menuApi.getPlatMenuByRoleId(page, rows, roleId);
            if (!ret.isSuc()) {
                logger.error(ret.getRetmsg());
                return;
            }
            printJson(resp, JSONObject.fromObject(ret.getT()).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取平台菜单树结构
     *
     * @param resp
     */
    @RequestMapping("/getMenuTree.action")
    public void getMenuTree(HttpServletResponse resp, String roleId) {
        try {
            ResultRespT<List<Tree>> ret = menuApi.getMenuTreeByRoleId(roleId);
            if (!ret.isSuc()) {
                return;
            }
            printJson(resp, JSONArray.fromObject(ret.getT()).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping("/delRoleMenu.action")
    public void delRoleMenu(HttpServletResponse resp, String roleId, String menuId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("roleId", roleId);
            params.put("menuId", menuId);
            ResultResp ret = roleApi.delPlatRoleMenuRela(params);
            printJson(resp, JSONObject.fromObject(ret).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Resource
    private RoleApi roleApi;
    @Resource
    private MenuApi menuApi;
    @Resource
    private Attr attr;
}
