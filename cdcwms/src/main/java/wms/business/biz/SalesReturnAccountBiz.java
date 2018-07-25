package wms.business.biz;

import javax.servlet.http.HttpSession;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.business.SalesReturnAccount;
import com.wms.business.SalesReturnAccountList;

public interface SalesReturnAccountBiz {
	public PageData<SalesReturnAccount> getPageData(int rows, int page, HttpSession session,
			SalesReturnAccount salesReturnAccount);

	public PageData<SalesReturnAccountList> getPageData(int rows, int page, HttpSession session,
			SalesReturnAccountList salesReturnAccountList);

	/**
	 * 退货
	 */
	public ResultResp salesReturn(HttpSession session, SalesReturnAccountList salesReturnAccountList);

	/**
	 * 二次入库
	 */
	public ResultResp again(HttpSession session, String id, int dealNum, String storageCode, String dealCode);

	/**
	 * 供应商退货
	 */
	public ResultResp supplierSalesReturn(HttpSession session, String id, int dealNum, String dealCode);
}
