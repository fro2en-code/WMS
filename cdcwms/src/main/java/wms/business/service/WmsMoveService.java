package wms.business.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsMove;

import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WmsMoveService extends BaseService<WmsMove> {

	/**
	 * 分页查询
	 */
	PageData<WmsMove> getPageData(int page, int rows, WmsMove wmsMove);

	/**
	 * 新增,修改
	 */
	ResultResp saveMove(WmsMove wmsMove);

	/**
	 * 删除
	 */
	ResultResp deleteMove(String id);

	/**
	 * 根据移库编码获取信息
	 */
	WmsMove getMoveInfoByMoveCode(String moveCode);
}
