package org.echoice.ums.web;

import java.io.Serializable;

import org.echoice.ums.domain.EcGroup;
import org.echoice.ums.domain.EcUser;

public class UmsAppBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6075378781387296183L;
	private boolean isAdmin;
	private String userAlias;
	private EcGroup ecGroup;
	private EcUser ecUser;

	public EcGroup getEcGroup() {
		return ecGroup;
	}

	public void setEcGroup(EcGroup ecGroup) {
		this.ecGroup = ecGroup;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public EcUser getEcUser() {
		return ecUser;
	}

	public void setEcUser(EcUser ecUser) {
		this.ecUser = ecUser;
	}
}
