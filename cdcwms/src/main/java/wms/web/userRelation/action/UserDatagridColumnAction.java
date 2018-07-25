package wms.web.userRelation.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.userRelation.UserDatagridColumn;

import wms.business.biz.UserBiz;

@Controller
@RequestMapping("/userDatagridColumn")

public class UserDatagridColumnAction extends BaseAction {
    @Resource
    private UserBiz userBiz;

    @RequestMapping("/save.action")
    @ResponseBody
    public ResultResp getSave(HttpSession session, UserDatagridColumn userDatagridColumn) {
        UserBean userBean = getUserInfo(session);
        userDatagridColumn.setUsername(userBean.getLoginname());
        userDatagridColumn.setWhCode(getBindWhCode(session));
        return userBiz.save(userDatagridColumn);
    }

    @RequestMapping("/getUserDatagridColumn.action")
    @ResponseBody
    public ResultRespT<UserDatagridColumn> getUserDatagridColumn(HttpSession session, String key) {
        UserBean userBean = getUserInfo(session);
        return userBiz.getUserDatagridColumn(userBean.getLoginname(), getBindWhCode(session), key);
    }

}