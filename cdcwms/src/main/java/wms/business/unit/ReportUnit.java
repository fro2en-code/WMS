package wms.business.unit;

import java.util.Date;
import java.util.Map;

import com.plat.common.page.PageData;
import com.wms.business.StockReport;

/**
 * 报表
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年6月8日
 */
public interface ReportUnit {
    /**
     * 生成库存月报表数据
     */
    void createStockReport(Date data);

    /**
     * 运行库存月报表数据获取
     */
    void runSotckReport();

    /**
     * 查询库存月报表
     */
    PageData<StockReport> queryStockMonthlyStatement(int page, int rows, StockReport stockReport);

    /**
     * 查询出库明细
     */
    PageData<Map<String, Object>> queryOutStorage(int page, int rows, String SUPPL_NO, String PART_NO,
            String MAP_SHEET_NO, String begin_time, String end_time, String whCode);

    /**
     * 查询入库明细
     */
    PageData<Map<String, Object>> queryIntoStorage(int page, int rows, String SUPPL_NO, String PART_NO,
            String MAP_SHEET_NO, String begin_time, String end_time, String whCode);

}
