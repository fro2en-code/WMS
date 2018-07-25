package wms.warehouse.service.impl;

import java.io.Serializable;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.wms.warehouse.WmsStorag;

@Service("wmsStoragService")
public class WmsStoragServiceImpl extends StoragServiceImpl {
    @CacheEvict(value = "service.storagService.*", allEntries = true)
    @Override
    public Serializable saveEntity(WmsStorag storag) {
        return super.saveEntity(storag);
    }

}
