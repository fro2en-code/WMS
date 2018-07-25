package wms.business.biz;

import java.io.Serializable;
import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.userRelation.UserCompany;
import com.wms.userRelation.UserDatagridColumn;
import com.wms.userRelation.UserLabel;
import com.wms.userRelation.UserSessionID;
import com.wms.userRelation.UserWarehouse;

public interface UserBiz {

    void clearCache(String loginName);

    void deleteEntity(UserCompany bean);

    ResultResp delUserLabel(String id);

    /**
     * 删除仓库绑定
     */
    ResultResp delUserWarehouse(String id);

    /**
     * 根据用户名查询绑定的公司
     */
    List<UserCompany> getCompanyByName(String UserName, String whCode);

    String getLoginNameByTid(String tid);

    PageData<UserCompany> getPageData(int page, int rows, UserCompany userCompany);

    List<String> getRolesByUser(String loginName);

    List<UserCompany> getUserCompany(UserCompany userCompany);

    ResultRespT<UserDatagridColumn> getUserDatagridColumn(String userName, String whCode, String key);

    /**
     * 根据用户登录名获取用户信息
     */
    UserSessionID getUserInfo(String loginName);

    PageData<UserLabel> getUserLabelPageData(int page, int rows, String userLoginname);

    /**
     * 获取用户信息
     */
    PageData<UserWarehouse> getUserPageData(int page, int rows, UserWarehouse userWarehouse);

    UserWarehouse getUserWarehouseEntity(Serializable id);

    /**
     * 查询分页数据
     */
    PageData<UserWarehouse> getUserWarehousePageData(int page, int rows, String loginname);

    List<UserWarehouse> getWarehouseMap(String loginname);

    ResultResp save(UserCompany bean);

    ResultResp save(UserDatagridColumn userDatagridColumn);

    /**
     * 保存仓库绑定
     */
    ResultResp save(UserWarehouse userWarehouse);

    void saveOrUpdateEntity(UserSessionID bean);

    /**
     * 保存、修改功能类型
     */
    ResultResp saveUserLabel(UserLabel la);

    void update(UserSessionID bean);
}
