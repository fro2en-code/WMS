package wms.business.biz.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wms.business.SerialNumber;
import com.ymt.utils.SerialNumberUtils;
import com.ymt.utils.SpringContex;

import its.base.service.BaseService;
import wms.business.biz.ToolBiz;
import wms.business.service.SerialNumberService;

@Service("toolBiz")
public class ToolBizImpl implements ToolBiz {

    @Resource
    private SerialNumberService serialNumberService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public String getSerialNumber(SerialNumberUtils utils, String className, int initNumber, int length) {
        SerialNumber serial = serialNumberService.getEntity(className);
        if (null == serial) {// 如果不存在
            serial = new SerialNumber();
            serial.setId(className);
            serial.setNumber(initNumber);
            serial.setStartTime(new Date());
            serialNumberService.saveEntity(serial);
        } else {
            if (utils.isExpired(serial.getStartTime())) {// 是否未过期
                serial.setNumber(serial.getNumber() + 1);
            } else {// 过期
                serial.setNumber(initNumber);
                serial.setStartTime(new Date());
            }
            serialNumberService.updateEntity(serial);
        }
        return String.format("%0" + length + "d", serial.getNumber());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void saveExcleToDatabase(List<?> list) {
        for (int i = 0, j = list.size(); i < j; i++) {
            Object object = list.get(i);
            try {
                String className = object.getClass().getSimpleName();
                className = className.substring(0, 1).toLowerCase() + className.substring(1) + "Service";
                BaseService service = SpringContex.getBean(className);
                service.saveEntity(object);
            } catch (Exception e) {
                throw new RuntimeException("第" + (i + 2) + "行 " + e.getMessage(), e);
            }
        }
    }

}
