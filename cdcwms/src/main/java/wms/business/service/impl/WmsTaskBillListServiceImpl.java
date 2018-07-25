package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.business.WmsTaskBillList;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsTaskBillListService;

/**
 *
 * @author zhouxianglh@gmail.comb
 *
 * @since 2017.03.17
 */
@Service("wmsTaskBillListService")
public class WmsTaskBillListServiceImpl extends BaseServiceImpl<WmsTaskBillList> implements WmsTaskBillListService {

    /**
     * 分页
     */
    @Override
    public PageData<WmsTaskBillList> getPageData(int page, int rows, WmsTaskBillList wmsTaskBillList) {
        List<Serializable> params = new ArrayList<Serializable>();
        params.add(wmsTaskBillList.getParentid());
        params.add(wmsTaskBillList.getBillid());
        return getPageDataByBaseHql("From WmsTaskBillList where parentid=? and billid=?", null, page, rows, params);
    }

    @Override
    public List<WmsTaskBillList> getBillListByParentId(String parentId) {
        return findEntityByHQL("From WmsTaskBillList where parentid=? order by gcode", parentId);
    }

    @Override
    public List<WmsTaskBillList> getBillListByBillId(String billId, String whCode) {
        return findEntityByHQL(
                "select wmsTaskBillList From WmsTaskBillList as wmsTaskBillList,WmsTaskBill as wmsTaskBill where wmsTaskBill.id=wmsTaskBillList.parentid and wmsTaskBill.status='1' and wmsTaskBill.whCode=? and wmsTaskBillList.billid=?",
                whCode, billId);
    }

    @Override
    public List<WmsTaskBillList> getBillList(WmsTaskBillList wmsTaskBillList) {
        return findEntityByHQL("From WmsTaskBillList where parentid=? and billid=?", wmsTaskBillList.getParentid(),
                wmsTaskBillList.getBillid());
    }

}
