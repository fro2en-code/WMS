package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsWaybill;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsWaybillService;

@Service("wmsWaybillService")
public class WmsWaybillServiceImpl extends BaseServiceImpl<WmsWaybill> implements WmsWaybillService {

    @Override
    public PageData<WmsWaybill> getPageData(int page, int rows, WmsWaybill wmsWaybill) {
        StringBuilder base_hql = new StringBuilder();
        List<Serializable> params = new ArrayList<>();
        base_hql.append("from WmsWaybill where whCode=? ");
        params.add(wmsWaybill.getWhCode());
        if (!StringUtil.isEmpty(wmsWaybill.getNumber())) {
            base_hql.append("and number like ?");
            params.add("%" + wmsWaybill.getNumber() + "%");
        }
        return getPageDataByBaseHql(base_hql.toString(), " Order By number desc", page, rows, params);
    }

}
