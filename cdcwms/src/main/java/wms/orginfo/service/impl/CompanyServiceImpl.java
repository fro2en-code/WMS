package wms.orginfo.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.Function;
import com.plat.common.utils.StringUtil;
import com.wms.orginfo.OrgConpany;

import its.base.service.BaseServiceImpl;
import wms.orginfo.service.CompanyService;

/**
 * 公司管理实现
 *
 */
@Service("companyService")
public class CompanyServiceImpl extends BaseServiceImpl<OrgConpany> implements CompanyService {

    /**
     * 查询分页
     */
    @Override
    public PageData<OrgConpany> getPageData(int page, int rows, OrgConpany conpany) {
        StringBuffer base_hql = new StringBuffer();
        List<Serializable> params = new ArrayList<>();
        base_hql.append("from OrgConpany where 1=1 ");
        if (!StringUtil.isEmpty(conpany.getCompanyid())) {
            base_hql.append(" And companyid like ?");
            params.add("%" + conpany.getCompanyid() + "%");
        }
        if (!StringUtil.isEmpty(conpany.getName())) {
            base_hql.append(" And name like ?");
            params.add("%" + conpany.getName() + "%");
        }
        if (!StringUtil.isEmpty(conpany.getWhCode())) {
            base_hql.append(" And whCode = ?");
            params.add(conpany.getWhCode());
        }
        return getPageDataByBaseHql(base_hql.toString(), " Order By companyid desc", page, rows, params);
    }

    /**
     * 获取全部公司数据
     */
    @Override
    public List<OrgConpany> getAllConpany() {
        return findEntityByHQL("From OrgConpany");
    }

    /**
     * 根据公司代码获取公司数据
     */
    @Override
    public OrgConpany getConpanyByConCode(String companyid) {
        List<OrgConpany> conpanies = findEntityByHQL("From OrgConpany Where companyid = ?", companyid);
        if (conpanies.isEmpty()) {
            return null;
        } else {
            return conpanies.get(0);
        }
    }

    /**
     * 保存、修改公司
     */
    @Override
    public ResultResp save(OrgConpany conpany) {
        ResultResp resp = new ResultResp();
        conpany.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
        String id = conpany.getId();
        String check = "select count(*) From OrgConpany Where companyid= ? and whCode = ?";
        List<Object> params = new ArrayList<>();
        params.add(conpany.getCompanyid());
        params.add(conpany.getWhCode());
        if (StringUtil.isEmpty(id)) {// 新增
            if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
                resp.setRetcode("-1");
                resp.setRetmsg("公司编号" + conpany.getCompanyid() + "已经存在！");
            } else {
                saveEntity(conpany);
                resp.setRetcode("0");
                resp.setRetmsg("新增公司成功！");
            }
        } else {// 更新
            check += " and id <> ?";
            params.add(conpany.getId());
            if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
                resp.setRetcode("-1");
                resp.setRetmsg("公司编号" + conpany.getCompanyid() + "已经存在！");
            } else {
                updateEntity(conpany);
                resp.setRetcode("0");
                resp.setRetmsg("修改公司成功！");
            }
        }
        return resp;
    }

    /**
     * 删除公司
     */
    @Override
    public ResultResp del(String id) {
        ResultResp resp = new ResultResp();
        OrgConpany conpany = getEntity(id);
        deleteEntity(conpany);
        resp.setRetcode("0");
        resp.setRetmsg("删除成功!");
        return resp;
    }

    /**
     * 根据公司类型查询下拉公司
     */
    @Override
    public List<Map<String, String>> getComboboxByConCode(String name, String whCode) {
        String type = "供应商";
        List<OrgConpany> conpanies = null;
        if (null != name) {
            conpanies = findEntityByHQL(
                    "From OrgConpany Where whCode = ? and type=? and (name like ? or companyid like ?)",
                    new Function<Query>() {
                        @Override
                        public void run(Query t) {
                            t.setMaxResults(searchRows);
                        }
                    }, whCode, type, "%" + name + "%", "%" + name + "%");
        } else {
            conpanies = findEntityByHQL("From OrgConpany Where whCode = ? and type=?", new Function<Query>() {
                @Override
                public void run(Query t) {
                    t.setMaxResults(searchRows);
                }
            }, whCode, type);
        }
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        for (OrgConpany orgConpany : conpanies) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", orgConpany.getId());
            map.put("companyid", orgConpany.getCompanyid());
            map.put("name", orgConpany.getName());
            map.put("conName", orgConpany.getName());
            map.put("type", orgConpany.getType());
            dataList.add(map);
        }
        return dataList;
    }

}