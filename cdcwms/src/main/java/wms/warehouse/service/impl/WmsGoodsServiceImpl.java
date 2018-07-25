package wms.warehouse.service.impl;

import java.io.Serializable;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.wms.warehouse.WmsGoods;

@Service("wmsGoodsService")
public class WmsGoodsServiceImpl extends GoodsServiceImpl {
    @CacheEvict(value = "service.goodsService.*", allEntries = true)
    @Override
    public Serializable saveEntity(WmsGoods goods) {
        return super.saveEntity(goods);
    }
}
