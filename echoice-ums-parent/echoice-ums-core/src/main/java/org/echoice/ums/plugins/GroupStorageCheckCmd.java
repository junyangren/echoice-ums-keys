package org.echoice.ums.plugins;

import org.echoice.ums.dao.AppPluginDao;
import org.echoice.ums.plugins.bean.ResultMsg;

public class GroupStorageCheckCmd implements Command<ResultMsg, String>{
	private AppPluginDao appPluginDao;
	public ResultMsg execute(String groupIds) {
		// TODO Auto-generated method stub
		ResultMsg resultMsg=new ResultMsg();
		boolean result=appPluginDao.checkStorageForGroup(groupIds);
		if(!result){
			resultMsg.addMsg("{msg:\"删除的库点中存在库存，删除失败！！\",failure:true}");
		}
		resultMsg.setResult(result);
		return resultMsg;
	}
	public AppPluginDao getAppPluginDao() {
		return appPluginDao;
	}
	public void setAppPluginDao(AppPluginDao appPluginDao) {
		this.appPluginDao = appPluginDao;
	}

}
