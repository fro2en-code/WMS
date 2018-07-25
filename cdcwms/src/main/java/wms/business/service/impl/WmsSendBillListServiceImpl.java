package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.utils.Function;
import com.wms.business.WmsSendBillList;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsSendBillListService;

@Service("wmsSendBillListService")
public class WmsSendBillListServiceImpl extends BaseServiceImpl<WmsSendBillList> implements WmsSendBillListService {

    @Override
    public PageData<WmsSendBillList> getPageDataList(int page, int rows, WmsSendBillList wmsSendBillList) {
        String baseSql = null;
        String countSql = null;
        String sql = null;
        List<Serializable> params = new ArrayList<>();
        if ("手工发货单".equals(wmsSendBillList.getType())) {
            baseSql = " FROM wms_handwork_send_list t WHERE t.wh_code = ? AND t.MAP_SHEET_NO = ?";
            countSql = "select count(*) " + baseSql;
            sql = "SELECT t.id, t.MAP_SHEET_NO number, t.PART_NO gCode, t.SUPPL_NO oraCode, null sxCardNo, t.SEND_PACKAGE_NO sendPackageNo, t.REQ_PACKAGE_NUM reqPackageNum, t.receive_QTY reqQty, t.wh_code whCode, '手工发货单' type "
                    + baseSql;
        } else if ("LES发货单".equals(wmsSendBillList.getType())) {
            baseSql = " FROM wms_les_send_list t WHERE t.wh_code = ? AND t.MAP_SHEET_NO = ?";
            countSql = "select count(*) " + baseSql;
            sql = "SELECT t.id, t.MAP_SHEET_NO number, t.PART_NO gCode, t.SUPPL_NO oraCode, t.SX_CARD_NO sxCardNo, t.SEND_PACKAGE_NO sendPackageNo, t.REQ_PACKAGE_NUM reqPackageNum, t.receive_QTY reqQty, t.wh_code whCode, '手工发货单' type "
                    + baseSql;
        }
        params.add(wmsSendBillList.getWhCode());
        params.add(wmsSendBillList.getNumber());
        return getPageDataBySql(sql, countSql, new Function<SQLQuery>() {

            @Override
            public void run(SQLQuery t) {
                t.addScalar("id", StandardBasicTypes.STRING);
                t.addScalar("number", StandardBasicTypes.STRING);
                t.addScalar("gCode", StandardBasicTypes.STRING);
                t.addScalar("oraCode", StandardBasicTypes.STRING);
                t.addScalar("sxCardNo", StandardBasicTypes.STRING);
                t.addScalar("sendPackageNo", StandardBasicTypes.STRING);
                t.addScalar("reqPackageNum", StandardBasicTypes.INTEGER);
                t.addScalar("reqQty", StandardBasicTypes.INTEGER);
                t.addScalar("whCode", StandardBasicTypes.STRING);
                t.setResultTransformer(Transformers.aliasToBean(WmsSendBillList.class));

            }
        }, page, rows, params.toArray(new Serializable[params.size()]));
    }

}
