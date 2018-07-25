package wms.userRelation.service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.userRelation.UserLabel;

public interface UserLabelService {

    /**
     * 保存、修改功能类型
     */
    ResultResp save(UserLabel la);

    PageData<UserLabel> getPageData(int page, int rows, String userLoginname);

    ResultResp del(String id);

    String getLoginNameByTid(String tid);

}
