package org.echoice.ums.dao;

import java.util.List;

import org.echoice.modules.web.paper.PageBean;
import org.echoice.ums.domain.EcAccssMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface EcAccssModeDao extends JpaRepository<EcAccssMode,Long>,JpaSpecificationExecutor<EcAccssMode> {
	
	@Query("select t from EcAccssMode t where t.alias=?1")
	public List<EcAccssMode> findByAlias(String alias);
	
	@Query("select t from EcAccssMode t order by t.taxis asc,t.accssId desc")
	public List<EcAccssMode> findAll();
	/**
	 * 根据条件，分页查询记录
	 * @param ecAccssMode
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageBean findPageCondition(EcAccssMode ecAccssMode, int pageNo,int pageSize);
}
