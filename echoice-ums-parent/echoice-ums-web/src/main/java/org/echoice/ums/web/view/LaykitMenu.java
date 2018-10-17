package org.echoice.ums.web.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class LaykitMenu implements Comparable<LaykitMenu>{
	private Long id;
	private String title;
	private String path;
	private String icon;
	private Long pid;
	private Boolean open;
	private List<LaykitMenu> children;
	private Long taxis;
	
	public void addChild(LaykitMenu one) {
		if(this.children==null){
			this.children=new ArrayList<LaykitMenu>();
		}
		this.children.add(one);
	}

	public boolean hasChilds(){
		if(this.children!=null&&this.children.size()>0){
			return true;
		}
		return false;
	}
	
	public void sortChilds(){
		Collections.sort(this.children);
	}
	
	@Override
	public int compareTo(LaykitMenu o) {
		// TODO Auto-generated method stub
		if(this.getTaxis().longValue()<o.getTaxis().longValue()){
			return -1;
		}else{
			return 1;
		}
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public List<LaykitMenu> getChildren() {
		return children;
	}
	public void setChildren(List<LaykitMenu> children) {
		this.children = children;
	}

	public Long getTaxis() {
		return taxis;
	}

	public void setTaxis(Long taxis) {
		this.taxis = taxis;
	}
	
}
