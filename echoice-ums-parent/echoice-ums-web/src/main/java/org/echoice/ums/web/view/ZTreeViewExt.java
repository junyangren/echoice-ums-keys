package org.echoice.ums.web.view;

import org.echoice.modules.web.ztree.ZTreeView;

public class ZTreeViewExt extends ZTreeView {
	private boolean chkDisabled=false;

	public boolean getChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}
	
}
