package wms.business.service;

import com.plat.common.page.PageData;
import com.wms.business.WarnRule;

import its.base.service.BaseService;

/**
 * 
 * @author zhouxianglh@gmail.com
 *
 * @since 2017.03.17
 */
public interface WarnRuleService extends BaseService<WarnRule> {

    /**
     * 分页查询
     */
    PageData<WarnRule> getPageData(int page, int rows, WarnRule warnRule);
}
