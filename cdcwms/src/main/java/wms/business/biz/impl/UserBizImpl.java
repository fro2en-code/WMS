package wms.business.biz.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.plat.common.beans.Attr;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.userRelation.UserCompany;
import com.wms.userRelation.UserDatagridColumn;
import com.wms.userRelation.UserLabel;
import com.wms.userRelation.UserSessionID;
import com.wms.userRelation.UserWarehouse;

import cn.rtzltech.user.api.RoleApi;
import cn.rtzltech.user.model.Plat_Role;
import wms.business.biz.UserBiz;
import wms.userRelation.service.UserCompanyService;
import wms.userRelation.service.UserDatagridColumnService;
import wms.userRelation.service.UserLabelService;
import wms.userRelation.service.UserSessionIDService;
import wms.userRelation.service.UserWarehouseService;

@Service("userBiz")
public class UserBizImpl implements UserBiz {
    @Resource
    private Attr attr;

    @Resource
    private RoleApi roleApi;

    @Resource
    private UserCompanyService userCompanyService;

    @Resource
    private UserDatagridColumnService userDatagridColumnService;

    @Resource
    private UserLabelService userLabelService;

    @Resource
    private UserSessionIDService userSessionIDService;

    @Resource
    private UserWarehouseService userWarehouseService;

    /**
     * 清空指定用户缓存信息
     */
    @CacheEvict(value = "user.getRolesByUser", key = "#loginName")
    @Override
    public void clearCache(String loginName) {
    }

    @Override
    public void deleteEntity(UserCompany bean) {
        userCompanyService.deleteEntity(bean);
    }

    @Override
    public ResultResp delUserLabel(String id) {
        return userLabelService.del(id);
    }

    @Override
    public ResultResp delUserWarehouse(String id) {
        return userWarehouseService.del(id);
    }

    @Override
    public List<UserCompany> getCompanyByName(String UserName, String whCode) {
        return userCompanyService.getCompanyByName(UserName, whCode);
    }

    @Override
    public String getLoginNameByTid(String tid) {
        return userLabelService.getLoginNameByTid(tid);
    }

    @Override
    public PageData<UserCompany> getPageData(int page, int rows, UserCompany userCompany) {
        return userCompanyService.getPageData(page, rows, userCompany);
    }

    @Cacheable(value = "user.getRolesByUser", key = "#loginName")
    @Override
    public List<String> getRolesByUser(String loginName) {
        List<String> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("platKey", attr.getWmsMark());
        params.put("loginname", loginName);
        cn.rtzltech.user.result.ResultRespT<List<Plat_Role>> result = roleApi.getUserRoleList(params);
        if (result.isSuc()) {
            List<Plat_Role> roles = result.getT();
            for (Plat_Role role : roles) {
                list.add(role.getName());
            }
        }
        return list;
    }

    @Override
    public List<UserCompany> getUserCompany(UserCompany userCompany) {
        return userCompanyService.getUserCompany(userCompany);
    }

    @Cacheable(value = "user.getUserDatagridColumn", key = "#userName+#whCode+#key")
    @Override
    public ResultRespT<UserDatagridColumn> getUserDatagridColumn(String userName, String whCode, String key) {
        return userDatagridColumnService.getUserDatagridColumn(userName, whCode, key);
    }

    @Override
    public UserSessionID getUserInfo(String loginName) {
        return userSessionIDService.getUserInfo(loginName);
    }

    @Override
    public PageData<UserLabel> getUserLabelPageData(int page, int rows, String userLoginname) {
        return userLabelService.getPageData(page, rows, userLoginname);
    }

    @Override
    public PageData<UserWarehouse> getUserPageData(int page, int rows, UserWarehouse userWarehouse) {
        return userWarehouseService.getUserPageData(page, rows, userWarehouse);
    }

    @Override
    public UserWarehouse getUserWarehouseEntity(Serializable id) {
        return userWarehouseService.getEntity(id);
    }

    @Override
    public PageData<UserWarehouse> getUserWarehousePageData(int page, int rows, String loginname) {
        return userWarehouseService.getPageData(page, rows, loginname);
    }

    @Override
    public List<UserWarehouse> getWarehouseMap(String loginname) {
        return userWarehouseService.getWarehouseMap(loginname);
    }

    @Override
    public ResultResp save(UserCompany bean) {
        return userCompanyService.save(bean);
    }

    @CacheEvict(value = "user.getUserDatagridColumn", key = "#userDatagridColumn.username+#userDatagridColumn.whCode+#userDatagridColumn.key")
    @Override
    public ResultResp save(UserDatagridColumn userDatagridColumn) {
        return userDatagridColumnService.save(userDatagridColumn);
    }

    @Override
    public ResultResp save(UserWarehouse userWarehouse) {
        return userWarehouseService.save(userWarehouse);
    }

    @Override
    public void saveOrUpdateEntity(UserSessionID bean) {
        userSessionIDService.saveOrUpdateEntity(bean);
    }

    @Override
    public ResultResp saveUserLabel(UserLabel la) {
        return userLabelService.save(la);
    }

    @Override
    public void update(UserSessionID bean) {
        userSessionIDService.update(bean);
    }

}
