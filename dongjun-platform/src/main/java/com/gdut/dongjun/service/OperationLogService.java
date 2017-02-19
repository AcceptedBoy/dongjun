package com.gdut.dongjun.service;

import com.gdut.dongjun.domain.po.OperationLog;
import com.gdut.dongjun.service.base.BaseService;

public interface OperationLogService extends BaseService<OperationLog> {

	public void createNewOperationLog(String userId, int type, String switchId, String companyId);
}
