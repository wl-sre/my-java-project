package com.hoolai.bi.report.dao;

import com.hoolai.bi.report.model.entity.UserLoginLog;
import com.hoolai.bi.report.model.entity.UserOperationLog;
import com.hoolai.dao.GenericDao;

public interface UserLoginLogDao extends GenericDao<UserLoginLog, Long> {

	int saveLoginLogs(UserLoginLog userLoginLog);

	int saveOperationLogs(UserOperationLog userOperationLog);

}
