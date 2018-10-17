package org.echoice.ums.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.persistence.BaseCommonDao;
import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.web.view.UserCakeyReportView;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

public class CakeyOrderDetailDaoImpl extends BaseCommonDao{
	
	public List<UserCakeyReportView> findReportList(CakeyOrderDetail searchForm,String[] groupFields){
		StringBuilder sql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		buildSql(sql,params,searchForm,groupFields);
		String listSql=sql.toString();
		logger.debug("listSql:{}",listSql);
		RowMapper<UserCakeyReportView> rowMapper =  new BeanPropertyRowMapper<UserCakeyReportView>(UserCakeyReportView.class);
		List<UserCakeyReportView> list=getJdbcTemplate().query(listSql, params.toArray(), rowMapper);
		return list;
	}
	
	public Page<UserCakeyReportView> searchReportPageList(CakeyOrderDetail searchForm,String[] groupFields,int pageNo,int pageSize){
		
		StringBuilder sql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		buildSql(sql,params,searchForm,groupFields);
		String listSql=sql.toString();
		String countSql="select count(1) from ("+listSql+") rtb";
		
		logger.debug("listSql:{}",listSql);
		RowMapper<UserCakeyReportView> rowMapper =  new BeanPropertyRowMapper<UserCakeyReportView>(UserCakeyReportView.class);
		Page<UserCakeyReportView> page=super.findPageSQLData(countSql,listSql, rowMapper, pageNo, pageSize, params.toArray());
		return page;
	}
	
	private void buildSql(StringBuilder sql,List<Object> params,CakeyOrderDetail searchForm,String[] groupFields) {
		sql.append("select t.reportTime,t.status,sum(t.total) total from ec_l2_cakey_view t");
		sql.append(" where 1=1");
        if(StringUtils.isNoneBlank(searchForm.getOpType())){
			sql.append(" and t.status = ?");
			params.add(searchForm.getOpType());
		}
		
        
		if(StringUtils.isNoneBlank(searchForm.getAppFormStartTime())){
			sql.append(" and t.reportTime >= ?");
			params.add(searchForm.getAppFormStartTime());
		}
		
		if(StringUtils.isNoneBlank(searchForm.getAppFormEndTime())){
			sql.append(" and t.reportTime <= ?");
			params.add(searchForm.getAppFormEndTime());
		}
		
		if(groupFields!=null&&groupFields.length>0){
			sql.append(" group by");
			int length=groupFields.length;
			for (int i = 0; i < length; i++) {
				sql.append(" t."+groupFields[i]);
				if(i!=(length-1)){
					sql.append(",");
				}
			}
		}
		
		if(groupFields!=null&&groupFields.length>0){
			sql.append(" order by");
			int length=groupFields.length;
			for (int i = 0; i < length; i++) {
				sql.append(" t."+groupFields[i]+" desc");
				if(i!=(length-1)){
					sql.append(",");
				}
			}
		}
	}
	
	public Page<CakeyOrderDetail> findPageList(CakeyOrderDetail searchForm, int pageNo, int pageSize) {
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
        sb.append("t.id");
        sb.append(",t.order_id");
        sb.append(",t.name");
        sb.append(",t.idcard");
        sb.append(",t.hardware_sn");
        sb.append(",t.op_type");
        sb.append(",t.create_time");
        sb.append(",t.create_user");
        sb.append(",t.op_time");
        sb.append(",t.op_user");
		
		sb.append(" from ec_cakey_order_detail t where 1=1");
		
		if(searchForm.getId()!=null){
			sb.append(" and t.id = ?");
			params.add(searchForm.getId());
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getOrderId())){
            sb.append(" and t.order_id = ?");
            params.add(searchForm.getOrderId());			          
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getName())){
            sb.append(" and t.name like ?");
            params.add("%"+searchForm.getName()+"%");			          
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getIdcard())){
            sb.append(" and t.idcard = ?");
            params.add(searchForm.getIdcard());			          
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getHardwareSn())){
            sb.append(" and t.hardware_sn like ?");
            params.add("%"+searchForm.getHardwareSn()+"%");			          
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getOpType())){
			sb.append(" and t.op_type = ?");
			params.add(searchForm.getOpType());
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
		return super.findPageSQLData(sb.toString(), new BeanPropertyRowMapper<CakeyOrderDetail>(CakeyOrderDetail.class),pageNo, pageSize, params.toArray());
	}
}
