package org.echoice.ums.service.impl;

import java.util.List;
import java.util.Map;

import org.echoice.modules.persistence.DynamicSpecifications;
import org.echoice.modules.persistence.SearchFilter;
import org.echoice.ums.dao.CakeyOrderDetailDao;
import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.service.CakeyOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* 描述：caKey操作工单明细 服务实现层
* @author test
* @date 2018/10/01
*/
@Service
public class CakeyOrderDetailServiceImpl implements CakeyOrderDetailService{
	@Autowired
	private CakeyOrderDetailDao cakeyOrderDetailDao;
	public Page<CakeyOrderDetail> findPageList(Map<String, Object> searchParams,int pageNumber, int pageSize){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, "auto");
		Specification<CakeyOrderDetail> spec = buildSpecification(searchParams);
		return cakeyOrderDetailDao.findAll(spec, pageRequest);
	}
	/**
	 * 批理删除caKey操作工单明细
	 * @param idList
	 */
	@Transactional
	public void batchDelete(List<Long> idList ){
		for (Long code : idList) {
			cakeyOrderDetailDao.delete(code);
		}
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
	private Specification<CakeyOrderDetail> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<CakeyOrderDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), CakeyOrderDetail.class);
		return spec;
	}
	
	/**
	 * 默认查询/get开启只读事务
	 * @return
	 */
	public CakeyOrderDetailDao getCakeyOrderDetailDao() {
		return cakeyOrderDetailDao;
	}
}
