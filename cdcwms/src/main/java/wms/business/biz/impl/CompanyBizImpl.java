package wms.business.biz.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.plat.common.page.PageData;
import com.plat.common.result.ResultResp;
import com.wms.orginfo.OrgConpany;

import wms.business.biz.CompanyBiz;
import wms.orginfo.service.CompanyService;

@Service("companyBiz")
public class CompanyBizImpl implements CompanyBiz {
    @Resource
    private CompanyService companyService;

    @Override
    public List<Map<String, String>> getComboboxByConCode(String name, String whCode) {
        return companyService.getComboboxByConCode(name, whCode);
    }

    @Override
    public PageData<OrgConpany> getPageData(int page, int rows, OrgConpany conpany) {
        return companyService.getPageData(page, rows, conpany);
    }

    @Override
    public ResultResp save(OrgConpany conpany) {
        return companyService.save(conpany);
    }

    @Override
    public ResultResp del(String id) {
        return companyService.del(id);
    }

    @Override
    public OrgConpany getConpanyByConCode(String conCode) {
        return companyService.getConpanyByConCode(conCode);
    }
}
