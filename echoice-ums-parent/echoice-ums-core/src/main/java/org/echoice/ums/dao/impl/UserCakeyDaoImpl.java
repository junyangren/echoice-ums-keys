package org.echoice.ums.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.persistence.BaseCommonDao;
import org.echoice.ums.domain.UserCakey;
import org.echoice.ums.web.view.UserCakeyReportView;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.annotation.Transactional;

public class UserCakeyDaoImpl extends BaseCommonDao{
	
	@Transactional
	public void persist(UserCakey userCakey){
		getEntityManager().persist(userCakey);
	}
	
	public List<UserCakeyReportView> countByStatus(){
		String sql="select t.status,count(1) total from ec_user_cakey t group by t.status order by t.status asc";
		List<UserCakeyReportView> list=getJdbcTemplate().query(sql, new BeanPropertyRowMapper<UserCakeyReportView>(UserCakeyReportView.class));
		return list;
	}
	
	public List<UserCakey> findAdvancedList(UserCakey searchForm) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		createSQLContition(searchForm,sb,params);
		List<UserCakey> list= getJdbcTemplate().query(sb.toString(),params.toArray(),new BeanPropertyRowMapper<UserCakey>(UserCakey.class));
		return list;
	}
	
	public Page<UserCakey> findAdvancedPageList(UserCakey searchForm, int pageNo, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		createSQLContition(searchForm,sb,params);
		return super.findPageSQLData(sb.toString(), new BeanPropertyRowMapper<UserCakey>(UserCakey.class),pageNo, pageSize, params.toArray());
	}
	
	private void createSQLContition(UserCakey searchForm,StringBuffer sb,List<Object> params) {
		sb.append("select ");
        sb.append("t.id");
        sb.append(",t.idcard");
        sb.append(",t.hardware_sn");
        sb.append(",t.status");
        sb.append(",t.create_time");
        sb.append(",t.create_user");
        sb.append(",t.op_time");
        sb.append(",t.op_user");
        sb.append(",t2.group_id");
        sb.append(",t2.name group_name");
        sb.append(",t3.name user_name");
		
		sb.append(" from ec_user_group t1,ec_group t2,ec_user t3,ec_user_cakey t");
		sb.append(" where t1.group_id=t2.group_id and t1.user_id=t3.user_id and t3.idcard=t.idcard");
		
		if(searchForm.getId()!=null){
			sb.append(" and t.id = ?");
			params.add(searchForm.getId());
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getIdcard())){
            sb.append(" and t.idcard = ?");
            params.add(searchForm.getIdcard()+"%");			          
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getHardwareSn())){
            sb.append(" and t.hardware_sn = ?");
            params.add(searchForm.getHardwareSn());
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getStatus())){
			sb.append(" and t.status = ?");
			params.add(searchForm.getStatus());
		}
        	  
        	  
		if(StringUtils.isNotBlank(searchForm.getCreateUser())){
			sb.append(" and t.create_user = ?");
			params.add(searchForm.getCreateUser());
		}
        	  
        	  
		if(StringUtils.isNotBlank(searchForm.getOpUser())){
			sb.append(" and t.op_user = ?");
			params.add(searchForm.getOpUser());
		}
        	  
        if(StringUtils.isNotBlank(searchForm.getAppFormStartTime())){
            sb.append(" and t.op_time >= ?");
            params.add(searchForm.getAppFormStartTime());
        }
        
        if(StringUtils.isNotBlank(searchForm.getAppFormEndTime())){
            sb.append(" and t.op_time <= ?");
            params.add(searchForm.getAppFormEndTime());
        }
        
        if(searchForm.getGroupId()!=null){
            sb.append(" and t2.group_id = ?");
            params.add(searchForm.getGroupId());
        }
        
		sb.append(" order by t.idcard asc ");
	}
	
	
	public Page<UserCakey> findPageList(UserCakey searchForm, int pageNo, int pageSize) {
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
        sb.append("t.id");
        sb.append(",t.idcard");
        sb.append(",t.hardware_sn");
        sb.append(",t.status");
        sb.append(",t.create_time");
        sb.append(",t.create_user");
        sb.append(",t.op_time");
        sb.append(",t.op_user");
		
		sb.append(" from ec_user_cakey t where 1=1");
		
		if(searchForm.getId()!=null){
			sb.append(" and t.id = ?");
			params.add(searchForm.getId());
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getIdcard())){
            sb.append(" and t.idcard like ?");
            params.add("%"+searchForm.getIdcard()+"%");			          
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getHardwareSn())){
            sb.append(" and t.hardware_sn like ?");
            params.add("%"+searchForm.getHardwareSn()+"%");			          
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getStatus())){
			sb.append(" and t.status = ?");
			params.add(searchForm.getStatus());
		}
        	  
        	  
		if(StringUtils.isNotBlank(searchForm.getCreateUser())){
			sb.append(" and t.create_user = ?");
			params.add(searchForm.getCreateUser());
		}
        	  
        	  
		if(StringUtils.isNotBlank(searchForm.getOpUser())){
			sb.append(" and t.op_user = ?");
			params.add(searchForm.getOpUser());
		}
        	  
        if(StringUtils.isNotBlank(searchForm.getAppFormStartTime())){
            sb.append(" and t.op_time >= ?");
            params.add(searchForm.getAppFormStartTime());
        }
        
        if(StringUtils.isNotBlank(searchForm.getAppFormEndTime())){
            sb.append(" and t.op_time <= ?");
            params.add(searchForm.getAppFormEndTime());
        }    	
		sb.append(" order by t.id desc ");
		return super.findPageSQLData(sb.toString(), new BeanPropertyRowMapper<UserCakey>(UserCakey.class),pageNo, pageSize, params.toArray());
	}
}
