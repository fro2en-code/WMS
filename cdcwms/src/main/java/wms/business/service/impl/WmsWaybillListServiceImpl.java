package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.business.WmsWaybillList;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsWaybillListService;

@Service("wmsWaybillListService")
public class WmsWaybillListServiceImpl extends BaseServiceImpl<WmsWaybillList> implements WmsWaybillListService {

    @Override
    public PageData<WmsWaybillList> getPageData(int page, int rows, WmsWaybillList wmsWaybillList) {
        List<Serializable> params = new ArrayList<>();
        params.add(wmsWaybillList.getWhCode());
        params.add(wmsWaybillList.getNumber());
        return getPageDataByBaseHql("from WmsWaybillList where whCode=? and number = ?", " Order By number desc", page,
                rows, params);

    }

}
