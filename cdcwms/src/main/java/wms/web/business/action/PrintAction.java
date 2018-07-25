package wms.web.business.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.plat.common.action.BaseAction;
import com.wms.business.WmsHandworkSend;
import com.wms.business.WmsHandworkSendList;
import com.wms.orginfo.OrgConpany;

import cn.rtzltech.user.utils.StringUtil;
import wms.business.biz.CompanyBiz;
import wms.business.biz.WarehouseManagementBiz;
import wms.business.biz.WmsHandworkSendBiz;

/**
 * 通用打印action
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/print")
public class PrintAction extends BaseAction {
	// 盘点
	public static final Integer check = 9;
	// 手工收货
	public static final Integer handworkReceive = 1;
	// 手工发货
	public static final Integer handworkSend = 6;
	// 手工发货中打印随箱卡
	public static final Integer handworkSendBox = 5;
	// LES收货
	public static final Integer lesReceive = 2;
	// LES发货
	public static final Integer lesSend = 7;
	// 发货区发货
	public static final Integer sendTruck = 10;
	@Resource
	private CompanyBiz companyBiz;
	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;
	@Resource
	private WmsHandworkSendBiz wmsHandworkSendBiz;

	public String error() {
		return "errorPrint";
	}

	/**
	 * 渲染页面
	 */
	@RequestMapping(value = "/toList.action")
	public String toList(HttpServletRequest request, HttpServletResponse response, HttpSession session, int type,
			String mapSheetNo) {
		String whCode = getBindWhCode(session);
		if (type == handworkSend && ("3PLXNY".equals(whCode) || "QRXNY".equals(whCode))) {// 新能源仓库的手工发货单
			return handworkSendPrint(request, response, session, mapSheetNo, whCode);
		} else if (type == handworkSendBox && ("3PLXNY".equals(whCode) || "QRXNY".equals(whCode))) {// 新能源仓库的手工发货单随箱卡
			return handworkSendBoxPrint(request, response, session, mapSheetNo, whCode);
		} else {
			return error();
		}
	}

	// 手工发货随箱卡打印
	public String handworkSendBoxPrint(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			String mapSheetNo, String whCode) {
		List<WmsHandworkSendList> handworkSendList = wmsHandworkSendBiz.getWmsHandworkSendList(mapSheetNo, whCode);
		if (handworkSendList.size() == 0) {// 当明细为空时不允许打印
			return "errorPrint";
		}
		// 创建打印的集合
		List<Map<String, Object>> printList = new ArrayList<>();
		for (int i = 0; i < handworkSendList.size(); i++) {
			Map<String, Object> handworkSendLists = new HashMap<>();
			handworkSendLists.put("gCode", handworkSendList.get(i).getPartNo());
			String goodsName = warehouseManagementBiz.getNameByGcode(handworkSendList.get(i).getPartNo(), whCode);
			String gName = "";
			if (goodsName.length() > 16) {// 零件名字超过16位则后面用...代替
				gName = goodsName.substring(0, 16).concat("...");
			} else {
				gName = goodsName;
			}
			handworkSendLists.put("gName", gName);
			handworkSendLists.put("oraCode", handworkSendList.get(i).getSupplNo());
			OrgConpany orgConpany = companyBiz.getConpanyByConCode(handworkSendList.get(i).getSupplNo());
			String oraCode = "";
			if (orgConpany.getName().length() > 16) {// 供应商名字超过16位则后面用...代替
				oraCode = orgConpany.getName().substring(0, 16).concat("...");
			} else {
				oraCode = orgConpany.getName();
			}
			handworkSendLists.put("oraName", oraCode);
			if (handworkSendList.get(i).getSendPackageNum() == null) {// 当发运包装容量为空时打印的数量为需求数量
				handworkSendLists.put("number", handworkSendList.get(i).getReqQty());
			} else {// 当不为空时需要根据需求数量和包装容量进行打印
				int sendPackageNum = handworkSendList.get(i).getSendPackageNum();// 包装容量
				int reqQrt = handworkSendList.get(i).getReqQty();// 需求数量
				if ((sendPackageNum < reqQrt)) {// 当包装容量小于需求数量时,则需要多次打印,例如包装容量为100,需求数量为250,则打印三次,两次数量为100,一次数量为50
					for (int j = 0; j < reqQrt / sendPackageNum; j++) {
						handworkSendLists.put("number", sendPackageNum);
						handworkSendLists.put("time", StringUtil.getCurStringDate("yyyy/MM/dd HH:mm"));
						printList.add(handworkSendLists);
					}
					if (reqQrt - ((reqQrt / sendPackageNum) * sendPackageNum) > 0) {// 打印剩余的数量
						Map<String, Object> map = new HashMap<>();
						map.put("gCode", handworkSendList.get(i).getPartNo());
						map.put("gName", gName);
						map.put("oraCode", handworkSendList.get(i).getSupplNo());
						map.put("oraName", oraCode);
						map.put("number", reqQrt - ((reqQrt / sendPackageNum) * sendPackageNum));
						map.put("time", StringUtil.getCurStringDate("yyyy/MM/dd HH:mm"));
						printList.add(map);
						continue;
					} else {
						continue;
					}
				} else {
					handworkSendLists.put("number", handworkSendList.get(i).getReqQty());
				}
			}
			handworkSendLists.put("time", StringUtil.getCurStringDate("yyyy/MM/dd HH:mm"));
			printList.add(handworkSendLists);
		}
		request.setAttribute("printList", printList);
		return "handworkSendBoxPrint";
	}

	// 手工发货打印
	@SuppressWarnings("unchecked")
	public String handworkSendPrint(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			String mapSheetNo, String whCode) {
		// 获取手工发货单明细
		List<WmsHandworkSendList> handworkSends = wmsHandworkSendBiz.getWmsHandworkSendList(mapSheetNo, whCode);
		// 获取手工发货单
		WmsHandworkSend wmsHandworkSend = wmsHandworkSendBiz.getInfo(mapSheetNo, whCode);
		String time = wmsHandworkSend.getLastRecRequrieTime().trim();
		if (handworkSends.size() == 0) {// 当明细为空时不允许打印
			return "errorPrint";
		}

		// 因为所有零件的供应商编码一样的,所以取第一个
		String oraCode = handworkSends.get(0).getSupplNo();
		request.setAttribute("oraCode", oraCode);
		OrgConpany orgConpany = companyBiz.getConpanyByConCode(oraCode);
		request.setAttribute("oraName", orgConpany.getName());
		request.setAttribute("mapSheetNo", mapSheetNo);
		Date date = new Date();
		request.setAttribute("year",
				(time == null || time.equals("")) ? StringUtil.DateToStr(date, "yyyy") : time.substring(0, 4));
		request.setAttribute("moon",
				(time == null || time.equals("")) ? StringUtil.DateToStr(date, "MM") : time.substring(5, 7));
		request.setAttribute("day",
				(time == null || time.equals("")) ? StringUtil.DateToStr(date, "dd") : time.substring(8, 10));
		// 创建存储打印内容的集合
		int printSize = 8;
		Map<String, Object>[] printList = new Map[printSize];
		for (int i = 0, j = printSize; i < j; i++) {
			WmsHandworkSendList wmsHandworkSendList = null;
			if (i < handworkSends.size()) {
				wmsHandworkSendList = handworkSends.get(i);
				Map<String, Object> handworkSend = new HashMap<>();
				handworkSend.put("gCode", wmsHandworkSendList.getPartNo());
				// 根据物料编码存储物料名字
				handworkSend.put("gName",
						warehouseManagementBiz.getNameByGcode(wmsHandworkSendList.getPartNo(), whCode));
				handworkSend.put("sendPackageNo", wmsHandworkSendList.getSendPackageNo());
				handworkSend.put("sendPackageNum", wmsHandworkSendList.getSendPackageNum());
				handworkSend.put("reqPackageNum", wmsHandworkSendList.getReqPackageNum());
				handworkSend.put("reqQty", wmsHandworkSendList.getReqQty());
				printList[i] = handworkSend;
			} else if (i == handworkSends.size()) {
				Map<String, Object> handworkSend = new HashMap<>();
				handworkSend.put("gCode", "以下空白");
				printList[i] = handworkSend;
				continue;
			} else {
				break;
			}
		}
		request.setAttribute("printList", printList);
		return "handworkSendPrint";
	}

}
