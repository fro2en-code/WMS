package com.plat.common.page;

import java.util.List;

public class PageData<T> extends cn.rtzltech.user.page.PageData<T> {

    /**
     *
     */
    private static final long serialVersionUID = 3400518081694507762L;

    public PageData() {
        super();
    }

    public PageData(Integer curNo, Integer itemsPerPage, Integer total, List<T> rows) {
        super(curNo, itemsPerPage, total, rows);
    }
}
