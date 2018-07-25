package wms.web.userRelation.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.userRelation.UserLabel;

import wms.business.biz.UserBiz;

@Controller
@RequestMapping("/userLabel")
public class UserLabelAction extends BaseAction {

    @Resource
    private UserBiz userBiz;

    /**
     * 保存和修改数据
     * 
     * @return
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public ResultResp handleData(HttpServletResponse resp, HttpSession session, HttpServletRequest req,
            String userLoginname, String userId, String tid) {
        UserBean user = getUserInfo(session);
        UserLabel userLabel = new UserLabel();
        userLabel.setUserId(userId);
        userLabel.setTid(tid);
        userLabel.setUserLoginname(userLoginname);
        userLabel.setInsertUser(user.getLoginname());
        userLabel.setInsertTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        return userBiz.saveUserLabel(userLabel);
    }

    @RequestMapping("/getPage.action")
    @ResponseBody
    public PageData<UserLabel> getPage(HttpServletResponse resp, HttpSession session, int page, int rows,
            String userLoginname) {
        return userBiz.getUserLabelPageData(page, rows, userLoginname);
    }

    @RequestMapping("/del.action")
    @ResponseBody
    public ResultResp delData(HttpServletResponse resp, String id) {
        return userBiz.delUserLabel(id);
    }
}
