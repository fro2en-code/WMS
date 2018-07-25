package wms.web.warehouse.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.BaseModel;
import com.plat.common.beans.UserBean;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.ExcelUtil.Excel;
import com.wms.warehouse.WmsGoods;
import com.ymt.utils.ExcelUtils;
import com.ymt.utils.ExcelUtils.ExportExcelHelper;

import wms.business.biz.WarehouseManagementBiz;

/**
 * 物料信息
 */
@Controller
@RequestMapping("/goods")
public class GoodsAction extends BaseAction {

	@Resource
	private WarehouseManagementBiz warehouseManagementBiz;

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletRequest req) {
		new Excel().setColumn("gcode", "物料编号").setColumn("gname", "物料名称")
				.setColumn("units", "计量单位  件  包   框   托盘   盒").setColumn("oraCode", "供应商代码")
				.setColumn("oraName", "供应商名称").setColumn("gtype", "零件用途").setColumn("chineseDes", "中文描述")
				.setColumn("englishDes", "英文描述").setColumn("abcType", "ABC类型").setColumn("gValue", "单件货值")
				.setColumn("sendBoxid", "发运包装编号").setColumn("boxType", "存储包装类型(1纸包装 2 料盒 3大件器具)")
				.setColumn("boxNum", "存储包装包含数").setColumn("packLength", "包装规格长度").setColumn("packWidth", "包装规格宽度")
				.setColumn("packHeigth", "包装规格高度").setColumn("packWeigth", "包装重量")
				.setColumn("batchType", "批次管理(1 批次管理 0不分批次)").setColumn("storageType", "存储类型(1托盘 2堆放)")
				.setColumn("trayNum", "托盘存储包装数").setColumn("receivetrayNum", "托盘收货包装数")
				.setColumn("storagezoneType", "指定库位/库区动态(1指定库位 0库区动态存储)")
				.setColumn("storagezoneId", "库位/库区编号(多库位，多库区，编码之间以;号间隔)").setColumn("repleniShment", "补货的库存数")
				.setColumn("moveNum", "移库的库存数").setColumn("maxNum", "最大的库存数").setColumn("storageMaxNum", "库位最大库存数")
				.setColumn("warningMaxNum", "最大预警库存").setColumn("warningMinNum", "最小预警库存")
				.setColumn("forkliftNum", "叉车数量").setColumn("whCode", "仓库代码").setColumn("whName", "仓库名称")
				.setColumn("qualityDay", "保质期").setColumn("innerCoding", "内部编码")
				.setColumn("skipPutaway", "搬运直接上架(0 是 ,1 否)").setClassName(WmsGoods.class.getName()).setTempName("物料信息")
				.Finish(req);
		return "goodsList";
	}

	/**
	 * 查询物料分页
	 *
	 * @return
	 */
	@RequestMapping("/list.action")
	@ResponseBody
	public PageData<WmsGoods> list(HttpServletResponse resp, HttpSession session, int page, int rows, WmsGoods goods) {
		goods.setWhCode(getBindWhCode(session));
		return warehouseManagementBiz.getPageData(page, rows, goods);
	}

	@RequestMapping("/export.action")
	public void export(HttpServletResponse resp, HttpSession session, final String keyNo, final WmsGoods goods) {
		goods.setWhCode(getBindWhCode(session));
		ExportExcelHelper helper = new ExcelUtils.ExportExcelHelper() {

			@Override
			public PageData<? extends BaseModel> getPageDate(int page, int size) throws Exception {
				return warehouseManagementBiz.getPageData(page, ExcelUtils.pageSize, goods);
			}

			@Override
			public String getKey() {
				return keyNo;
			}
		};
		helper.run(resp);
	}

	private void validata(WmsGoods goods) {
		if (StringUtils.isEmpty(goods.getOraCode())) {
			throw new RuntimeException("供应商请从下拉框里选取");
		}
	}

	/**
	 * 保存、修改物料
	 *
	 * @return
	 */
	@RequestMapping("save.action")
	@ResponseBody
	public ResultResp saveRoleMenu(HttpServletResponse resp, HttpSession session, WmsGoods goods) {
		validata(goods);
		goods.setWhCode((String) session.getAttribute("defaultWhCode"));
		goods.setWhName((String) session.getAttribute("defaultWhName"));
		UserBean user = getUserInfo(session);
		goods.setUpdateUser(user.getLoginname());
		goods.setInsertUser(user.getLoginname());
		return warehouseManagementBiz.save(goods);
	}

	/**
	 * 删除物料
	 *
	 * @return
	 */
	@RequestMapping("del.action")
	@ResponseBody
	public ResultResp delRoleMenu(HttpServletResponse resp, String id) {
		return warehouseManagementBiz.delGoods(id);
	}

	@RequestMapping("getWmsGoodsByKey.action")
	@ResponseBody
	public List<Map<String, String>> getWmsGoodsByKey(HttpServletResponse resp,
			@RequestParam(value = "q", defaultValue = "") String key, HttpSession session) {
		String whCode = getBindWhCode(session);
		return warehouseManagementBiz.getWmsGoodsByKey(key, whCode);
	}

	@RequestMapping("getWmsGoodsInfoByKey.action")
	@ResponseBody
	public List<Map<String, String>> getWmsGoodsInfoByKey(HttpServletResponse resp,
			@RequestParam(value = "q", defaultValue = "") String key, HttpSession session) {
		String whCode = getBindWhCode(session);
		return warehouseManagementBiz.getWmsGoodsInfoByKey(key, whCode);
	}
}
