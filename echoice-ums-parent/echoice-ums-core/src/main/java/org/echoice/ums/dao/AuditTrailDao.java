package org.echoice.ums.dao;

import org.echoice.modules.web.paper.PageBean;
import org.echoice.ums.domain.AuditTrailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuditTrailDao extends JpaRepository<AuditTrailEntity,Long>,JpaSpecificationExecutor<AuditTrailEntity>{
	public PageBean findPageCondition(AuditTrailEntity searchForm, int pageNo,int pageSize);
}
