package wms.business.biz;

import java.io.Serializable;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsAdj;

public interface WmsAdjBiz {

	/**
	 * 删除库存调整申请单
	 */
	ResultResp delete(String id);

	WmsAdj getEntity(Serializable id);

	PageData<WmsAdj> getPageData(int page, int rows, WmsAdj wmsAdj);

	ResultResp save(WmsAdj wmsAdj);

	public ResultResp updateStock(WmsAdj wmsAdjs);

}
