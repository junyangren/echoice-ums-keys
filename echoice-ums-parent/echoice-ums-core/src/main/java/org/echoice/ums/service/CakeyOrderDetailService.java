package org.echoice.ums.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import org.echoice.ums.dao.CakeyOrderDetailDao;
import org.echoice.ums.domain.CakeyOrderDetail;

public interface CakeyOrderDetailService {

	public Page<CakeyOrderDetail> findPageList(Map<String, Object> searchParams,int pageNumber, int pageSize);
	
	public void batchDelete(List<Long> idList);
	
	public CakeyOrderDetailDao getCakeyOrderDetailDao();
}