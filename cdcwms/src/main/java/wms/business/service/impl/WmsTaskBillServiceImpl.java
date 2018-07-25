package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.business.WmsTaskBill;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsTaskBillService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsTaskBillService")
public class WmsTaskBillServiceImpl extends BaseServiceImpl<WmsTaskBill> implements WmsTaskBillService {

    /**
     * 分页
     */
    @Override
    public PageData<WmsTaskBill> getPageData(int page, int rows, WmsTaskBill wmsTaskBill) {
        return getPageDataByBaseHql("From WmsTaskBill where 1=1 ", null, page, rows, new ArrayList<Serializable>());
    }

    @Override
    public void updateStatusByTaskId(String taskId, Integer status, String updateUser) {
        WmsTaskBill taskBill = getTaskBillByTaskId(taskId);
        taskBill.setStatus(status);
        taskBill.setUpdateUser(updateUser);
        saveEntity(taskBill);
    }

    @Override
    public WmsTaskBill getTaskBillByTaskId(String taskId) {
        return (WmsTaskBill) uniqueResult("from WmsTaskBill where taskid=?", taskId);
    }

    @Override
    public Boolean lesReciveTaskExist(String boxCarid) {
        return count(
                "select count(*) from WmsTask as wmsTask,WmsTaskBill as wmsTaskBill,WmsTaskBillList as wmsTaskBillList where wmsTask.status=2 and wmsTask.id=wmsTaskBill.taskid and wmsTaskBill.id = wmsTaskBillList.parentid and wmsTaskBillList.boxCardid= ?",
                boxCarid) == 1;
    }
}
