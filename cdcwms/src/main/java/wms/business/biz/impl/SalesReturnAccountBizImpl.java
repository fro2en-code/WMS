package wms.business.biz.impl;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.SalesReturnAccount;
import com.wms.business.SalesReturnAccountList;
import com.wms.orginfo.OrgConpany;
import com.wms.warehouse.WmsGoods;
import com.wms.warehouse.WmsWarehouse;

import wms.business.biz.SalesReturnAccountBiz;
import wms.business.service.SalesReturnAccountListService;
import wms.business.service.SalesReturnAccountService;
import wms.business.service.WmsHandworkSendListService;
import wms.business.service.WmsHandworkSendService;
import wms.business.service.WmsLesSendListService;
import wms.business.service.WmsLesSendService;
import wms.business.unit.InventoryUnit;
import wms.orginfo.service.CompanyService;
import wms.warehouse.service.GoodsService;
import wms.warehouse.service.StoragService;
import wms.warehouse.service.WarehouseService;

@Service("salesReturnAccountBiz")
public class SalesReturnAccountBizImpl implements SalesReturnAccountBiz {

	@Resource
	private SalesReturnAccountService salesReturnAccountService;
	@Resource
	private SalesReturnAccountListService salesReturnAccountListService;
	@Resource
	private WmsLesSendService wmsLesSendService;
	@Resource
	private WmsLesSendListService wmsLesSendListService;
	@Resource
	private WmsHandworkSendService wmsHandworkSendService;
	@Resource
	private WmsHandworkSendListService wmsHandworkSendListService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private CompanyService companyService;
	@Resource
	private WarehouseService warehouseService;
	@Resource
	private InventoryUnit inventoryUnit;
	@Resource
	private StoragService storagService;

	@Override
	public PageData<SalesReturnAccount> getPageData(int rows, int page, HttpSession session,
			SalesReturnAccount salesReturnAccount) {
		return salesReturnAccountService.getPageData(rows, page, session, salesReturnAccount);
	}

	@Override
	public PageData<SalesReturnAccountList> getPageData(int rows, int page, HttpSession session,
			SalesReturnAccountList salesReturnAccountList) {
		return salesReturnAccountListService.getPageData(rows, page, session, salesReturnAccountList);
	}

	public ResultResp salesReturn(HttpSession session, SalesReturnAccountList salesReturnAccountList) {
		ResultResp resp = new ResultResp();
		// 查找物料是否存在
		WmsGoods goods = goodsService.getGoodsInfo(salesReturnAccountList.getGcode(), salesReturnAccountList.getGtype(),
				salesReturnAccountList.getOraCode(), salesReturnAccountList.getWhCode());
		if (goods == null) {
			resp.setRetcode("-1");
			resp.setRetmsg("该物料不存在,请核实!");
			return resp;
		}
		// 获取该零件的退货台账信息
		SalesReturnAccount salesReturnAccount = salesReturnAccountService.getSalesReturnAccount(
				salesReturnAccountList.getGcode(), salesReturnAccountList.getGtype(),
				salesReturnAccountList.getOraCode(), salesReturnAccountList.getWhCode());
		int num = 0;// 库存总量
		String gname = goodsService.getNameByGcode(salesReturnAccountList.getGcode(),
				salesReturnAccountList.getWhCode());
		salesReturnAccountList.setGname(gname);
		OrgConpany orgconpany = companyService.getConpanyByConCode(salesReturnAccountList.getOraCode());
		String oraName = orgconpany.getName();
		salesReturnAccountList.setOraName(oraName);
		WmsWarehouse wareHouse = warehouseService.getWmsByCode(salesReturnAccountList.getWhCode());
		String whName = wareHouse.getWhName();
		salesReturnAccountList.setWhName(whName);
		if (salesReturnAccount == null) {// 没有退货台账则创建
			SalesReturnAccount salesReturnAccounts = new SalesReturnAccount();
			salesReturnAccounts.setGcode(salesReturnAccountList.getGcode());
			salesReturnAccounts.setGname(salesReturnAccountList.getGname());
			salesReturnAccounts.setGtype(salesReturnAccountList.getGtype());
			salesReturnAccounts.setOraCode(salesReturnAccountList.getOraCode());
			salesReturnAccounts.setOraName(salesReturnAccountList.getOraName());
			salesReturnAccounts.setWhCode(salesReturnAccountList.getWhCode());
			salesReturnAccounts.setWhName(salesReturnAccountList.getWhName());
			salesReturnAccounts.setTotalNum(salesReturnAccountList.getDealNum());
			salesReturnAccountService.save(salesReturnAccounts);
			num = salesReturnAccountList.getDealNum();
		} else {
			num = salesReturnAccount.getTotalNum() + salesReturnAccountList.getDealNum();
			salesReturnAccount.setTotalNum(num);
			salesReturnAccountService.update(salesReturnAccount);
		}
		// 创建退货台账明细
		SalesReturnAccountList salesReturnAccountLists = new SalesReturnAccountList();
		salesReturnAccountLists.setDealCode(salesReturnAccountList.getDealCode());
		salesReturnAccountLists.setDealNum(salesReturnAccountList.getDealNum());
		salesReturnAccountLists.setAccountType(1);
		salesReturnAccountLists.setDealType(4);
		salesReturnAccountLists.setGcode(salesReturnAccountList.getGcode());
		salesReturnAccountLists.setGname(salesReturnAccountList.getGname());
		salesReturnAccountLists.setGtype(salesReturnAccountList.getGtype());
		salesReturnAccountLists.setBatchCode(salesReturnAccountList.getBatchCode());
		salesReturnAccountLists.setOraCode(salesReturnAccountList.getOraCode());
		salesReturnAccountLists.setOraName(salesReturnAccountList.getOraName());
		salesReturnAccountLists.setWhCode(salesReturnAccountList.getWhCode());
		salesReturnAccountLists.setWhName(salesReturnAccountList.getWhName());
		salesReturnAccountLists.setDealNum(salesReturnAccountList.getDealNum());
		salesReturnAccountLists.setTotalNum(num);
		salesReturnAccountLists.setSalesReturnType(salesReturnAccountList.getSalesReturnType());
		salesReturnAccountLists.setDealTime(new Timestamp((new GregorianCalendar()).getTimeInMillis()));
		salesReturnAccountLists.setInTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
		salesReturnAccountLists.setRemark(salesReturnAccountList.getRemark());
		salesReturnAccountListService.save(salesReturnAccountLists);
		resp.setRetcode("0");
		resp.setRetmsg("退货完成");
		return resp;
	}

	/**
	 * 旧的退货代码(需要与发货单绑定的情况下)留着,万一有用呢
	 */
	// public ResultResp salesReturn(HttpSession session, SalesReturnAccountList
	// salesReturnAccountList) {
	// // 查找发货单是否存在
	// ResultResp resp = new ResultResp();
	// int count = 0;// 零件发货的数量
	// if (salesReturnAccountList.getSalesReturnType().equals(0)) {// 手工发货单
	// WmsHandworkSend wmsHandworkSend =
	// wmsHandworkSendService.getInfo(salesReturnAccountList.getDealCode(),
	// salesReturnAccountList.getWhCode());
	// if (wmsHandworkSend == null) {
	// resp.setRetcode("-1");
	// resp.setRetmsg("您填写的发货单不存在,请核实!");
	// return resp;
	// }
	// if (!wmsHandworkSend.getStatus().equals(BaseModel.INT_COMPLETE)) {//
	// 只有当发货单状态为完成时才能退货
	// resp.setRetcode("-1");
	// resp.setRetmsg("该发货单发货未完成,不允许退货!");
	// return resp;
	// }
	// List<WmsHandworkSendList> handworkSendList =
	// wmsHandworkSendListService.getInfo(
	// salesReturnAccountList.getDealCode(), salesReturnAccountList.getGcode(),
	// salesReturnAccountList.getWhCode());
	//
	// if (handworkSendList.size() > 0) {
	// for (WmsHandworkSendList wmsHandworkSendList : handworkSendList) {
	// count += wmsHandworkSendList.getReqQty();
	// }
	// if (salesReturnAccountList.getDealNum() > count) {
	// resp.setRetcode("-1");
	// resp.setRetmsg("退货的数量超出了零件发货数量,请确认!");
	// return resp;
	// }
	// } else {
	// resp.setRetcode("-1");
	// resp.setRetmsg("配送单号,零件号或者退货单据类型填写错误,请重新核实!");
	// return resp;
	// }
	// } else if (salesReturnAccountList.getSalesReturnType().equals(1)) {//
	// les发货单
	// WmsLesSend wmsLesSend =
	// wmsLesSendService.getInfo(salesReturnAccountList.getDealCode(),
	// salesReturnAccountList.getWhCode());
	// if (wmsLesSend == null) {
	// resp.setRetcode("-1");
	// resp.setRetmsg("您填写的发货单不存在,请核实!");
	// return resp;
	// }
	// if (!wmsLesSend.getStatus().equals(BaseModel.INT_COMPLETE)) {
	// resp.setRetcode("-1");
	// resp.setRetmsg("该发货单发货未完成,不允许退货!");
	// return resp;
	// }
	// List<WmsLesSendList> lesSendList =
	// wmsLesSendListService.getInfo(salesReturnAccountList.getDealCode(),
	// salesReturnAccountList.getGcode(), salesReturnAccountList.getWhCode());
	// if (lesSendList.size() > 0) {
	// for (WmsLesSendList wmsLesSendList : lesSendList) {
	// count += wmsLesSendList.getReqQty();
	// }
	// if (salesReturnAccountList.getDealNum() > count) {
	// resp.setRetcode("-1");
	// resp.setRetmsg("退货的数量超出了零件发货数量,请确认!");
	// return resp;
	// }
	// } else {
	// resp.setRetcode("-1");
	// resp.setRetmsg("配送单号或者零件号填写错误,请重新核实!");
	// return resp;
	// }
	// } else {
	// resp.setRetcode("-1");
	// resp.setRetmsg("单据类型错误");
	// return resp;
	// }
	// // 获取该零件的退货台账信息
	// SalesReturnAccount salesReturnAccount =
	// salesReturnAccountService.getSalesReturnAccount(
	// salesReturnAccountList.getGcode(), salesReturnAccountList.getGtype(),
	// salesReturnAccountList.getOraCode(), salesReturnAccountList.getWhCode());
	// int num = 0;// 库存总量
	// String gname =
	// goodsService.getNameByGcode(salesReturnAccountList.getGcode(),
	// salesReturnAccountList.getWhCode());
	// salesReturnAccountList.setGname(gname);
	// OrgConpany orgconpany =
	// companyService.getConpanyByConCode(salesReturnAccountList.getOraCode());
	// String oraName = orgconpany.getName();
	// salesReturnAccountList.setOraName(oraName);
	// WmsWarehouse wareHouse =
	// warehouseService.getWmsByCode(salesReturnAccountList.getWhCode());
	// String whName = wareHouse.getWhName();
	// salesReturnAccountList.setWhName(whName);
	// if (salesReturnAccount == null) {// 没有退货台账则创建
	// SalesReturnAccount salesReturnAccounts = new SalesReturnAccount();
	// salesReturnAccounts.setGcode(salesReturnAccountList.getGcode());
	// salesReturnAccounts.setGname(salesReturnAccountList.getGname());
	// salesReturnAccounts.setGtype(salesReturnAccountList.getGtype());
	// salesReturnAccounts.setOraCode(salesReturnAccountList.getOraCode());
	// salesReturnAccounts.setOraName(salesReturnAccountList.getOraName());
	// salesReturnAccounts.setWhCode(salesReturnAccountList.getWhCode());
	// salesReturnAccounts.setWhName(salesReturnAccountList.getWhName());
	// salesReturnAccounts.setTotalNum(salesReturnAccountList.getDealNum());
	// salesReturnAccountService.save(salesReturnAccounts);
	// num = salesReturnAccountList.getDealNum();
	// } else {
	// num = salesReturnAccount.getTotalNum() +
	// salesReturnAccountList.getDealNum();
	// salesReturnAccount.setTotalNum(num);
	// salesReturnAccountService.update(salesReturnAccount);
	// }
	// // 创建退货台账明细
	// SalesReturnAccountList salesReturnAccountLists = new
	// SalesReturnAccountList();
	// salesReturnAccountLists.setDealCode(salesReturnAccountList.getDealCode());
	// salesReturnAccountLists.setDealNum(salesReturnAccountList.getDealNum());
	// salesReturnAccountLists.setAccountType(1);
	// salesReturnAccountLists.setDealType(4);
	// salesReturnAccountLists.setGcode(salesReturnAccountList.getGcode());
	// salesReturnAccountLists.setGname(salesReturnAccountList.getGname());
	// salesReturnAccountLists.setGtype(salesReturnAccountList.getGtype());
	// salesReturnAccountLists.setOraCode(salesReturnAccountList.getOraCode());
	// salesReturnAccountLists.setOraName(salesReturnAccountList.getOraName());
	// salesReturnAccountLists.setWhCode(salesReturnAccountList.getWhCode());
	// salesReturnAccountLists.setWhName(salesReturnAccountList.getWhName());
	// salesReturnAccountLists.setDealNum(salesReturnAccountList.getDealNum());
	// salesReturnAccountLists.setTotalNum(num);
	// salesReturnAccountLists.setSalesReturnType(salesReturnAccountList.getSalesReturnType());
	// salesReturnAccountLists.setDealTime(new Timestamp((new
	// GregorianCalendar()).getTimeInMillis()));
	// salesReturnAccountLists.setInTime(StringUtil.getCurStringDate("yyyy-MM-dd
	// HH:mm:ss"));
	// salesReturnAccountListService.save(salesReturnAccountLists);
	// resp.setRetcode("0");
	// resp.setRetmsg("退货完成");
	// return resp;
	// }

	@Override
	public ResultResp again(HttpSession session, String id, int dealNum, String storageCode, String dealCode) {
		ResultResp resp = new ResultResp();
		// 获取该零件的退货台账信息
		SalesReturnAccount salesReturnAccount = salesReturnAccountService.getEntity(id);
		if (salesReturnAccount.getTotalNum() < dealNum) {
			resp.setRetcode("-1");
			resp.setRetmsg("您填写的数量超出现有可退货数量,请重新填写!");
			return resp;
		}
		// 获取物料信息
		WmsGoods wmsGoods = goodsService.getGoodsInfo(salesReturnAccount.getGcode(), salesReturnAccount.getGtype(),
				salesReturnAccount.getOraCode(), salesReturnAccount.getWhCode());
		int num = salesReturnAccount.getTotalNum() - dealNum;// 入库后库存总数量
		salesReturnAccount.setTotalNum(num);
		// 新增台账明细
		SalesReturnAccountList salesReturnAccountLists = new SalesReturnAccountList();
		salesReturnAccountLists.setGname(wmsGoods.getGname());
		OrgConpany orgconpany = companyService.getConpanyByConCode(salesReturnAccount.getOraCode());
		String oraName = orgconpany.getName();
		salesReturnAccountLists.setOraName(oraName);
		WmsWarehouse wareHouse = warehouseService.getWmsByCode(salesReturnAccount.getWhCode());
		String whName = wareHouse.getWhName();
		salesReturnAccountLists.setDealCode(dealCode);
		salesReturnAccountLists.setWhName(whName);
		salesReturnAccountLists.setAccountType(2);
		salesReturnAccountLists.setDealType(0);
		salesReturnAccountLists.setSalesReturnType(2);
		salesReturnAccountLists.setGcode(salesReturnAccount.getGcode());
		salesReturnAccountLists.setGtype(salesReturnAccount.getGtype());
		salesReturnAccountLists.setOraCode(salesReturnAccount.getOraCode());
		salesReturnAccountLists.setWhCode(salesReturnAccount.getWhCode());
		salesReturnAccountLists.setDealNum(-dealNum);
		salesReturnAccountLists.setTotalNum(num);
		salesReturnAccountLists.setDealTime(new Timestamp((new GregorianCalendar()).getTimeInMillis()));
		salesReturnAccountLists.setInTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
		salesReturnAccountService.update(salesReturnAccount);
		salesReturnAccountListService.save(salesReturnAccountLists);
		// 调整库存
		inventoryUnit.addWmsStock(storageCode, wmsGoods, dealNum, false, null);
		resp.setRetcode("0");
		resp.setRetmsg("二次入库完成");
		return resp;
	}

	@Override
	public ResultResp supplierSalesReturn(HttpSession session, String id, int dealNum, String dealCode) {
		ResultResp resp = new ResultResp();
		// 获取该零件的退货台账信息
		SalesReturnAccount salesReturnAccount = salesReturnAccountService.getEntity(id);
		if (salesReturnAccount.getTotalNum() < dealNum) {
			resp.setRetcode("-1");
			resp.setRetmsg("您填写的数量超出现有可退货数量,请重新填写!");
			return resp;
		}
		int num = salesReturnAccount.getTotalNum() - dealNum;// 退货后库存总数量
		salesReturnAccount.setTotalNum(num);
		// 新增台账明细
		SalesReturnAccountList salesReturnAccountLists = new SalesReturnAccountList();
		String gname = goodsService.getNameByGcode(salesReturnAccount.getGcode(), salesReturnAccount.getWhCode());
		salesReturnAccountLists.setGname(gname);
		OrgConpany orgconpany = companyService.getConpanyByConCode(salesReturnAccount.getOraCode());
		String oraName = orgconpany.getName();
		salesReturnAccountLists.setOraName(oraName);
		WmsWarehouse wareHouse = warehouseService.getWmsByCode(salesReturnAccount.getWhCode());
		String whName = wareHouse.getWhName();
		salesReturnAccountLists.setDealCode(dealCode);
		salesReturnAccountLists.setWhName(whName);
		salesReturnAccountLists.setAccountType(3);
		salesReturnAccountLists.setDealType(0);
		salesReturnAccountLists.setSalesReturnType(3);
		salesReturnAccountLists.setGcode(salesReturnAccount.getGcode());
		salesReturnAccountLists.setGtype(salesReturnAccount.getGtype());
		salesReturnAccountLists.setOraCode(salesReturnAccount.getOraCode());
		salesReturnAccountLists.setWhCode(salesReturnAccount.getWhCode());
		salesReturnAccountLists.setDealNum(-dealNum);
		salesReturnAccountLists.setTotalNum(num);
		salesReturnAccountLists.setDealTime(new Timestamp((new GregorianCalendar()).getTimeInMillis()));
		salesReturnAccountLists.setInTime(StringUtil.getCurStringDate("yyyy-MM-dd HH:mm:ss"));
		salesReturnAccountService.update(salesReturnAccount);
		salesReturnAccountListService.save(salesReturnAccountLists);
		resp.setRetcode("0");
		resp.setRetmsg("退货完成");
		return resp;
	}

}
