package wms.userRelation.service;

import java.util.Collection;
import java.util.Set;

import com.wms.userRelation.UserSessionID;

import its.base.service.BaseService;

/**
 * 用户session管理接口
 */
public interface UserSessionIDService extends BaseService<UserSessionID> {
    /**
     * 根据用户登录名获取用户信息
     */
    UserSessionID getUserInfo(String loginName);

    /**
     * 获取当前在线用户
     */
    Set<String> getOnline(Collection<String> user);

    /**
     * 保存数据
     */
    void save(UserSessionID userSessionID);

    /**
     * 修改数据
     */
    void update(UserSessionID userSessionID);
}
