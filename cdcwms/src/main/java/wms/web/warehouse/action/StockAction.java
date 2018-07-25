package wms.web.warehouse.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plat.common.action.BaseAction;
import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.utils.ExcelUtil.Excel;
import com.wms.warehouse.WmsStock;
import com.ymt.utils.ExcelUtils;
import com.ymt.utils.ExcelUtils.ExportExcelHelper;

import wms.business.biz.WmsStockBiz;

@Controller
@RequestMapping(value = "/stock")
public class StockAction extends BaseAction {

    @Resource
    private WmsStockBiz wmsStockBiz;

    /*
     * 库存列表
     */
    @RequestMapping(value = "/list.action")
    @ResponseBody
    public PageData<WmsStock> list(HttpSession session, int page, int rows, WmsStock ws) {
        ws.setWhCode(getBindWhCode(session));
        return wmsStockBiz.getPageData(page, rows, ws);
    }

    @RequestMapping(value = "/toList.action")
    public String toList(HttpServletRequest request) {

        new Excel().setColumn("whCode", "仓库代码").setColumn("zoneCode", "库区代码").setColumn("storageCode", "库位代码")
                .setColumn("gcode", "物料编码").setColumn("gname", "物料名称").setColumn("gtype", "零件用途")
                .setColumn("supCode", "供应商代码").setColumn("supName", "供应商名字").setColumn("batchCode", "批次")
                .setColumn("quantity", "库存数量").setColumn("prePickNum", "预拣货数量").setColumn("lockType", "锁定库存类型")
                .setColumn("lockNum", "锁定库存数量").setClassName(WmsStock.class.getName()).setTempName("库存信息")
                .Finish(request);

        return "stockList";
    }

    @RequestMapping("/export.action")
    @ResponseBody
    public void export(HttpServletResponse resp, HttpSession session, final String keyNo, final WmsStock stock) {
        stock.setWhCode(getBindWhCode(session));
        ExportExcelHelper helper = new ExcelUtils.ExportExcelHelper() {

            @Override
            public PageData<? extends BaseModel> getPageDate(int page, int size) throws Exception {
                return wmsStockBiz.getPageData(page, ExcelUtils.pageSize, stock);
            }

            @Override
            public String getKey() {
                return keyNo;
            }
        };
        helper.run(resp);
    }
}
