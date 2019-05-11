package org.echoice.ums.dao;

import org.echoice.ums.domain.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppInfoDao extends JpaRepository<AppInfo,Long>,JpaSpecificationExecutor<AppInfo> {
	
}
