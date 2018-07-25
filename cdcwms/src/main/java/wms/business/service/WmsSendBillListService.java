package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WmsSendBillList;

import its.base.service.BaseService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsSendBillListService extends BaseService<WmsSendBillList> {

    /**
     * 分页查询
     */
    PageData<WmsSendBillList> getPageDataList(int page, int rows, WmsSendBillList wmsSendBillList);
}
