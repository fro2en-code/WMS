package wms.web.business.action;

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
import com.wms.warehouse.WmsObject;
import com.ymt.utils.ExcelUtils;
import com.ymt.utils.ExcelUtils.ExportExcelHelper;

import wms.business.biz.AccountBiz;

/**
 * 零件管理
 *
 * @author wzz
 *
 */
@Controller
@RequestMapping("/wmsObject")
public class WmsObjectAction extends BaseAction {

    @Resource
    private AccountBiz accountBiz;

    /**
     * 渲染页面
     */
    @RequestMapping("/toList.action")
    public String toList(HttpServletRequest rep) {
        new Excel().setColumn("whCode", "仓库代码").setColumn("whName", "仓库名称").setColumn("gcode", "物料编号")
                .setColumn("gname", "物料名称").setColumn("gtype", "零件用途").setColumn("supCode", "供应商代码")
                .setColumn("supName", "供应商名字").setColumn("batchCode", "批次号").setColumn("inTime", "入库时间")
                .setColumn("outTime", "过期时间").setColumn("quantity", "零件数量").setColumn("lockNum", "锁定零件数量")
                .setClassName(WmsObject.class.getName()).setTempName("零件信息").Finish(rep);
        return "wmsObjectList";
    }

    /**
     * 查询零件管理分页
     *
     * @return
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public PageData<WmsObject> list(HttpSession session, int page, int rows, WmsObject wmsObject) {
        wmsObject.setWhCode(getBindWhCode(session));
        return accountBiz.getPageData(page, rows, wmsObject);
    }

    @RequestMapping("/export.action")
    @ResponseBody
    public void export(HttpServletResponse resp, HttpSession session, final String keyNo, final WmsObject wmsObject) {
        wmsObject.setWhCode(getBindWhCode(session));
        ExportExcelHelper helper = new ExcelUtils.ExportExcelHelper() {

            @Override
            public PageData<? extends BaseModel> getPageDate(int page, int size) throws Exception {
                return accountBiz.getPageData(page, ExcelUtils.pageSize, wmsObject);
            }

            @Override
            public String getKey() {
                return keyNo;
            }
        };
        helper.run(resp);
    }
}
