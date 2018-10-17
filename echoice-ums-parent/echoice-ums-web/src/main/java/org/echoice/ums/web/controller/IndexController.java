package org.echoice.ums.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.echoice.ums.dao.UmsClientDao;
import org.echoice.ums.domain.EcObjects;
import org.echoice.ums.web.UmsHolder;
import org.echoice.ums.web.view.LaykitMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
public class IndexController {
	
	@Autowired
	private UmsClientDao umsClientDao;
	
	@GetMapping("/")
	public String index(HttpServletRequest request) {
		//request.setAttribute("userAlias", UmsHolder.getUserAlias());
		return "redirect:/console/index";
	}
	
	@GetMapping("/console/index")
	public String console(HttpServletRequest request) {
		request.setAttribute("userAlias", UmsHolder.getUserAlias());
		return "index";
	}
	
	@RequestMapping(value="/console/userMenu",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<LaykitMenu> userMenu(@RequestParam(defaultValue="console-menu") String rootMenuAlias,@RequestParam(defaultValue="view") String accessMode) {
		String userName=UmsHolder.getUserAlias();
		//取出根据节点菜单
		EcObjects rootMenu= umsClientDao.getObjectsByAlias(rootMenuAlias);
		LaykitMenu rootMenuNode=new LaykitMenu();
		rootMenuNode.setId(rootMenu.getObjId());
		rootMenuNode.setTitle(rootMenu.getName());
		rootMenuNode.setPid(-1L);
		
		//取出所有权限菜单
		List<EcObjects> allMenuList=umsClientDao.findAssignPermissionObjectList(userName, accessMode);
		buildMenuTree(allMenuList,rootMenuNode);
		return rootMenuNode.getChildren();
	}
	
	private LaykitMenu buildMenuTree(List<EcObjects> allMenuList,LaykitMenu parentMenuNode) {
		String icon=null;
		Boolean open=false;
		for (EcObjects oneMenu : allMenuList) {
			if(oneMenu.getParentId().longValue()==parentMenuNode.getId().longValue()){
				LaykitMenu tmpMenuNode=new LaykitMenu();
				String path=null;
				tmpMenuNode.setId(oneMenu.getObjId());
				tmpMenuNode.setTitle(oneMenu.getName());
				tmpMenuNode.setPid(oneMenu.getParentId());
				if(parentMenuNode.getPid()==-1L) {
					tmpMenuNode.setPid(0L);
				}
				tmpMenuNode.setOpen(false);
				tmpMenuNode.setTaxis(oneMenu.getTaxis());
				tmpMenuNode.setIcon("&#xe602;");
				if(StringUtils.isBlank(oneMenu.getNote())) {
					path="javascript:;";
				}else {
					try {
						JSONObject jsonObj=JSON.parseObject(oneMenu.getNote());
						path="#"+jsonObj.getString("url");
						icon=jsonObj.getString("icon");
						open=jsonObj.getBoolean("open");
						if(StringUtils.isNotBlank(icon)) {
							tmpMenuNode.setIcon(icon);
						}
						tmpMenuNode.setOpen(open);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
				tmpMenuNode.setPath(path);

				
				parentMenuNode.addChild(tmpMenuNode);
				
				buildMenuTree(allMenuList,tmpMenuNode);
			}
		}
		if(parentMenuNode.hasChilds()) {
			parentMenuNode.setIcon("&#xe631;");
			parentMenuNode.sortChilds();
		}
		return parentMenuNode;
	}
	
	private void addChildMenu(EcObjects oneMenu,LaykitMenu oneMenuNode,String icon) {
		if(oneMenu.getParentId().longValue()==oneMenuNode.getId().longValue()){
			LaykitMenu tmpMenuNode=new LaykitMenu();
			String path=null;
			tmpMenuNode.setId(oneMenu.getObjId());
			tmpMenuNode.setTitle(oneMenu.getName());
			tmpMenuNode.setPid(oneMenu.getParentId());
			tmpMenuNode.setOpen(true);
			tmpMenuNode.setTaxis(oneMenu.getTaxis());
			tmpMenuNode.setIcon(icon);
			if(StringUtils.isBlank(oneMenu.getNote())) {
				path="javascript:;";
			}else {
				JSONObject jsonObj=JSON.parseObject(oneMenu.getNote());
				path="#"+jsonObj.getString("url");
				jsonObj.getString("icon");
			}
			tmpMenuNode.setPath(path);
			oneMenuNode.addChild(tmpMenuNode);
		}
	}
}
