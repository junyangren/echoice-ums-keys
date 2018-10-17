package org.echoice.ums.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.echoice.modules.persistence.BaseCommonDao;
import org.echoice.ums.domain.EcAccssMode;
import org.echoice.ums.domain.EcObjects;
import org.echoice.ums.domain.EcOperator;
import org.springframework.transaction.annotation.Transactional;

public class EcOperatorDaoImpl extends BaseCommonDao {
	@Transactional
	public void saveBatch(List<Long> accssModeIds, List<Long> objectsIds) {
		// TODO Auto-generated method stub
		EcAccssMode ecAccssMode=null;
		EcOperator ecOperator=null;
		EcObjects ecObjects=null;
		for (Long accssId : accssModeIds) {
			ecAccssMode=new EcAccssMode();
			ecAccssMode.setAccssId(accssId);
			for (Long objId : objectsIds) {
				if(objId==-1){
					continue;
				}
				boolean isAssign=checkIsAssign(accssId,objId);
				if(!isAssign){
					ecOperator=new EcOperator();
					ecOperator.setEcAccssMode(ecAccssMode);
					ecObjects=new EcObjects();
					ecObjects.setObjId(objId);
					ecOperator.setEcObjects(ecObjects);
					
					getEntityManager().merge(ecOperator);
				}
			}
		}
	}
	
	@Transactional
	public void removeBatch(List<Long> accssModeIds, List<Long> objectsIds) {
		// TODO Auto-generated method stub
		for (Long accssId : accssModeIds) {
			for (Long objId : objectsIds) {
				remove(accssId,objId);
			}
		}
	}
	
	@Transactional
	public void remove(Long accessId,Long objId){
		String hql="delete from EcOperator t where t.ecAccssMode.accssId=? and t.ecObjects.objId=?";
		Query query=createQuery(hql, new Object[]{accessId,objId});
		query.executeUpdate();
	}
	
	public boolean checkIsAssign(Long accessId,Long objId){
		String hql="select count(*) from EcOperator t where t.ecAccssMode.accssId=? and t.ecObjects.objId=?";
		Query query=createQuery(hql, new Object[]{accessId,objId});
		List<Number> list=query.getResultList();
		Number tmp=list.get(0);
		if(tmp.intValue()>0){
			return true;
		}else{
			return false;
		}
	}
	
	public List findObjectListByAccessId(Long accessId){
		String hql="select t3 from EcOperator t inner join t.ecAccssMode t2 inner join t.ecObjects t3 where t2.accssId=?";
		List list=createQuery(hql,new Object[]{accessId}).getResultList();
		return list;
	}
	
	public List findAccessListByObjId(Long objId){
		String hql="select t2 from EcOperator t inner join t.ecAccssMode t2 inner join t.ecObjects t3 where t3.objId=?";
		List list=createQuery(hql,new Object[]{objId}).getResultList();
		return list;
	}
	
	public EcOperator getEcOperator(Long accessId,Long objId){
		String hql="select t from EcOperator t where t.ecAccssMode.accssId=? and t.ecObjects.objId=?";
		Query query=createQuery(hql, new Object[]{accessId,objId});
		List<EcOperator> list=query.getResultList();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
