package org.echoice.ums.dao;

import java.util.List;

import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.web.view.UserCakeyReportView;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CakeyOrderDetailDao extends JpaRepository<CakeyOrderDetail,Long>,JpaSpecificationExecutor<CakeyOrderDetail>{
	public Page<CakeyOrderDetail> findPageList(CakeyOrderDetail searchForm, int pageNo, int pageSize);
	
	@Query(value="select t from CakeyOrderDetail t where t.orderId=?1")
	public List<CakeyOrderDetail> findByOrderId(String orderId);
	
	public Page<UserCakeyReportView> searchReportPageList(CakeyOrderDetail searchForm,String[] groupFields,int pageNo,int pageSize);
	
	public List<UserCakeyReportView> findReportList(CakeyOrderDetail searchForm,String[] groupFields);
}