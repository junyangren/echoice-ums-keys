package org.echoice.ums.service;

import java.util.List;

import org.echoice.ums.plugins.Command;
import org.echoice.ums.plugins.bean.ResultMsg;

public class AppPluginService {
	private List<Command<ResultMsg, String>> groupFilterList;
	private List<Command<ResultMsg, String>> userFilterList;
	public List<Command<ResultMsg, String>> getGroupFilterList() {
		return groupFilterList;
	}
	public void setGroupFilterList(List<Command<ResultMsg, String>> groupFilterList) {
		this.groupFilterList = groupFilterList;
	}
	public List<Command<ResultMsg, String>> getUserFilterList() {
		return userFilterList;
	}
	public void setUserFilterList(List<Command<ResultMsg, String>> userFilterList) {
		this.userFilterList = userFilterList;
	}

}
