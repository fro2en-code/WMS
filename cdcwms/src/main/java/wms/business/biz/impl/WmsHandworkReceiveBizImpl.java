package wms.business.biz.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsHandworkReceive;
import com.wms.business.WmsHandworkReceiveList;

import wms.business.biz.WmsHandworkReceiveBiz;
import wms.business.service.WmsHandworkReceiveListService;
import wms.business.service.WmsHandworkReceiveService;

@Service("wmsHandworkReceiveBiz")
public class WmsHandworkReceiveBizImpl implements WmsHandworkReceiveBiz {
	@Resource
	private WmsHandworkReceiveService wmsHandworkReceiveService;
	@Resource
	private WmsHandworkReceiveListService wmsHandworkReceiveListService;

	@Override
	public WmsHandworkReceive getWmsHandworkReceiveEntity(Serializable id) {
		return wmsHandworkReceiveService.getEntity(id);
	}

	@Override
	public PageData<WmsHandworkReceiveList> getPageHandworkReceiveList(int page, int rows,
			WmsHandworkReceiveList wmsHandworkReceiveList) {
		return wmsHandworkReceiveListService.getPageDataList(page, rows, wmsHandworkReceiveList);
	}

	@Override
	public List<WmsHandworkReceiveList> getWmsHandworkReceiveList(String mapSheetNo, String whCode) {
		return wmsHandworkReceiveListService.getWmsHandworkReceiveList(mapSheetNo, whCode);
	}

	@Override
	public void deleteHandworkReceive(WmsHandworkReceive wmsHandworkReceive) {
		wmsHandworkReceive = wmsHandworkReceiveService.getEntity(wmsHandworkReceive.getId());
		wmsHandworkReceiveService.deleteEntity(wmsHandworkReceive);
		wmsHandworkReceiveListService.delByHandworkReceiveList(wmsHandworkReceive.getMapSheetNo(),
				wmsHandworkReceive.getWhCode());
	}

	@Override
	public ResultResp deleteHandworkReceiveList(WmsHandworkReceiveList wmsHandworkReceiveList) {
		return wmsHandworkReceiveListService.del(wmsHandworkReceiveList);
	}

	@Override
	public PageData<WmsHandworkReceive> getPageDataHandworkReceive(int page, int rows,
			WmsHandworkReceive wmsHandworkReceive) {
		return wmsHandworkReceiveService.getPageDataList(page, rows, wmsHandworkReceive);
	}

	@Override
	public ResultResp saveHandworkReceive(WmsHandworkReceive wmsHandworkReceive) {
		return wmsHandworkReceiveService.save(wmsHandworkReceive);
	}

	@Override
	public ResultResp saveHandworkReceiveList(WmsHandworkReceiveList wmsHandworkReceiveList) {
		return wmsHandworkReceiveListService.save(wmsHandworkReceiveList);
	}

	@Override
	public void saveEntity(WmsHandworkReceiveList wmsHandworkReceiveList) {
		wmsHandworkReceiveListService.saveEntity(wmsHandworkReceiveList);
	}

	@Override
	public void saveEntity(WmsHandworkReceive wmsHandworkReceive) {
		wmsHandworkReceiveService.saveEntity(wmsHandworkReceive);
	}

	@Override
	public WmsHandworkReceive getReceiveByMapSheetNo(String mapSheetNo, String whCode) {
		return wmsHandworkReceiveService.getReceiveByMapSheetNo(mapSheetNo, whCode);
	}

}
