package wms.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.wms.business.WarnRule;

import its.base.service.BaseServiceImpl;
import wms.business.service.WarnRuleService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
@Service("warnRuleService")
public class WarnRuleServiceImpl extends BaseServiceImpl<WarnRule> implements WarnRuleService {

    /**
     * 分页
     */
    @Override
    public PageData<WarnRule> getPageData(int page, int rows, WarnRule warnRule) {
        return getPageDataByBaseHql("From WarnRule where 1=1 ", null, page, rows,
                new ArrayList<Serializable>());
    }
}
