package wms.business.service;

import java.util.List;

import com.wms.business.WmsSendBill;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsSendBillService extends BaseService<WmsSendBill> {

    /**
     * 分页查询
     */
    List<WmsSendBill> getDataList(WmsSendBill wmsSendBill);
}
