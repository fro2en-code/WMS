package its.base.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.plat.common.page.PageData;
import com.plat.common.utils.Function;

public interface BaseService<T> {

    /**
     * 批量写操作
     *
     * @param hql
     * @param serializables
     */
    public int batchHandle(String hql, Serializable... serializables);

    public int count(String hql, Serializable... serializables);

    public void deleteEntity(T t);

    public void evict(T t);

    /**
     * 批量读操作
     *
     * @param hql
     * @param serializables
     * @return
     */

    public List<T> findEntityByHQL(String hql, Function<Query> functon, Serializable... serializables);

    /**
     * 批量读操作
     *
     * @param hql
     * @param serializables
     * @return
     */

    public List<T> findEntityByHQL(String hql, Serializable... serializables);

    List<T> findEntityBySQL(String sql, Function<SQLQuery> functon, Serializable... serializables);

    /**
     * 批量读操作
     *
     * @param sql
     * @param serializables
     * @return
     */
    List<T> findEntityBySQL(String sql, Serializable... serializables);

    public List<T> findEntitySplitPage(String hql, Function<Query> functon, int startIndex, int endIndex,
            Serializable... serializables);

    /**
     * 分页读操作
     *
     * @param hql
     * @param serializables
     * @return
     */
    public List<T> findEntitySplitPage(String hql, int startIndex, int endIndex, Serializable... serializables);

    /**
     * 单体读操作
     *
     * @param id
     * @return
     */
    public T getEntity(Serializable id);

    PageData<T> getPageDataByBaseHql(String baseHql, String orderByHQL, Function<Query> functon, int page, int rows,
            List<Serializable> list);

    PageData<T> getPageDataByBaseHql(String baseHql, String orderByHQL, Function<Query> functon, int page, int rows,
            Serializable... serializables);

    /**
     * 通用hql分页
     *
     * @param baseHql
     *            基础HQL
     * @param orderByHQL
     *            排序HQL
     * @param page
     * @param rows
     * @param list
     * @return
     */
    PageData<T> getPageDataByBaseHql(String baseHql, String orderByHQL, int page, int rows, List<Serializable> list);

    /**
     * 通用hql分页
     *
     * @param baseHql
     *            基础HQL
     * @param orderByHQL
     *            排序HQL
     * @param page
     * @param rows
     * @param serializables
     * @return
     */
    PageData<T> getPageDataByBaseHql(String baseHql, String orderByHQL, int page, int rows,
            Serializable... serializables);

    public PageData<T> getPageDataByHql(String listHql, String countHql, Function<Query> functon, int page, int rows,
            Serializable... serializables);

    /**
     * 通用hql分页
     *
     * @param listHql
     * @param countHql
     * @param page
     * @param rows
     * @param serializables
     * @return
     */
    public PageData<T> getPageDataByHql(String listHql, String countHql, int page, int rows,
            Serializable... serializables);

    public PageData<T> getPageDataBySql(String listSql, String countSql, Function<SQLQuery> functon, int page, int rows,
            Serializable... serializables);

    public PageData<?> getPageListBySql(String listSql, String countSql, Function<SQLQuery> functon, int page, int rows,
            Serializable... serializables);

    /**
     * 通用sql分页
     *
     * @param listSql
     * @param countSql
     * @param page
     * @param rows
     * @param serializables
     * @return
     */
    public PageData<T> getPageDataBySql(String listSql, String countSql, int page, int rows,
            Serializable... serializables);

    public T loadEntity(Serializable id);

    /**
     * 单体写操作
     *
     * @param t
     */
    public Serializable saveEntity(T t);

    public void saveOrUpdateEntity(T t);

    /**
     * 查询结果有且仅有一条记录。
     *
     * @param hql
     * @param serializables
     * @return
     */
    public Object uniqueResult(String hql, Serializable... serializables);

    /**
     * 查询结果有且仅有一条记录。一般是统计。
     *
     * @param sql
     * @param serializables
     * @return
     */
    public Object uniqueSqlResult(String sql, Serializable... serializables);

    public void updateEntity(T t);

}
