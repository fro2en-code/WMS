package wms.web.userRelation.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserWarehouse;
import com.wms.warehouse.WmsWarehouse;
import com.ymt.utils.DockUtils;
import com.ymt.utils.UserUtils;

import cn.rtzltech.user.model.Plat_User;
import cn.rtzltech.user.page.PageData;
import wms.business.biz.UserBiz;
import wms.business.biz.WarehouseManagementBiz;

/**
 * 用户仓库关系
 */
@Controller
@RequestMapping("/userBindWh")
public class UserWarehouseAction extends BaseAction {

    @Resource
    private DockUtils dockUtils;
    @Resource
    private UserUtils userUtils;

    @Resource
    private UserBiz userBiz;

    @Resource
    private WarehouseManagementBiz warehouseManagementBiz;

    /**
     * 删除仓库绑定
     *
     * @return
     */
    @RequestMapping("/del.action")
    @ResponseBody
    public ResultResp del(HttpServletResponse resp, HttpSession session, String id) {
        UserWarehouse userWarehouse = userBiz.getUserWarehouseEntity(id);
        ResultResp result = userBiz.delUserWarehouse(id);
        UserBean ub = getUserInfo(session);
        if (ub.getLoginname().equals(userWarehouse.getUserLoginname())) {// 操作自己
            session.invalidate();
        } else {
            userUtils.remvoeSession(userWarehouse.getUserLoginname());
        }
        return result;
    }

    /**
     * 获取平台用户
     *
     * @return
     */
    @RequestMapping("/getPageUser.action")
    @ResponseBody
    public PageData<Plat_User> getPageUser(HttpServletResponse resp, HttpSession session, int page, int rows) {
        return userUtils.getUserPageData(page, rows, null);
    }

    /**
     * 获取用户绑定的仓库列表
     *
     * @return
     */
    @RequestMapping("/getPageWarehouse.action")
    @ResponseBody
    public PageData<UserWarehouse> getPageWarehouse(HttpServletResponse resp, HttpSession session, int page, int rows,
            String userLoginname) {
        return userBiz.getUserWarehousePageData(page, rows, userLoginname);
    }

    /**
     * 保存仓库绑定
     *
     * @return
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public ResultResp save(HttpServletResponse resp, HttpSession session, UserWarehouse userWarehouse) {
        UserBean ub = getUserInfo(session);
        userWarehouse.setInsertUser(ub.getLoginname());
        userWarehouse.setUpdateUser(ub.getLoginname());// 插入当前登陆用户名
        userWarehouse.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));// 插入当前时间
        WmsWarehouse warehouse = warehouseManagementBiz.getWmsByCode(userWarehouse.getWhCode());
        userWarehouse.setWhName(warehouse.getWhName());
        ResultResp result = userBiz.save(userWarehouse);
        if (ub.getLoginname().equals(userWarehouse.getUserLoginname())) {// 操作自己
            session.invalidate();
        } else {
            userUtils.remvoeSession(userWarehouse.getUserLoginname());
        }
        return result;
    }

    /**
     * 删除当前默认仓库
     *
     * @return
     */
    @RequestMapping("/setWarehouse.action")
    @ResponseBody
    public ResultResp setWarehouse(HttpServletResponse response, HttpSession session, String whCode, String whName) {
        ResultResp re = new ResultResp();
        if (StringUtil.isEmpty(whCode) || StringUtil.isEmpty(whName)) {
            re.setRetcode("-1");
            re.setRetmsg("接收的参数非法！");
        } else {
            session.setAttribute("defaultWhCode", whCode);
            session.setAttribute("defaultWhName", whName);
            // 移除绑定库位
            UserBean user = getUserInfo(session);
            session.removeAttribute(StringUtil.DEFAULT_DOCK);
            dockUtils.bindWmsDock(null, user.getLoginname());
            dockUtils.defaultBindWmsDock(session, user.getLoginname(), whCode);
            //
            re.setRetcode("0");
            re.setRetmsg("设置成功！");
        }
        return re;
    }

    /**
     * 渲染页面
     */
    @RequestMapping(value = "/toList.action")
    public String toList() {
        return "userBindWarehouse";
    }
}
