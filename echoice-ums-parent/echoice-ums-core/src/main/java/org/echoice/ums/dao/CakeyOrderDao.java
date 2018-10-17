package org.echoice.ums.dao;

import org.echoice.ums.domain.CakeyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CakeyOrderDao extends JpaRepository<CakeyOrder,Long>,JpaSpecificationExecutor<CakeyOrder>{
	public Page<CakeyOrder> findPageList(CakeyOrder searchForm, int pageNo, int pageSize);
	
	@Query(value="select t from CakeyOrder t where t.orderId=?1")
	public CakeyOrder findByOrderId(String orderId);
}