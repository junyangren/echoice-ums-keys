package org.echoice.ums.dao;

import java.util.List;

import org.echoice.ums.domain.UserCakey;
import org.echoice.ums.web.view.UserCakeyReportView;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCakeyDao extends JpaRepository<UserCakey,Long>,JpaSpecificationExecutor<UserCakey>{
	public Page<UserCakey> findPageList(UserCakey searchForm, int pageNo, int pageSize);
	
	public Page<UserCakey> findAdvancedPageList(UserCakey searchForm, int pageNo, int pageSize);
	
	public List<UserCakey> findAdvancedList(UserCakey searchForm);
	
	@Query("select count(1) from UserCakey t where t.idcard=?1 and t.hardwareSn=?2")
	public Long countByIdcardAndHardwareSn(String idcard,String hardwareSn);
	
	@Query("select count(1) from UserCakey t where t.hardwareSn=?1")
	public Long countByHardwareSn(String hardwareSn);
	
	@Query("select t from UserCakey t where t.idcard=?1 and t.hardwareSn=?2")
	public List<UserCakey> findByIdcardAndHardwareSn(String idcard,String hardwareSn);
	
	@Query("update UserCakey t set t.status=?3 where t.idcard=?1 and t.hardwareSn=?2")
	@Modifying
	public Long update(String idcard,String hardwareSn,String status);
	
	//@Query(value="select new org.echoice.ums.web.view.UserCakeyReportView(select t.status,count(1)) from UserCakey t group by t.status order by t.status asc",
	//		nativeQuery=false)
	public List<UserCakeyReportView> countByStatus();
	
	public void persist(UserCakey userCakey);
}