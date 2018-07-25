package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.StringUtil;
import com.wms.business.WmsStockLockLog;
import com.ymt.utils.SerialNumberUtils;
import com.ymt.utils.StockException;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsStockLockLogService;

@Service("WmsStockLockLogService")
public class WmsStockLockLogServiceImpl extends BaseServiceImpl<WmsStockLockLog> implements WmsStockLockLogService {

    private SerialNumberUtils utils = new SerialNumberUtils() {

        @Override
        public boolean isExpired(Date startTime) {
            return new Date().getTime() / (1000L * 60 * 60 * 24) == startTime.getTime() / (1000L * 60 * 60 * 24);// 是否为同一天
        }
    };

    @Override
    public void del(Serializable id) {
        WmsStockLockLog bean = getEntity(id);
        if ("已操作".equals(bean.getState())) {
            throw new RuntimeException("已操作的单据不能删除");
        }
        deleteEntity(bean);
    }

    @Override
    public PageData<WmsStockLockLog> getPageData(int page, int rows, WmsStockLockLog bean) {
        StringBuilder baseSQL = new StringBuilder("from WmsStockLockLog where whCode = ? ");
        List<Serializable> params = new ArrayList<>();
        params.add(bean.getWhCode());
        if (StringUtils.isNotEmpty(bean.getgCode())) {
            baseSQL.append(" and gCode like ? ");
            params.add("%" + bean.getgCode() + "%");
        }
        if (StringUtils.isNotEmpty(bean.getOraCode())) {
            baseSQL.append(" and oraCode like ? ");
            params.add("%" + bean.getOraCode() + "%");
        }
        return getPageDataByBaseHql(baseSQL.toString(), " Order By updateTime desc", page, rows, params);
    }

    private void lockStock(WmsStockLockLog bean) {
        // 查询是否可以操作
        StringBuilder sql = new StringBuilder(
                "SELECT count(t.Lock_num) FROM wms_stock t WHERE t.Wh_code = ? and t.Lock_num > 0 AND t.sup_code = ? ");
        List<Serializable> params = new ArrayList<>();
        params.add(bean.getWhCode());
        params.add(bean.getOraCode());
        if (StringUtils.isNotEmpty(bean.getgCode())) {
            sql.append(" AND t.G_code = ? AND t.G_TYPE = ? ");
            params.add(bean.getgCode());
            params.add(bean.getgType());
        }
        if (StringUtils.isNotEmpty(bean.getStartTime())) {
            sql.append(" AND t.Batch_code >= ? ");
            params.add(bean.getOraCode() + bean.getStartTime().replace("-", ""));
        }
        if (StringUtils.isNotEmpty(bean.getEndTime())) {
            sql.append(" AND t.Batch_code <= ? ");
            params.add(bean.getOraCode() + bean.getEndTime().replace("-", ""));
        }
        Number count = (Number) uniqueSqlResult(sql.toString(), params.toArray(new Serializable[params.size()]));
        if (null == count || count.intValue() == 0) {// 如果存在锁定库存就不可以锁定
            // 执行锁定库存操作
            StringBuilder executSQL = new StringBuilder("UPDATE wms_stock SET "
                    + "wms_stock.Lock_num = IFNULL(wms_stock.Lock_num, 0) + IFNULL(wms_stock.Quantity, 0) + IFNULL(Pre_pick_num, 0), "
                    + "Quantity = 0, Pre_pick_num = 0 WHERE Wh_code = ? AND sup_code = ? ");
            List<Serializable> executParams = new ArrayList<>();
            executParams.add(bean.getWhCode());
            executParams.add(bean.getOraCode());
            if (StringUtils.isNotEmpty(bean.getgCode())) {
                executSQL.append(" AND G_code = ? AND G_TYPE = ? ");
                executParams.add(bean.getgCode());
                executParams.add(bean.getgType());
            }
            if (StringUtils.isNotEmpty(bean.getStartTime())) {
                executSQL.append(" AND Batch_code >= ? ");
                executParams.add(bean.getOraCode() + bean.getStartTime().replace("-", ""));
            }
            if (StringUtils.isNotEmpty(bean.getEndTime())) {
                executSQL.append(" AND Batch_code <= ? ");
                executParams.add(bean.getOraCode() + bean.getEndTime().replace("-", ""));
            }
            execute(executSQL.toString(), executParams.toArray(new Serializable[executParams.size()]));
        } else {
            throw new StockException("存在锁定的库存,不允许执行锁定操作");
        }
    }

    @Override
    public void save(WmsStockLockLog bean) {
        if (null == bean.getId()) {
            bean.setState("未操作");
            bean.setNumber("KCSD" + StringUtil.getCurStringDate("yyyyMMdd") + utils.getSerialNumber(5));
            bean.setInsertUser(bean.getUpdateUser());
            saveEntity(bean);
        } else {
            WmsStockLockLog value = getEntity(bean.getId());
            if ("已操作".equals(value.getState())) {
                throw new RuntimeException("已操作的单据不能修改");
            }
            updateEntity(bean);
        }
    }

    @Override
    public void sure(Serializable id, String userName) {
        WmsStockLockLog bean = getEntity(id);
        if ("已操作".equals(bean.getState())) {
            throw new RuntimeException("已操作的单据不能重复操作");
        }
        if ("锁定库存".equals(bean.getType())) {
            lockStock(bean);
        } else {
            unLockStock(bean);
        }
        // 保存
        bean.setExector(userName);
        bean.setState("已操作");
        updateEntity(bean);
    }

    private void unLockStock(WmsStockLockLog bean) {
        // 解锁库存(如果把正常锁定的库存也给解锁了,会不会有问题)
        StringBuilder executSQL = new StringBuilder("UPDATE wms_stock SET "
                + "wms_stock.Quantity = IFNULL(wms_stock.Lock_num, 0) + IFNULL(wms_stock.Quantity, 0), Lock_num = 0 "
                + "WHERE Wh_code = ? AND Lock_num > 0 AND sup_code = ? ");
        List<Serializable> executParams = new ArrayList<>();
        executParams.add(bean.getWhCode());
        executParams.add(bean.getOraCode());
        if (StringUtils.isNotEmpty(bean.getgCode())) {
            executSQL.append(" AND G_code = ? AND G_TYPE = ? ");
            executParams.add(bean.getgCode());
            executParams.add(bean.getgType());
        }
        if (StringUtils.isNotEmpty(bean.getStartTime())) {
            executSQL.append(" AND Batch_code >= ? ");
            executParams.add(bean.getOraCode() + bean.getStartTime().replace("-", ""));
        }
        if (StringUtils.isNotEmpty(bean.getEndTime())) {
            executSQL.append(" AND Batch_code <= ? ");
            executParams.add(bean.getOraCode() + bean.getEndTime().replace("-", ""));
        }
        execute(executSQL.toString(), executParams.toArray(new Serializable[executParams.size()]));
    }
}
