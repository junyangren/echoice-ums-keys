package org.echoice.ums.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import org.echoice.ums.dao.UserCakeyDao;
import org.echoice.ums.domain.CakeyOrder;
import org.echoice.ums.domain.UserCakey;
import org.echoice.ums.web.view.MsgTipExt;

public interface UserCakeyService {

	public Page<UserCakey> findPageList(Map<String, Object> searchParams,int pageNumber, int pageSize);
	
	public void batchDelete(List<Long> idList);
	
	public UserCakeyDao getUserCakeyDao();
	
	/**
	 * 单个key领取、标记挂失、离职归还
	 * @param userCakey
	 * @return
	 */
	public MsgTipExt saveForOptKey(UserCakey userCakey);
	
	/**
	 * 单个用户多个key
	 * @param userId
	 * @param list
	 * @param updateStatus
	 * @return
	 */
	public MsgTipExt saveForOptKeys(Long userId,List<UserCakey> list,String updateStatus);
	
	/**
	 * 按部门领取
	 * @param userCakey
	 * @return
	 */
	public MsgTipExt saveIssueByDept(UserCakey userCakey);
	
	/**
	 * 
	 * @param list
	 * @param updateStatus
	 * @return
	 */
	public MsgTipExt saveBatchStorage(List<UserCakey> list);
	
	/**
	 * 创建领取临时工单
	 * @param userId
	 * @param list
	 * @param updateStatus
	 * @return
	 */
	public CakeyOrder createIssueByUserTmpOrder(Long userId,List<UserCakey> list,String updateStatus);
	
	/**
	 * 根据选择要领取key的用户创建领取工单
	 * @param idsStr
	 * @return
	 */
	public CakeyOrder createIssueByUserArraysTmpOrder(String idsStr);
	
	/**
	 * 创建领取临时工单
	 * @param userCakey
	 * @return
	 */
	public CakeyOrder createIssueByDeptTmpOrder(UserCakey userCakey);
	
	/**
	 * 保存领取工单
	 * @param cakeyOrder
	 */
	public void saveIssueOrder(CakeyOrder cakeyOrder);
}