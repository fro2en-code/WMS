package wms.userRelation.service;

import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.userRelation.UserDatagridColumn;

import its.base.service.BaseService;

public interface UserDatagridColumnService extends BaseService<UserDatagridColumn> {
    ResultRespT<UserDatagridColumn> getUserDatagridColumn(String userName, String whCode, String key);

    ResultResp save(UserDatagridColumn userDatagridColumn);

}
