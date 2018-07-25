package wms.business.unit;

import com.wms.warehouse.WmsWarehouse;

/**
 * 手r持unit
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年3月30日
 */
public interface MobileUnit {
    /**
     * 获取手持默认仓库信息
     *
     * @return 仓库
     */
    WmsWarehouse getDefaultWareHouse();
}
