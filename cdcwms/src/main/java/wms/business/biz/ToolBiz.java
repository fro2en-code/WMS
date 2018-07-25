package wms.business.biz;

import java.util.List;

import com.ymt.utils.SerialNumberUtils;

public interface ToolBiz {
    void saveExcleToDatabase(List<?> list);

    String getSerialNumber(SerialNumberUtils utils, String className, int initNumber, int length);
}
