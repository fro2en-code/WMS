package wms.business.service.impl;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.wms.business.SerialNumber;

import its.base.service.BaseServiceImpl;
import wms.business.service.SerialNumberService;

@Service("serialNumberService")
public class SerialNumberServiceImpl extends BaseServiceImpl<SerialNumber> implements SerialNumberService {
    @Override
    public Serializable saveEntity(SerialNumber t) {
        return super.saveEntity(t);
    }

    @Override
    public SerialNumber getEntity(Serializable id) {
        return super.getEntity(id);
    }

    @Override
    public void updateEntity(SerialNumber t) {
        super.updateEntity(t);
    }
}
