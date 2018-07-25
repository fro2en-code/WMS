package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.business.WmsTaskLog;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsTaskLogService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsTaskLogService")
public class WmsTaskLogServiceImpl extends BaseServiceImpl<WmsTaskLog> implements WmsTaskLogService {

    /**
     * 分页
     */
    @Override
    public PageData<WmsTaskLog> getPageData(int page, int rows, WmsTaskLog wmsTaskLog) {
        return getPageDataByBaseHql("From WmsTaskLog where 1=1 ", null, page, rows, new ArrayList<Serializable>());
    }

    @Override
    public WmsTaskLog selectLogByBillId(String billId) {
        return (WmsTaskLog) uniqueResult("from WmsTaskLog where billid=?", billId);
    }
}
