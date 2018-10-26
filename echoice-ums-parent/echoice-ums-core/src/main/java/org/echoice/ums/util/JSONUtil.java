package org.echoice.ums.util;

import java.util.ArrayList;
import java.util.List;

import org.echoice.ums.config.AppPropertyPreFilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;

public class JSONUtil {
	private final static String FIELD_TOTAL="total";
	private final static String FIELD_ROWS="rows";
	private final static String FIELD_FOOTER="footer";
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	
	public static String getGridFastJSON(Number totalSize,List bodyList,List footList,String[] excudeField){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("code", 0);
		jsonObject.put(FIELD_TOTAL, totalSize);
		if(bodyList==null){
			bodyList=new ArrayList();
		}
		jsonObject.put(FIELD_ROWS, bodyList);
		
		if(footList==null){
			footList=new ArrayList();
		}
		
		jsonObject.put(FIELD_FOOTER, footList);
		String resp=JSON.toJSONString(jsonObject,SerializeConfig.globalInstance,new SerializeFilter[]{new AppPropertyPreFilter(excudeField)},DEFAULT_DATE_PATTERN,JSON.DEFAULT_GENERATE_FEATURE);
		return resp;
	}
	
	public static String getGridFastJSON(Number totalSize,List bodyList,List footList){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("code", 0);
		jsonObject.put(FIELD_TOTAL, totalSize);
		if(bodyList==null){
			bodyList=new ArrayList();
		}
		jsonObject.put(FIELD_ROWS, bodyList);
		
		if(footList==null){
			footList=new ArrayList();
		}
		
		jsonObject.put(FIELD_FOOTER, footList);
		String resp=JSON.toJSONString(jsonObject);
		return resp;
	}
	
	public static String getGridFastJSON(Number totalSize,List bodyList){
		return getGridFastJSON(totalSize,bodyList,null);
	}
	
	public static String toJSONString(Object obj,String[] excudeField){
		String resp=JSON.toJSONString(obj,SerializeConfig.globalInstance,new SerializeFilter[]{new AppPropertyPreFilter(excudeField)},DEFAULT_DATE_PATTERN,JSON.DEFAULT_GENERATE_FEATURE);
		return resp;
	}
	
	public static String toJSONString(Object obj){
		String resp=JSON.toJSONString(obj,SerializeConfig.globalInstance,null,DEFAULT_DATE_PATTERN,JSON.DEFAULT_GENERATE_FEATURE);
		return resp;
	}
}

