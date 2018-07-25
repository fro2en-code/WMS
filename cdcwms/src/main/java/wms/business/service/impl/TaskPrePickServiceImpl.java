package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.TaskPrePick;

import its.base.service.BaseServiceImpl;
import wms.business.service.TaskPicPickService;
import wms.orginfo.service.CompanyService;
import wms.warehouse.service.WarehouseService;

/**
 * 预拣货任务定义
 *
 * @author wangzz
 *
 */
@Service("taskPicPickService")
public class TaskPrePickServiceImpl extends BaseServiceImpl<TaskPrePick> implements TaskPicPickService {
    @Resource
    private CompanyService companyService;
    @Resource
    private WarehouseService warehouseService;

    /**
     * 预拣货任务分页
     */
    @Override
    public PageData<TaskPrePick> getPageDataList(int page, int rows, TaskPrePick taskPrePick) {
        StringBuilder base_hql = new StringBuilder();
        List<Serializable> params = new ArrayList<>();
        base_hql.append("From TaskPrePick where 1=1 ");
        if (!StringUtil.isEmpty(taskPrePick.getExpression())) {
            base_hql.append(" and expression =? ");
            params.add(taskPrePick.getExpression());
        }
        if (!StringUtil.isEmpty(taskPrePick.getWhCode())) {
            base_hql.append("and whCode = ?");
            params.add(taskPrePick.getWhCode());
        }
        return getPageDataByBaseHql(base_hql.toString(), " Order By insertTime desc", page, rows, params);
    }

    /**
     * 保存，修改预拣货任务
     */
    @Override
    public ResultResp save(TaskPrePick taskPrePick) {
        ResultResp resp = new ResultResp();
        if (StringUtil.isEmpty(taskPrePick.getId())) {// 新增
            taskPrePick.setUpdateUser("");
            resp.setBillId((String) saveEntity(taskPrePick));
            resp.setRetcode("0");
            resp.setRetmsg("新增预拣货任务成功！");
            return resp;
        } else {// 更新
            updateEntity(taskPrePick);
            resp.setRetcode("0");
            resp.setRetmsg("修改预拣货任务成功！");
            return resp;
        }
    }

    /**
     * 删除预拣货任务
     */
    @Override
    public ResultResp del(TaskPrePick taskPrePick) {
        ResultResp resp = new ResultResp();
        deleteEntity(taskPrePick);
        resp.setRetcode("0");
        resp.setRetmsg("删除成功！");
        return resp;
    }
}
