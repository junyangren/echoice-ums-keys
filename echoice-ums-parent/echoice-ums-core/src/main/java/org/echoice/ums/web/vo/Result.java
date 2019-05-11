package org.echoice.ums.web.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Result {
	private String code;
	private String msg;
	private Map<String, Object> data=new HashMap<>();

	public Result(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public static Result fail(String msg) {
		return new Result("4002",msg);
	}
	
	public static Result success() {
		return new Result("0","操作成功");
	}
	
	public static Result success(String msg) {
		return new Result("0",msg);
	}
	
	public Result addData(String key,Object value) {
		data.put(key, value);
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean isSuccess() {
		if(StringUtils.equalsIgnoreCase("0", this.code)) {
			return true;
		}else {
			return false;
		}
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
