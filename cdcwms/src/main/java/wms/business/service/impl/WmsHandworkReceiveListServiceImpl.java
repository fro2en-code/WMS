package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.beans.BaseModel;
import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsHandworkReceive;
import com.wms.business.WmsHandworkReceiveList;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsHandworkReceiveListService;
import wms.business.service.WmsHandworkReceiveService;

/**
 *
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("wmsHandworkReceiveListService")
public class WmsHandworkReceiveListServiceImpl extends BaseServiceImpl<WmsHandworkReceiveList>
        implements WmsHandworkReceiveListService {

    @Resource
    private WmsHandworkReceiveService wmsHandworkReceiveService;

    /**
     * 分页
     */
    @Override
    public PageData<WmsHandworkReceiveList> getPageData(int page, int rows,
            WmsHandworkReceiveList wmsHandworkReceiveList) {
        return getPageDataByBaseHql("From WmsHandworkReceiveList where 1=1 ", null, page, rows,
                new ArrayList<Serializable>());
    }

    @Override
    public PageData<WmsHandworkReceiveList> getPageDataList(int page, int rows,
            WmsHandworkReceiveList wmsHandworkReceiveList) {
        StringBuilder base_hql = new StringBuilder();
        List<Serializable> params = new ArrayList<>();
        base_hql.append("From WmsHandworkReceiveList where 1=1 ");
        if (!StringUtil.isEmpty(wmsHandworkReceiveList.getMapSheetNo())) {
            base_hql.append(" and mapSheetNo = ?");
            params.add(wmsHandworkReceiveList.getMapSheetNo());
        }
        if (!StringUtil.isEmpty(wmsHandworkReceiveList.getWhCode())) {
            base_hql.append(" and whCode = ?");
            params.add(wmsHandworkReceiveList.getWhCode());
        }

        return getPageDataByBaseHql(base_hql.toString(), " Order By updateTime desc", page, rows, params);
    }

    @Override
    public void delByHandworkReceiveList(String mapSheetNo, String whCode) {
        batchHandle("delete from WmsHandworkReceiveList where mapSheetNo = ? and whCode=?", mapSheetNo, whCode);
    }

    @Override
    public List<WmsHandworkReceiveList> getWmsHandworkReceiveList(String mapSheetNo, String whCode) {
        return findEntityByHQL("from WmsHandworkReceiveList where mapSheetNo = ? and whCode=?", mapSheetNo, whCode);
    }

    @Override
    public ResultResp save(WmsHandworkReceiveList wmsHandworkReceiveList) {
        ResultResp resp = new ResultResp();
        // 通过配送单号查询手工收货单信息
        WmsHandworkReceive wmsHandworkReceive = wmsHandworkReceiveService
                .getReceiveByMapSheetNo(wmsHandworkReceiveList.getMapSheetNo(), wmsHandworkReceiveList.getWhCode());
        if (BaseModel.INT_INIT.equals(wmsHandworkReceive.getStatus())) {
            if (StringUtil.isEmpty(wmsHandworkReceiveList.getId())) {// 新增
                saveEntity(wmsHandworkReceiveList);
                resp.setRetcode("0");
                resp.setRetmsg("新增收货明细单成功！");
            } else {// 修改
                updateEntity(wmsHandworkReceiveList);
                resp.setRetcode("0");
                resp.setRetmsg("修改收货明细单成功！");

            }
        } else {
            resp.setRetcode("-1");
            resp.setRetmsg("该收货单已经生成任务,无法进行操作!");
        }
        return resp;
    }

    @Override
    public ResultResp del(WmsHandworkReceiveList wmsHandworkReceiveList) {
        ResultResp resp = new ResultResp();
        // 通过配送单号查询手工收货单信息
        WmsHandworkReceive wmsHandworkReceive = wmsHandworkReceiveService
                .getReceiveByMapSheetNo(wmsHandworkReceiveList.getMapSheetNo(), wmsHandworkReceiveList.getWhCode());
        if (BaseModel.INT_INIT.equals(wmsHandworkReceive.getStatus())) {
            deleteEntity(wmsHandworkReceiveList);
            resp.setRetcode("0");
            resp.setRetmsg("删除成功！");
        } else {
            resp.setRetcode("-1");
            resp.setRetmsg("该收货单已经生成任务,无法进行操作!");
        }
        return resp;
    }

    @Override
    public void saveWmsHandworkReceiveList(WmsHandworkReceiveList wmsHandworkReceiveList) {
        updateEntity(wmsHandworkReceiveList);
    }
}
