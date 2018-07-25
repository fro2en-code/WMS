package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.warehouse.WmsObject;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsObjectService;

@Service("wmsObjectService")
public class WmsObjectServiceImpl extends BaseServiceImpl<WmsObject> implements WmsObjectService {

    /**
     * 查询分页
     */
    @Override
    public PageData<WmsObject> getPageData(int page, int rows, WmsObject wmsObject) {
        StringBuilder baseSQL = new StringBuilder("from WmsObject where 1=1 ");
        List<Serializable> params = new ArrayList<>();
        if (StringUtils.isNotEmpty(wmsObject.getWhCode())) {
            baseSQL.append(" and whCode = ? ");
            params.add(wmsObject.getWhCode());
        }
        if (StringUtils.isNotEmpty(wmsObject.getGcode())) {
            baseSQL.append(" and gcode like ? ");
            params.add("%" + wmsObject.getGcode() + "%");
        }
        if (StringUtils.isNotEmpty(wmsObject.getSupCode())) {
            baseSQL.append(" and supCode like ? ");
            params.add("%" + wmsObject.getSupCode() + "%");
        }
        return getPageDataByBaseHql(baseSQL.toString(), " Order By updateTime desc", page, rows, params);
    }

    @Override
    public WmsObject getWmsObject(WmsObject wmsObject) {
        return (WmsObject) uniqueResult(
                "From WmsObject Where whCode=? And supCode = ? And gcode=? and gtype=? and batchCode=?",
                wmsObject.getWhCode(), wmsObject.getSupCode(), wmsObject.getGcode(), wmsObject.getGtype(),
                wmsObject.getBatchCode());
    }

    @Override
    public List<WmsObject> getWmsObjectList(WmsObject wmsObject) {
        return findEntityByHQL(
                "From WmsObject Where whCode=? And supCode = ? And gcode=? and gtype=? order by  batchCode",
                wmsObject.getWhCode(), wmsObject.getSupCode(), wmsObject.getGcode(), wmsObject.getGtype());
    }

    @Override
    public void updateWmsObject(WmsObject wmsObject) {
        WmsObject oldWmsObject = getWmsObject(wmsObject);
        if (null == oldWmsObject) {// 对象不存在则新增
            saveEntity(wmsObject);
        } else {// 对象存在则修改
            oldWmsObject.setQuantity(oldWmsObject.getQuantity() + wmsObject.getQuantity());
            updateEntity(oldWmsObject);
        }
    }

}
