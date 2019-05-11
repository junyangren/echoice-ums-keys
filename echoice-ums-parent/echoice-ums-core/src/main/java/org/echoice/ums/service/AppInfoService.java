package org.echoice.ums.service;

import java.util.List;

import org.echoice.ums.dao.AppInfoDao;
import org.echoice.ums.domain.AppInfo;
import org.springframework.data.domain.Page;

public interface AppInfoService {
	public Page<AppInfo> findPageList(AppInfo searchForm, int pageNo, int pageSize);
	public void del(List<Long> idsArr);
	public AppInfoDao getAppInfoDao() ;
}
