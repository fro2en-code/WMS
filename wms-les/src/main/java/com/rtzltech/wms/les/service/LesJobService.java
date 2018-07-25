package com.rtzltech.wms.les.service;

import com.wms.business.WmsLesBill;

import its.base.service.BaseService;

public interface LesJobService extends BaseService<WmsLesBill> {

	void execute();
}
