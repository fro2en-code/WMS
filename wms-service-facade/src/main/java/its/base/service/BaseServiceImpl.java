package its.base.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.utils.Function;
import com.plat.common.utils.StringUtil;

public abstract class BaseServiceImpl<T extends BaseModel> implements BaseService<T> {

    @SuppressWarnings("rawtypes")
    protected Class clazz;

    @Value("${minSearchLength}")
    protected int minSearchLength;
    @Value("${searchRows}")
    protected int searchRows;

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @SuppressWarnings("rawtypes")
    public BaseServiceImpl() {
        // 这一段代码会导致JDK8 启动失败,不知道怎么改
        // 真TMD蛋疼的代码,这么写不能继承,为了个小小功能,搞成为个样子
        Class classRun = this.getClass();
        while (!(classRun.getGenericSuperclass() instanceof ParameterizedType)) {
            classRun = classRun.getSuperclass();
        }
        ParameterizedType pt = (ParameterizedType) classRun.getGenericSuperclass();
        clazz = (Class) pt.getActualTypeArguments()[0];
    }

    /**
     * 对新增时间,最后修改人,最后修改时间补全
     */
    private void autoComplete(T t) {
        if (StringUtils.isEmpty(t.getInsertTime())) {
            t.setInsertTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        }
        if (StringUtils.isEmpty(t.getUpdateTime())) {
            t.setUpdateTime(t.getInsertTime());
        }
        if (StringUtils.isEmpty(t.getUpdateUser())) {
            t.setUpdateUser(t.getInsertUser());
        }
    }

    @Override
    public int batchHandle(String hql, Serializable... serializables) {
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < serializables.length; i++) {
            query.setParameter(i, serializables[i]);
        }
        return query.executeUpdate();
    }

    @Override
    public int count(String hql, Serializable... serializables) {
        Long count = (Long) this.uniqueResult(hql, serializables);
        return count.intValue();
    }

    @Override
    public void deleteEntity(T t) {
        getSession().delete(t);
    }

    @Override
    public void evict(T t) {
        getSession().evict(t);
    }

    public int execute(String sql, Serializable... serializables) {
        Query query = getSession().createSQLQuery(sql);
        for (int i = 0; i < serializables.length; i++) {
            query.setParameter(i, serializables[i]);
        }
        return query.executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByHQL(String hql, Function<Query> function, Serializable... serializables) {
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < serializables.length; i++) {
            query.setParameter(i, serializables[i]);
        }
        if (null != function) {
            function.run(query);
        }
        return query.list();
    }

    @Override
    public List<T> findEntityByHQL(String hql, Serializable... serializables) {
        return findEntityByHQL(hql, null, serializables);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityBySQL(String sql, Function<SQLQuery> function, Serializable... serializables) {
        Query query = getSession().createSQLQuery(sql);
        for (int i = 0; i < serializables.length; i++) {
            query.setParameter(i, serializables[i]);
        }
        if (null != function) {
            function.run((SQLQuery) query);
        }
        return query.list();
    }

    @Override
    public List<T> findEntityBySQL(String sql, Serializable... serializables) {
        return findEntityBySQL(sql, null, serializables);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntitySplitPage(String hql, Function<Query> functon, int startIndex, int endIndex,
            Serializable... serializables) {
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < serializables.length; i++) {
            query.setParameter(i, serializables[i]);
        }
        query.setFirstResult(startIndex);
        query.setMaxResults(endIndex);
        if (null != functon) {
            functon.run(query);
        }
        return query.list();
    }

    @Override
    public List<T> findEntitySplitPage(String hql, int startIndex, int endIndex, Serializable... serializables) {
        return findEntitySplitPage(hql, null, startIndex, endIndex, serializables);
    }

    /**
     * @seqName 列名
     */
    public String getCode(String seqName) {
        final String sql = "select nextval('" + seqName + "')";
        Query query = getSession().createSQLQuery(sql);
        return query.uniqueResult().toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getEntity(Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    @Override
    public PageData<T> getPageDataByBaseHql(String baseHql, String orderByHQL, Function<Query> functon, int page,
            int rows, List<Serializable> list) {
        Serializable[] params = list.toArray(new Serializable[list.size()]);
        return getPageDataByBaseHql(baseHql, orderByHQL, null, page, rows, params);
    }

    @Override
    public PageData<T> getPageDataByBaseHql(String baseHql, String orderByHQL, Function<Query> functon, int page,
            int rows, Serializable... serializables) {
        String count_hql = StringUtils.join("select count(*) ", baseHql);
        String query_hql = null;
        if (!StringUtils.isEmpty(orderByHQL)) {
            query_hql = StringUtils.join(baseHql, orderByHQL);
        } else {
            query_hql = baseHql;
        }
        return getPageDataByHql(query_hql.toString(), count_hql.toString(), functon, page, rows, serializables);
    }

    @Override
    public PageData<T> getPageDataByBaseHql(String baseHql, String orderByHQL, int page, int rows,
            List<Serializable> list) {
        return getPageDataByBaseHql(baseHql, orderByHQL, null, page, rows, list);
    }

    @Override
    public PageData<T> getPageDataByBaseHql(String baseHql, String orderByHQL, int page, int rows,
            Serializable... serializables) {
        return getPageDataByBaseHql(baseHql, orderByHQL, null, page, rows, serializables);
    }

    @Override
    public PageData<T> getPageDataByHql(String listHql, String countHql, Function<Query> functon, int page, int rows,
            Serializable... serializables) {
        int total = count(countHql, serializables);
        List<T> list = findEntitySplitPage(listHql, functon, (page - 1) * rows, rows, serializables);
        return new PageData<>(page, rows, total, list);
    }

    @Override
    public PageData<T> getPageDataByHql(String listHql, String countHql, int page, int rows,
            Serializable... serializables) {
        return getPageDataByHql(listHql, countHql, null, page, rows, serializables);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageData<T> getPageDataBySql(String listSql, String countSql, Function<SQLQuery> functon, int page, int rows,
            Serializable... serializables) {
        return (PageData<T>) getPageListBySql(listSql, countSql, functon, page, rows, serializables);
    }

    @Override
    public PageData<?> getPageListBySql(String listSql, String countSql, Function<SQLQuery> functon, int page, int rows,
            Serializable... serializables) {
        Query query = getSession().createSQLQuery(listSql);
        for (int i = 0; i < serializables.length; i++) {
            query.setParameter(i, serializables[i]);
        }
        query.setFirstResult((page - 1) * rows);
        query.setMaxResults(rows);
        if (null != functon) {
            functon.run((SQLQuery) query);
        }
        List<?> list = query.list();
        int total = 0;
        Object obj = uniqueSqlResult(countSql, serializables);
        if (obj != null) {
            total = ((Number) obj).intValue();
        }
        return new PageData<>(page, rows, total, list);
    }

    @Override
    public PageData<T> getPageDataBySql(String listSql, String countSql, int page, int rows,
            Serializable... serializables) {
        return getPageDataBySql(listSql, countSql, null, page, rows, serializables);
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T loadEntity(Serializable id) {
        return (T) getSession().load(clazz, id);
    }

    protected Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public Serializable saveEntity(T t) {
        autoComplete(t);
        return getSession().save(t);
    }

    @Override
    public void saveOrUpdateEntity(T t) {
        autoComplete(t);
        getSession().saveOrUpdate(t);
    }

    @Override
    public Object uniqueResult(String hql, Serializable... serializables) {
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < serializables.length; i++) {
            query.setParameter(i, serializables[i]);
        }
        return query.uniqueResult();
    }

    @Override
    public Object uniqueSqlResult(String sql, Serializable... serializables) {
        Query query = getSession().createSQLQuery(sql);
        for (int i = 0; i < serializables.length; i++) {
            query.setParameter(i, serializables[i]);
        }
        return query.uniqueResult();
    }

    @Override
    public void updateEntity(T t) {
        autoComplete(t);
        getSession().merge(t);
    }
}
