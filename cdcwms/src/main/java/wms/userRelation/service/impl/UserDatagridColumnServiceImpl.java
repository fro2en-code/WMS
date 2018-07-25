package wms.userRelation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.result.ResultResp;
import com.plat.common.result.ResultRespT;
import com.wms.userRelation.UserDatagridColumn;

import its.base.service.BaseServiceImpl;
import wms.userRelation.service.UserDatagridColumnService;

@Service("userDatagridColumnService")
public class UserDatagridColumnServiceImpl extends BaseServiceImpl<UserDatagridColumn>
        implements UserDatagridColumnService {

    /**
     * 获取数据
     */
    @Override
    public ResultRespT<UserDatagridColumn> getUserDatagridColumn(String userName, String whCode, String key) {
        List<UserDatagridColumn> result = findEntityByHQL(
                "from UserDatagridColumn where username=? and whCode=? and key=?", userName, whCode, key);
        UserDatagridColumn bean = null;
        if (null != result && result.size() > 0) {
            bean = result.get(0);
        }
        return new ResultRespT<>(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG, bean);
    }

    /**
     * 保存修改
     */
    @Override
    public ResultResp save(UserDatagridColumn userDatagridColumn) {
        List<UserDatagridColumn> result = findEntityByHQL(
                "from UserDatagridColumn where username=? and whCode=? and key=?", userDatagridColumn.getUsername(),
                userDatagridColumn.getWhCode(), userDatagridColumn.getKey());
        if (null != result && result.size() > 0) {
            UserDatagridColumn bean = result.get(0);
            bean.setColumn(userDatagridColumn.getColumn());
            updateEntity(userDatagridColumn);
        } else {
            saveEntity(userDatagridColumn);
        }
        return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG);
    }

}
