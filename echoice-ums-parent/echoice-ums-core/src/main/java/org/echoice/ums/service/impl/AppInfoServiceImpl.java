package org.echoice.ums.service.impl;

import java.util.List;

import org.echoice.ums.dao.AppInfoDao;
import org.echoice.ums.domain.AppInfo;
import org.echoice.ums.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppInfoServiceImpl implements AppInfoService{
	@Autowired
	private AppInfoDao appInfoDao;
	
	public Page<AppInfo> findPageList(AppInfo searchForm, int pageNo, int pageSize) {
		int pageIndex=pageNo-1;
		Pageable pageable=new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC, "id"));
		ExampleMatcher matcher = ExampleMatcher.matching()
				  .withMatcher("appName", match -> match.contains());
		Example<AppInfo> example= Example.of(searchForm,matcher);
		Page<AppInfo> page=appInfoDao.findAll(example, pageable);
		return page;
	}
	
	@Transactional
	public void del(List<Long> idsArr) {
		// TODO Auto-generated method stub
		for (Long id : idsArr) {
			this.appInfoDao.delete(id);
		}
	}	

	public AppInfoDao getAppInfoDao() {
		return appInfoDao;
	}


}
