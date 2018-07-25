package wms.web.business.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.page.PageData;
import com.plat.common.utils.ExcelUtil.Excel;
import com.ymt.utils.ExcelUtils;
import com.ymt.utils.ExcelUtils.ExportExcelHelper;

import wms.business.biz.ReportBiz;

/**
 * 出库明细
 *
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/outStorage")
public class OutStorageListAction extends BaseAction {
	@Resource
	private ReportBiz reportBiz;

	@RequestMapping("/export.action")
	public void export(HttpServletResponse resp, final HttpSession session, final String keyNo, final String SUPPL_NO,
			final String PART_NO, final String MAP_SHEET_NO, final String begin_time, final String end_time) {
		ExportExcelHelper helper = new ExcelUtils.ExportExcelHelper() {

			@Override
			public String getKey() {
				return keyNo;
			}

			@Override
			public PageData<Map<String, Object>> getPageDate(int page, int size) throws Exception {
				return reportBiz.queryOutStorage(page, ExcelUtils.pageSize, SUPPL_NO, PART_NO, MAP_SHEET_NO, begin_time,
						end_time, getBindWhCode(session));
			}
		};
		helper.run(resp);
	}

	@RequestMapping("list.action")
	@ResponseBody
	public PageData<Map<String, Object>> list(HttpSession session, int page, int rows, String SUPPL_NO, String PART_NO,
			String MAP_SHEET_NO, String begin_time, String end_time) {
		return reportBiz.queryOutStorage(page, rows, SUPPL_NO, PART_NO, MAP_SHEET_NO, begin_time, end_time,
				getBindWhCode(session));
	}

	/**
	 * 渲染页面
	 */
	@RequestMapping("/toList.action")
	public String toList(HttpServletRequest req) {
		new Excel().setColumn("MAP_SHEET_NO", "配送单号").setColumn("DELIVERY_REC", "目的地代码").setColumn("SUPPL_NO", "供应商编码")
				.setColumn("ora_name", "供应商名字").setColumn("PART_NO", "零件号").setColumn("inner_coding", "内部编码")
				.setColumn("SEND_PACKAGE_NUM", "发运包装容量").setColumn("REQ_PACKAGE_NUM", "需求箱数")
				.setColumn("REQ_QTY", "需求数量").setColumn("begin_time", "开始时间").setColumn("end_time", "结束时间")
				.setColumn("G_TYPE", "零件用途").setClassName("出库明细").setTempName("出库明细").Finish(req);
		return "outStorageList";
	}
}
