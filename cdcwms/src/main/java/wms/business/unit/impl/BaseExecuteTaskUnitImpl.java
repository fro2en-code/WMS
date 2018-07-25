package wms.business.unit.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.plat.common.utils.BeanUtils;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsTask;
import com.wms.business.WmsTaskBill;
import com.wms.business.WmsTaskBillList;
import com.wms.warehouse.WmsGoods;
import com.ymt.utils.MessageSender;
import com.ymt.utils.TaskUtils;
import com.ymt.utils.UserUtils;

import wms.business.service.WmsTaskService;
import wms.business.unit.ExecuteTaskUnit;
import wms.business.unit.InventoryUnit;
import wms.business.unit.TaskUnit;
import wms.userRelation.service.UserSessionIDService;
import wms.userRelation.service.ZoneManagerService;
import wms.warehouse.service.GoodsService;

/**
 * 基础任务类
 *
 * @author zhouxianglh@gmail.com
 *
 */
public abstract class BaseExecuteTaskUnitImpl implements ExecuteTaskUnit {
    private static final Logger logger = LoggerFactory.getLogger(BaseExecuteTaskUnitImpl.class);
    @Resource
    protected GoodsService goodsService;
    @Resource
    protected InventoryUnit inventoryUnit;
    @Resource
    protected MessageSender messageSender;
    @Resource
    protected TaskUnit taskUnit;
    @Resource
    protected TaskUtils taskUtils;
    @Resource
    private UserSessionIDService userSessionIDService;
    @Resource
    protected UserUtils userUtils;
    @Resource
    protected WmsTaskService wmsTaskService;

    @Resource
    protected ZoneManagerService zoneManagerService;

    /**
     * 创建默认 ask(包括taskbill,taskbillList)
     */
    public WmsTask createDefaultTask(WmsTask task, String billId, String taskDesc) {
        WmsTask wmsTask = new WmsTask();
        wmsTask.setBeginTime(StringUtil.getCurStringDate(StringUtil.PATTERN));
        BeanUtils.copyProperty(task, wmsTask, "whCode", "level");
        wmsTask.setType(getTaskType());
        wmsTask.setStatus(0);
        wmsTask.setTaskdesc(taskDesc);
        wmsTask.setBillid(billId);
        //
        WmsTaskBill wmsTaskBill = new WmsTaskBill();
        if (null != task.getWmsTaskBill()) {
            BeanUtils.copyProperty(task.getWmsTaskBill(), wmsTaskBill, "whCode", "oraCode", "oraName", "source",
                    "billNumber");
        } else {
            wmsTaskBill.setWhCode(task.getWhCode());
        }
        wmsTaskBill.setBillid(billId);
        wmsTaskBill.setType(getTaskType());
        wmsTaskBill.setStatus(0);
        //
        List<WmsTaskBillList> list = new ArrayList<>();
        //
        wmsTask.setWmsTaskBill(wmsTaskBill);
        wmsTaskBill.setWmsTaskBillLists(list);
        return wmsTask;
    }

    @Override
    public WmsTask createDefaultTaskByBillID(String billID) {
        throw new RuntimeException("当前方法未实现");
    }

    @Override
    public Set<String> getExector(WmsTask task) {
        Set<String> set = taskUtils.getTaskUserByTaskCode(task.getType(), task.getWhCode());
        if (set.size() > 0) {
            return userSessionIDService.getOnline(set);
        } else {
            return new HashSet<>();
        }
    }

    public Set<String> getStorageMange(String starageCode) {
        return zoneManagerService.getManager(starageCode);
    }

    @Override
    public boolean isAllCompleteNext() {
        return false;
    }

    public void lockWmsStock(String whCode, String nowStorage, String nextStorage,
            List<WmsTaskBillList> wmsTaskBillLists) {
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            lockWmsStock(whCode, nowStorage, nextStorage, wmsTaskBillList);
        }
    }

    public void lockWmsStock(String whCode, String nowStorage, String nextStorage,
            WmsTaskBillList... wmsTaskBillLists) {
        for (WmsTaskBillList wmsTaskBillList : wmsTaskBillLists) {
            lockWmsStock(whCode, nowStorage, nextStorage, wmsTaskBillList);
        }
    }

    private void lockWmsStock(String whCode, String nowStorage, String nextStorage, WmsTaskBillList billList) {
        WmsGoods wmsGoods = goodsService.getGoodsInfo(billList.getGcode(), billList.getGtype(), billList.getOraCode(),
                whCode);
        inventoryUnit.lockWmsStock(nowStorage, nextStorage, wmsGoods, billList.getGoodNeedNum());
    }

    /**
     * 发送消息通知
     */
    public void sendMessage(WmsTask task) {
        Set<String> set = getExector(task);
        for (String string : set) {
            // messageSender.sendMessage(string, task.getTaskdesc());
            if (logger.isDebugEnabled()) {
                logger.debug("消息发送人:{} 消息内容:{}", string, task.getTaskdesc());
            }
        }
    }

    @Override
    public void setTaskCancel(WmsTask task) {
    }

    @Override
    public void setTaskComplete(WmsTask task) {
    }

    @Override
    public void setTaskRecive(WmsTask task) {
    }

}
