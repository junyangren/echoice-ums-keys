package org.echoice.ums.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.persistence.BaseCommonDao;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.echoice.ums.domain.CakeyOrder;

public class CakeyOrderDaoImpl extends BaseCommonDao{
	
	public Page<CakeyOrder> findPageList(CakeyOrder searchForm, int pageNo, int pageSize) {
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
        sb.append("t.id");
        sb.append(",t.order_id");
        sb.append(",t.op_count");
        sb.append(",t.op_type");
        sb.append(",t.create_time");
        sb.append(",t.create_user");
        sb.append(",t.op_time");
        sb.append(",t.op_user");
		
		sb.append(" from ec_cakey_order t where 1=1");
		
		if(searchForm.getId()!=null){
			sb.append(" and t.id = ?");
			params.add(searchForm.getId());
		}
        	  
		if(StringUtils.isNotBlank(searchForm.getOrderId())){
            sb.append(" and t.order_id like ?");
            params.add("%"+searchForm.getOrderId()+"%");			          
		}
        	  
		if(searchForm.getOpCount()!=null){
			sb.append(" and t.op_count = ?");
			params.add(searchForm.getOpCount());
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
		return super.findPageSQLData(sb.toString(), new BeanPropertyRowMapper<CakeyOrder>(CakeyOrder.class),pageNo, pageSize, params.toArray());
	}
}
