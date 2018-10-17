package org.echoice.ums.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;;

public class AppPropertyPreFilter implements PropertyPreFilter {
	private String[] excudeField;
	public AppPropertyPreFilter(String... excudeField){
		this.excudeField=excudeField;
	}
	
	@Override
	public boolean apply(JSONSerializer serializer, Object object, String name) {
		// TODO Auto-generated method stub
		if(excudeField!=null&&excudeField.length>0){
			for (int i = 0; i < excudeField.length; i++) {
				if(name.equalsIgnoreCase(excudeField[i])){
					return false;
				}
			}
		}
		return true;
	}

}
