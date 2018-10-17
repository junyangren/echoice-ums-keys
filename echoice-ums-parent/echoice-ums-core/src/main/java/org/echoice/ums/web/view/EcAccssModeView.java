package org.echoice.ums.web.view;

import org.echoice.ums.domain.EcAccssMode;

import com.alibaba.fastjson.annotation.JSONField;

public class EcAccssModeView extends EcAccssMode{
	@JSONField(name="LAY_CHECKED")
	private Boolean LAY_CHECKED=false;

	public Boolean getLAY_CHECKED() {
		return LAY_CHECKED;
	}

	public void setLAY_CHECKED(Boolean lAY_CHECKED) {
		LAY_CHECKED = lAY_CHECKED;
	}
}
