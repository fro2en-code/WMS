package wms.business.biz;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsMove;

public interface MoveBiz {
	/**
	 * 移库申请单分页
	 */
	public PageData<WmsMove> getPageDataMove(int page, int rows, WmsMove wmsMove);

	/**
	 * 新增,修改移库申请单
	 */
	public ResultResp saveMove(WmsMove wmsMove);

	/**
	 * 删除盘库申请单
	 */
	public ResultResp deleteMove(String id);
	
	public WmsMove getEntity(String id);
}
