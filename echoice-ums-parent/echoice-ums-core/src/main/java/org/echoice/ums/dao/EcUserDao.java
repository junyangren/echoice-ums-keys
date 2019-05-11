package org.echoice.ums.dao;

import java.util.List;

import org.echoice.modules.web.paper.PageBean;
import org.echoice.ums.domain.EcUser;
import org.echoice.ums.domain.EcUserExtend;
import org.echoice.ums.domain.EcUserGroup;
import org.echoice.ums.web.view.EcUserInfoView;
import org.echoice.ums.web.view.EcUserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EcUserDao extends JpaRepository<EcUser,Long> {
	public List<EcUser> findByAlias(String alias);
	public void save(EcUser ecUser,EcUserExtend ecUserExtend);
	public void saveUserGroup(EcUserGroup userGroup);
	public EcUserInfoView getById(Long userId);
	public EcUserInfoView getByAlias(String alias);
	public PageBean findPageCondition(EcUserView ecUser, int pageNo, int pageSize);
	public PageBean searchPageCondition(EcUserView ecUser, int pageNo, int pageSize);
	public EcUserGroup getUserGroup(Long groupId,Long userId);
	public void removeUserGroup(Long groupId);
	public List findGroupByUserId(Long userId);
	public List findGroupByUserAlias(String userAlias);
	public List findGroupByUserAndParenRoleAlias(String userAlias,String parentRoleAlias);
	public List<EcUser> findUserListByIds(String ids);
	public PageBean searchPageConditionSQL(EcUserView ecUser, int pageNo, int pageSize);
	
	@Modifying
	@Query(value="update EcUser set faceImg=:faceImg where alias=:alias")
	public void updateFaceImg(@Param("alias") String alias,@Param("faceImg") String faceImg);
	
	@Modifying
	@Query(value="update EcUser set fingerprint=:fingerprint where alias=:alias")
	public void updateFingerprint(@Param("alias") String alias,@Param("fingerprint") String fingerprint);
}
