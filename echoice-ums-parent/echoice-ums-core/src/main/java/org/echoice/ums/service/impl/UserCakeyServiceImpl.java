package org.echoice.ums.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.echoice.modules.persistence.DynamicSpecifications;
import org.echoice.modules.persistence.SearchFilter;
import org.echoice.ums.dao.CakeyOrderDao;
import org.echoice.ums.dao.CakeyOrderDetailDao;
import org.echoice.ums.dao.EcUserDao;
import org.echoice.ums.dao.UserCakeyDao;
import org.echoice.ums.domain.CakeyOrder;
import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.domain.EcUser;
import org.echoice.ums.domain.UserCakey;
import org.echoice.ums.service.UserCakeyService;
import org.echoice.ums.web.UmsHolder;
import org.echoice.ums.web.view.MsgTipExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* 描述：test 服务实现层
* @author test
* @date 2018/10/01
*/
@Service
public class UserCakeyServiceImpl implements UserCakeyService{
	public final static Map<String,String> OPTS_MAP=new LinkedHashMap<String,String>(4);
	static {
		OPTS_MAP.put("01", "入库");
		OPTS_MAP.put("02", "领取");
		OPTS_MAP.put("03", "标记丢失");
		OPTS_MAP.put("04", "离职归还");
	}
	private static final AtomicInteger ID_SEQ=new AtomicInteger(0);
	@Autowired
	private UserCakeyDao userCakeyDao;
	
	@Autowired
	private CakeyOrderDao cakeyOrderDao;
	
	@Autowired
	private CakeyOrderDetailDao cakeyOrderDetailDao;
	
	@Autowired
	private EcUserDao ecUserDao;
	
	
	public Page<UserCakey> findPageList(Map<String, Object> searchParams,int pageNumber, int pageSize){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, "auto");
		Specification<UserCakey> spec = buildSpecification(searchParams);
		return userCakeyDao.findAll(spec, pageRequest);
	}
	/**
	 * 批理删除test
	 * @param idList
	 */
	@Transactional
	public void batchDelete(List<Long> idList ){
		for (Long code : idList) {
			userCakeyDao.delete(code);
		}
	}
	
	public MsgTipExt saveForOptKeys(Long userId,List<UserCakey> list,String updateStatus) {
		MsgTipExt msgTip=new MsgTipExt();
		Date now=new Date();
		UserCakey dbUserCakey=null;
		
		EcUser ecUser=ecUserDao.findOne(userId);
		
		//生成工单
		long count=list.size();
		int seq=ID_SEQ.incrementAndGet()%10000;
		String orderId=DateFormatUtils.format(now, "yyyyMMddHHmmss")+String.format("%05d", seq);
		CakeyOrder cakeyOrder=new CakeyOrder();
		cakeyOrder.setOrderId(orderId);
		cakeyOrder.setOpType(updateStatus);
		cakeyOrder.setOpCount(count);
		cakeyOrder.setCreateUser(UmsHolder.getUserAlias());
		cakeyOrder.setCreateTime(now);
		cakeyOrder.setOpUser(UmsHolder.getUserAlias());
		cakeyOrder.setOpTime(now);
		
		this.cakeyOrderDao.save(cakeyOrder);
		CakeyOrderDetail cakeyOrderDetail=null;
		
		for (UserCakey oneUserCakey : list) {
			dbUserCakey=userCakeyDao.findOne(oneUserCakey.getId());
			dbUserCakey.setStatus(updateStatus);
			dbUserCakey.setOpUser(UmsHolder.getUserAlias());
			dbUserCakey.setOpTime(now);
			this.userCakeyDao.save(dbUserCakey);
			
			cakeyOrderDetail=new CakeyOrderDetail();
			cakeyOrderDetail.setOrderId(orderId);
			cakeyOrderDetail.setOpType(updateStatus);
			cakeyOrderDetail.setIdcard(dbUserCakey.getIdcard());
			cakeyOrderDetail.setHardwareSn(dbUserCakey.getHardwareSn());
			cakeyOrderDetail.setName(ecUser.getName());
			cakeyOrderDetail.setCreateUser(UmsHolder.getUserAlias());
			cakeyOrderDetail.setCreateTime(now);
			cakeyOrderDetail.setOpUser(UmsHolder.getUserAlias());
			cakeyOrderDetail.setOpTime(now);
			
			this.cakeyOrderDetailDao.save(cakeyOrderDetail);
		}
		
		msgTip.setMsg(String.format("操作成功 ，工单号：%s", cakeyOrder.getOrderId()));
		msgTip.setData(cakeyOrder);
		return msgTip;
	}
	
	public MsgTipExt saveForOptKey(UserCakey userCakey) {
		MsgTipExt msgTip=new MsgTipExt();
		Date now=new Date();
		List<UserCakey> list=this.userCakeyDao.findByIdcardAndHardwareSn(userCakey.getIdcard(), userCakey.getHardwareSn());
		if(list!=null&&list.size()>0) {
			UserCakey dbUserCakey=list.get(0);
			dbUserCakey.setStatus(userCakey.getStatus());
			dbUserCakey.setOpUser(UmsHolder.getUserAlias());
			dbUserCakey.setOpTime(now);
			this.userCakeyDao.save(dbUserCakey);
			
			int seq=ID_SEQ.incrementAndGet()%10000;
			String orderId=DateFormatUtils.format(now, "yyyyMMddHHmmss")+String.format("%05d", seq);
			CakeyOrder cakeyOrder=new CakeyOrder();
			cakeyOrder.setOrderId(orderId);
			cakeyOrder.setOpType(userCakey.getStatus());
			cakeyOrder.setOpCount(1L);
			cakeyOrder.setCreateUser(UmsHolder.getUserAlias());
			cakeyOrder.setCreateTime(now);
			cakeyOrder.setOpUser(UmsHolder.getUserAlias());
			cakeyOrder.setOpTime(now);
			
			this.cakeyOrderDao.save(cakeyOrder);
			
			CakeyOrderDetail cakeyOrderDetail=new CakeyOrderDetail();
			cakeyOrderDetail.setOrderId(orderId);
			cakeyOrderDetail.setOpType(userCakey.getStatus());
			cakeyOrderDetail.setIdcard(userCakey.getIdcard());
			cakeyOrderDetail.setHardwareSn(userCakey.getHardwareSn());
			cakeyOrderDetail.setName(userCakey.getUserName());
			cakeyOrderDetail.setCreateUser(UmsHolder.getUserAlias());
			cakeyOrderDetail.setCreateTime(now);
			cakeyOrderDetail.setOpUser(UmsHolder.getUserAlias());
			cakeyOrderDetail.setOpTime(now);
			
			this.cakeyOrderDetailDao.save(cakeyOrderDetail);
			
			msgTip.setMsg(String.format("操作成功 ，工单号：%s", cakeyOrder.getOrderId()));
			msgTip.setData(cakeyOrder);
		}else {
			msgTip.setCode(4002);
			msgTip.setMsg(String.format("[%s]不存在，更新失败", userCakey.getHardwareSn()));
		}
		return msgTip;
	}
	
	
	public MsgTipExt saveBatchStorage(List<UserCakey> list) {
		MsgTipExt msgTip=new MsgTipExt();
		Date now=new Date();
		String updateStatus="01";
		if(list!=null&&list.size()>0) {
			long count=list.size();
			int seq=ID_SEQ.incrementAndGet()%10000;
			String orderId=DateFormatUtils.format(now, "yyyyMMddHHmmss")+String.format("%05d", seq);
			CakeyOrder cakeyOrder=new CakeyOrder();
			cakeyOrder.setOrderId(orderId);
			cakeyOrder.setOpType(updateStatus);
			cakeyOrder.setOpCount(count);
			cakeyOrder.setCreateUser(UmsHolder.getUserAlias());
			cakeyOrder.setCreateTime(now);
			cakeyOrder.setOpUser(UmsHolder.getUserAlias());
			cakeyOrder.setOpTime(now);
			
			CakeyOrderDetail cakeyOrderDetail=null;
			this.cakeyOrderDao.save(cakeyOrder);
			
			for (UserCakey dbUserCakey : list) {
				dbUserCakey.setStatus(updateStatus);
				dbUserCakey.setOpUser(UmsHolder.getUserAlias());
				dbUserCakey.setOpTime(now);
				this.userCakeyDao.save(dbUserCakey);
				
				cakeyOrderDetail=new CakeyOrderDetail();
				cakeyOrderDetail.setOrderId(orderId);
				cakeyOrderDetail.setOpType(updateStatus);
				cakeyOrderDetail.setIdcard(dbUserCakey.getIdcard());
				cakeyOrderDetail.setHardwareSn(dbUserCakey.getHardwareSn());
				cakeyOrderDetail.setName(dbUserCakey.getUserName());
				cakeyOrderDetail.setCreateUser(UmsHolder.getUserAlias());
				cakeyOrderDetail.setCreateTime(now);
				cakeyOrderDetail.setOpUser(UmsHolder.getUserAlias());
				cakeyOrderDetail.setOpTime(now);
				
				this.cakeyOrderDetailDao.save(cakeyOrderDetail);
			}
			msgTip.setData(cakeyOrder);
		}else {
			msgTip.setCode(4002);
			msgTip.setMsg("excel未找到可以入库的KEY记录");
		}
		return msgTip;
	}
	
	public MsgTipExt saveIssueByDept(UserCakey userCakey) {
		MsgTipExt msgTip=new MsgTipExt();
		Date now=new Date();
		userCakey.setStatus("01");
		List<UserCakey> list=this.userCakeyDao.findAdvancedList(userCakey);
		if(list!=null&&list.size()>0) {

			String updateStatus="02";
			long count=list.size();
			int seq=ID_SEQ.incrementAndGet()%10000;
			String orderId=DateFormatUtils.format(now, "yyyyMMddHHmmss")+String.format("%05d", seq);
			CakeyOrder cakeyOrder=new CakeyOrder();
			cakeyOrder.setOrderId(orderId);
			cakeyOrder.setOpType(updateStatus);
			cakeyOrder.setOpCount(count);
			cakeyOrder.setCreateUser(UmsHolder.getUserAlias());
			cakeyOrder.setCreateTime(now);
			cakeyOrder.setOpUser(UmsHolder.getUserAlias());
			cakeyOrder.setOpTime(now);
			
			CakeyOrderDetail cakeyOrderDetail=null;
			this.cakeyOrderDao.save(cakeyOrder);
			
			for (UserCakey dbUserCakey : list) {
				dbUserCakey.setStatus(updateStatus);
				dbUserCakey.setOpUser(UmsHolder.getUserAlias());
				dbUserCakey.setOpTime(now);
				this.userCakeyDao.save(dbUserCakey);
				
				cakeyOrderDetail=new CakeyOrderDetail();
				cakeyOrderDetail.setOrderId(orderId);
				cakeyOrderDetail.setOpType(updateStatus);
				cakeyOrderDetail.setIdcard(dbUserCakey.getIdcard());
				cakeyOrderDetail.setHardwareSn(dbUserCakey.getHardwareSn());
				cakeyOrderDetail.setName(dbUserCakey.getUserName());
				cakeyOrderDetail.setCreateUser(UmsHolder.getUserAlias());
				cakeyOrderDetail.setCreateTime(now);
				cakeyOrderDetail.setOpUser(UmsHolder.getUserAlias());
				cakeyOrderDetail.setOpTime(now);
				
				this.cakeyOrderDetailDao.save(cakeyOrderDetail);
			}
			msgTip.setData(cakeyOrder);
		}else {
			msgTip.setCode(4002);
			msgTip.setMsg(String.format("[%s]未找到可领取的Key，领取失败", userCakey.getGroupName()));
		}
		return msgTip;
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}
		
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<UserCakey> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<UserCakey> spec = DynamicSpecifications.bySearchFilter(filters.values(), UserCakey.class);
		return spec;
	}
	
	/**
	 * 默认查询/get开启只读事务
	 * @return
	 */
	public UserCakeyDao getUserCakeyDao() {
		return userCakeyDao;
	}
}
