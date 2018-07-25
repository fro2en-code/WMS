package wms.business.biz.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.WmsLesSend;
import com.wms.business.WmsLesSendList;
import com.wms.business.WmsTask;

import wms.business.biz.WmsLesSendBiz;
import wms.business.service.WmsLesSendListService;
import wms.business.service.WmsLesSendService;
import wms.business.unit.ExecuteTaskUnit;

@Service("wmsLesSendBiz")
public class WmsLesSendBizImpl implements WmsLesSendBiz {
	@Resource(name = "lesSendExecuteTaskUnit")
	private ExecuteTaskUnit sendExcecut;

	@Resource
	private WmsLesSendService wmsLesSendService;

	@Resource
	private WmsLesSendListService wmsLesSendListService;

	@Override
	public WmsLesSend getWmsLesSendEntity(Serializable id) {
		return wmsLesSendService.getEntity(id);
	}

	@Override
	public void updateWmsLesSendEntity(WmsLesSend bean) {
		wmsLesSendService.updateEntity(bean);
	}

	@Override
	public WmsTask createDefaultTaskByBillID(String billId) {
		return sendExcecut.createDefaultTaskByBillID(billId);
	}

	@Override
	public ResultResp setLesSendReturn(String id) {
		WmsLesSend send = wmsLesSendService.getEntity(id);
		if (null != send.getStatus() && Integer.valueOf(2).equals(send.getStatus())) {
			send.setStatus(4);
			wmsLesSendService.updateEntity(send);
			return new ResultResp(ResultResp.SUCCESS_CODE, "操作完成");
		} else {
			return new ResultResp(ResultResp.ERROR_CODE, "当前单据状态不允许进行回单操作");
		}

	}

	@Override
	public ResultResp setLesSendListReturn(String id, int receiveQty) {
		WmsLesSendList sendList = wmsLesSendListService.getEntity(id);
		sendList.setReceiveQty(receiveQty);
		wmsLesSendListService.updateEntity(sendList);
		return new ResultResp(ResultResp.SUCCESS_CODE, "操作完成");
	}

	/**
	 * les发货单
	 */
	@Override
	public PageData<WmsLesSend> getPageDataLesSend(int page, int rows, WmsLesSend wmsLesSend) {
		return wmsLesSendService.getPageDataList(page, rows, wmsLesSend);
	}

	/**
	 * les发货明细单
	 */
	@Override
	public PageData<WmsLesSendList> getPageDataLesSendList(int page, int rows, WmsLesSendList wmsLesSendList) {
		return wmsLesSendListService.getPageDataList(page, rows, wmsLesSendList);
	}

	/**
	 * 保存,修改les发货单据
	 */
	@Override
	public ResultResp saveLesSend(WmsLesSend wmsLesSend) {
		return wmsLesSendService.save(wmsLesSend);
	}

	@Override
	public ResultResp saveLesSendBill(WmsLesSend wmsLesSend) {
		String id = null;
		WmsLesSend send = wmsLesSendService.getInfo(wmsLesSend.getMapSheetNo(), wmsLesSend.getWhCode());
		if (null != send) {// 修改
			if (!BaseModel.INT_INIT.equals(send.getStatus()) && !Integer.valueOf(5).equals(send.getStatus())) {// 已操作,不可以修改
				throw new RuntimeException(wmsLesSend.getMapSheetNo() + "已操作,不允许修改");
			}
			id = send.getId();
			wmsLesSend.setId(id);
			wmsLesSendService.updateEntity(wmsLesSend);
			wmsLesSendListService.delByMapSheetNo(wmsLesSend.getMapSheetNo(), wmsLesSend.getWhCode());
		} else {
			wmsLesSend.setStatus(BaseModel.INT_INIT);
			id = (String) wmsLesSendService.saveEntity(wmsLesSend);
		}
		List<WmsLesSendList> list = wmsLesSend.getWmsLesSendList();
		for (WmsLesSendList lesSendList : list) {
			wmsLesSendListService.saveEntity(lesSendList);
		}
		return new ResultResp(ResultResp.SUCCESS_CODE, ResultResp.SUCCESS_MSG, id);
	}

	/**
	 * 保存,修改les发货明细单
	 */
	@Override
	public ResultResp saveLesSendList(WmsLesSendList wmsLesSendList) {
		return wmsLesSendListService.save(wmsLesSendList);
	}

	/**
	 * 删除les发货单据
	 */
	@Override
	public void deleteLesSend(WmsLesSend wmsLesSend) {
		wmsLesSend = wmsLesSendService.getEntity(wmsLesSend.getId());
		wmsLesSendService.deleteEntity(wmsLesSend);
		wmsLesSendListService.delByMapSheetNo(wmsLesSend.getMapSheetNo(), wmsLesSend.getWhCode());
	}

	/**
	 * 删除les发货明细单
	 */
	@Override
	public ResultResp deleteLesSendList(WmsLesSendList wmsLesSendList) {
		return wmsLesSendListService.del(wmsLesSendList);
	}

	@Override
	public WmsLesSend getInfo(String mapSheetNo, String whCode) {
		return wmsLesSendService.getInfo(mapSheetNo, whCode);
	}

	@Override
	public WmsLesSendList getWmsLesSendListEntity(Serializable id) {
		return wmsLesSendListService.getEntity(id);
	}

	@Override
	public List<WmsLesSendList> getWmsLesSendList(String mapSheetNo, String whCode) {
		return wmsLesSendListService.getWmsLesSendList(mapSheetNo, whCode);
	}

}
