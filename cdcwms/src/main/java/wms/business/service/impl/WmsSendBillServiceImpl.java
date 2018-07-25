package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import com.plat.common.utils.Function;
import com.wms.business.WmsSendBill;

import its.base.service.BaseServiceImpl;
import wms.business.service.WmsSendBillService;

@Service("wmsSendBillService")
public class WmsSendBillServiceImpl extends BaseServiceImpl<WmsSendBill> implements WmsSendBillService {

    @Override
    public List<WmsSendBill> getDataList(WmsSendBill wmsSendBill) {
        StringBuilder sql = new StringBuilder(
                "SELECT id,number,priority,type, LAST_REC_REQURIE_TIME lastRecRequrieTime, mriCreateTime, DELIVERY_REC deliveryRec, wh_code whCode FROM (SELECT t.id, t.MAP_SHEET_NO number, IF(t.IS_EMERGE = 0, '非紧急', '紧急') priority, 'LES发货单' `type`, t.LAST_REC_REQURIE_TIME, t.mriCreateTime, t.DELIVERY_REC, t.wh_code FROM wms_les_send t WHERE t.Status = '6' UNION ALL SELECT t.id, t.MAP_SHEET_NO number, IF(t.IS_EMERGE = 0, '非紧急', '紧急') priority, '手工发货单' `type`, t.LAST_REC_REQURIE_TIME, t.mriCreateTime, t.DELIVERY_REC, t.wh_code FROM wms_handwork_send t WHERE t.Status = '6') wmsSendBill WHERE wmsSendBill.wh_code = ? ");
        List<Serializable> params = new ArrayList<>();
        params.add(wmsSendBill.getWhCode());
        if (StringUtils.isNotEmpty(wmsSendBill.getNumber())) {
            params.add("%" + wmsSendBill.getNumber() + "%");
            sql.append(" AND wmsSendBill.number LIKE ? ");
        }

        return findEntityBySQL(sql.toString(), new Function<SQLQuery>() {

            @Override
            public void run(SQLQuery t) {
                t.addScalar("id", StandardBasicTypes.STRING);
                t.addScalar("number", StandardBasicTypes.STRING);
                t.addScalar("priority", StandardBasicTypes.STRING);
                t.addScalar("type", StandardBasicTypes.STRING);
                t.addScalar("lastRecRequrieTime", StandardBasicTypes.STRING);
                t.addScalar("mriCreateTime", StandardBasicTypes.STRING);
                t.addScalar("deliveryRec", StandardBasicTypes.STRING);
                t.addScalar("whCode", StandardBasicTypes.STRING);
                t.setResultTransformer(Transformers.aliasToBean(WmsSendBill.class));
            }
        }, params.toArray(new Serializable[params.size()]));
    }

}
