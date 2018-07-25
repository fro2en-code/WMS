package wms.business.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsMove;

import wms.business.biz.MoveBiz;
import wms.business.service.WmsMoveService;

@Service("moveBiz")
public class MoveBizImpl implements MoveBiz {
	@Resource
	private WmsMoveService wmsMoveService;

	public PageData<WmsMove> getPageDataMove(int page, int rows, WmsMove wmsMove) {
		return wmsMoveService.getPageData(page, rows, wmsMove);
	}

	public ResultResp saveMove(WmsMove wmsMove) {
		return wmsMoveService.saveMove(wmsMove);
	}

	public ResultResp deleteMove(String id) {
		return wmsMoveService.deleteMove(id);
	}

	public WmsMove getEntity(String id) {
		return wmsMoveService.getEntity(id);
	}
}
