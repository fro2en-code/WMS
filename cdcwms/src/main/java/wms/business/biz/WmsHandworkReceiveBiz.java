package wms.business.biz;

import java.io.Serializable;
import java.util.List;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkReceive;
import com.wms.business.WmsHandworkReceiveList;

public interface WmsHandworkReceiveBiz {
	/**
	 * 删除手工收货单据
	 */
	void deleteHandworkReceive(WmsHandworkReceive wmsHandworkReceive);

	/**
	 * 删除手工收货明细单
	 */
	ResultResp deleteHandworkReceiveList(WmsHandworkReceiveList wmsHandworkReceiveList);

	PageData<WmsHandworkReceive> getPageDataHandworkReceive(int page, int rows, WmsHandworkReceive wmsHandworkReceive);

	PageData<WmsHandworkReceiveList> getPageHandworkReceiveList(int page, int rows,
			WmsHandworkReceiveList wmsHandworkReceiveList);

	WmsHandworkReceive getWmsHandworkReceiveEntity(Serializable id);

	List<WmsHandworkReceiveList> getWmsHandworkReceiveList(String mapSheetNo, String whCode);

	public void saveEntity(WmsHandworkReceive wmsHandworkReceive);

	public void saveEntity(WmsHandworkReceiveList wmsHandworkReceiveList);

	/**
	 * 保存,修改手工收货单据
	 */
	ResultResp saveHandworkReceive(WmsHandworkReceive wmsHandworkReceive);

	/**
	 * 保存,修改手工收货明细单
	 */
	ResultResp saveHandworkReceiveList(WmsHandworkReceiveList wmsHandworkReceiveList);

	WmsHandworkReceive getReceiveByMapSheetNo(String mapSheetNo, String whCode);
}
