package org.echoice.ums.service;

import java.util.List;
import java.util.Map;

import org.echoice.ums.dao.CakeyOrderDao;
import org.echoice.ums.domain.CakeyOrder;
import org.springframework.data.domain.Page;

public interface CakeyOrderService {

	public Page<CakeyOrder> findPageList(Map<String, Object> searchParams,int pageNumber, int pageSize);
	
	public void batchDelete(List<Long> idList);
	
	public CakeyOrderDao getCakeyOrderDao();
}