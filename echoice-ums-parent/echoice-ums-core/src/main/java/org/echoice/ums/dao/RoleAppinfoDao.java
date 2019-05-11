package org.echoice.ums.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.echoice.ums.domain.RoleAppinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleAppinfoDao extends JpaRepository<RoleAppinfo,Long>,JpaSpecificationExecutor<RoleAppinfo> {
	public List<RoleAppinfo> findByRoleId(Long roleId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from RoleAppinfo where roleId=:roleId and appId=:appId")
	public int delete(@Param(value = "roleId") Long roleId,@Param(value = "appId")Long appId);
}
