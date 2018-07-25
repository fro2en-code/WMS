package wms.userRelation.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.wms.userRelation.UserSessionID;

import its.base.service.BaseServiceImpl;
import wms.userRelation.service.UserSessionIDService;

@Service("userSessionIDService")
public class UserSessionIDServiceImpl extends BaseServiceImpl<UserSessionID> implements UserSessionIDService {
    /**
     * 根据用户登录名获取用户信息
     */
    @Override
    public UserSessionID getUserInfo(String loginName) {
        return (UserSessionID) uniqueResult("from UserSessionID where userLoginname = ?", loginName);
    }

    @Override
    public void save(UserSessionID userSessionID) {
        saveEntity(userSessionID);
    }

    @Override
    public void update(UserSessionID userSessionID) {
        updateEntity(userSessionID);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getOnline(Collection<String> user) {
        StringBuilder hql = new StringBuilder("select userLoginname from UserSessionID where userLoginname in (");
        List<Serializable> params = new ArrayList<>();
        for (String string : user) {
            hql.append("?,");
            params.add(string);
        }
        hql.deleteCharAt(hql.length() - 1);
        hql.append(")");
        Query query = getSession().createQuery(hql.toString());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        return new HashSet<>(query.list());
    }

}
