package wms.orginfo.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.Function;
import com.plat.common.utils.StringUtil;
import com.wms.orginfo.ExtOraTruck;

import its.base.service.BaseServiceImpl;
import wms.orginfo.service.TruckService;

/**
 * 车辆管理实现
 *
 */
@Service("truckService")
public class TruckServiceImpl extends BaseServiceImpl<ExtOraTruck> implements TruckService {

    /**
     * 删除车辆
     */
    @Override
    public ResultResp del(String id) {
        ResultResp resp = new ResultResp();
        ExtOraTruck et = getEntity(id);
        deleteEntity(et);
        resp.setRetcode("0");
        resp.setRetmsg("删除成功!");
        return resp;
    }

    /**
     * 获取全部车辆数据
     */
    @Override
    public List<ExtOraTruck> getAllExtOraTruck() {
        return null;
    }

    /**
     * 查询分页
     */
    @Override
    public PageData<ExtOraTruck> getPageData(int page, int rows, ExtOraTruck extOraTruck) {
        StringBuilder base_hql = new StringBuilder();
        List<Serializable> params = new ArrayList<>();
        base_hql.append("from ExtOraTruck where 1=1 ");
        if (!StringUtil.isEmpty(extOraTruck.getCompanyid())) {
            base_hql.append("and companyid like ?");
            params.add("%" + extOraTruck.getCompanyid() + "%");
        }
        if (!StringUtil.isEmpty(extOraTruck.getPlateInd())) {
            base_hql.append("and plateInd like ?");
            params.add("%" + extOraTruck.getPlateInd() + "%");
        }
        if (!StringUtil.isEmpty(extOraTruck.getIdentity())) {
            base_hql.append("and identity like ?");
            params.add("%" + extOraTruck.getIdentity() + "%");
        }
        if (!StringUtil.isEmpty(extOraTruck.getWhCode())) {
            base_hql.append("and whCode = ?");
            params.add(extOraTruck.getWhCode());
        }
        return getPageDataByBaseHql(base_hql.toString(), " Order By companyid desc", page, rows, params);
    }

    @Override
    public List<ExtOraTruck> getTruck(String key, String whCode) {
        if (null != key && key.length() >= 3) {
            return findEntityByHQL("from ExtOraTruck where whCode=? and plateInd like ?", new Function<Query>() {
                @Override
                public void run(Query t) {
                    t.setMaxResults(10);
                }
            }, whCode, "%" + key + "%");
        } else {
            return null;
        }
    }

    /**
     * 保存、修改车辆
     */
    @Override
    public ResultResp save(ExtOraTruck et) {
        ResultResp resp = new ResultResp();
        // 插入当前时间
        et.setUpdateTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
        String id = et.getId();
        String check = "select count(*) From ExtOraTruck Where plateInd= ? and whCode = ?";
        List<Object> params = new ArrayList<>();
        params.add(et.getPlateInd());
        params.add(et.getWhCode());
        if (StringUtil.isEmpty(id)) {// 新增
            if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
                resp.setRetcode("-1");
                resp.setRetmsg("车牌号" + et.getPlateInd() + "已经存在！");
            } else {
                saveEntity(et);
                resp.setRetcode("0");
                resp.setRetmsg("新增车辆成功！");
            }
        } else {// 更新
            check += " and id <> ?";
            params.add(et.getId());
            if (count(check, params.toArray(new Serializable[params.size()])) > 0) {
                resp.setRetcode("-1");
                resp.setRetmsg("车牌号" + et.getPlateInd() + "已经存在！");
            } else {
                updateEntity(et);
                resp.setRetcode("0");
                resp.setRetmsg("修改车辆成功！");
            }
        }
        return resp;
    }
}
