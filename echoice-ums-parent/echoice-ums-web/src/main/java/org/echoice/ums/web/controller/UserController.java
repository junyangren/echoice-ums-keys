package org.echoice.ums.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.echoice.modules.encrypt.MD5;
import org.echoice.modules.web.MsgTip;
import org.echoice.modules.web.paper.PageBean;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.dao.EcGroupDao;
import org.echoice.ums.dao.EcUserDao;
import org.echoice.ums.dao.UmsClientDao;
import org.echoice.ums.domain.EcGroup;
import org.echoice.ums.domain.EcUser;
import org.echoice.ums.domain.EcUserExtend;
import org.echoice.ums.plugins.Command;
import org.echoice.ums.plugins.bean.ResultMsg;
import org.echoice.ums.service.AppPluginService;
import org.echoice.ums.service.UmsCommonService;
import org.echoice.ums.util.CasUmsUtil;
import org.echoice.ums.util.FileUtil;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.UmsHolder;
import org.echoice.ums.web.view.EcUserInfoView;
import org.echoice.ums.web.view.EcUserView;
import org.echoice.ums.web.view.MsgTipExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
@Controller
@RequestMapping("/console/user")
public class UserController {
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	private static final String PAGE_SIZE="20";
	private static final String[] EXCLUDE_FIELDS=new String[]{"ecUserGroups","ecUserExtends","ecUsersAssignmens"};
	@Autowired
	private EcUserDao ecUserDao;
	@Autowired
	private EcGroupDao ecGroupDao;
	@Autowired
	private UmsCommonService umsCommonService;
	@Autowired
	private AppPluginService appPluginService;
	@Autowired
	private UmsClientDao umsClientDao;
	
	@Autowired
	private ConfigBean configBean;
	
	@RequestMapping("index")
	public String index() throws Exception {
		// TODO Auto-generated method stub
		return "user/index";
	}
	
	@RequestMapping("profile")
	public String profile() throws Exception {
		// TODO Auto-generated method stub
		return "user/profile";
	}
	
	@RequestMapping("password")
	public String password() throws Exception {
		// TODO Auto-generated method stub
		return "user/password";
	}
	
	@RequestMapping(value="searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,EcUserView searchForm,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		PageBean pageBean=null;
		if(!CasUmsUtil.isAdmin(request)) {
			searchForm.setParentGroupAlias(CasUmsUtil.getUserGroup(request).getAlias());
		}
		
		if(searchForm.getGroupId()!=null&&searchForm.getGroupId()==-1L) {
			searchForm.setGroupId(null);
		}
		pageBean=ecUserDao.findPageCondition(searchForm, pageNumber, pageSize);
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String save(HttpServletRequest request,HttpServletResponse response,EcUser ecUser,EcUserExtend ecUserExtend) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt(); 
		String groupIds=request.getParameter("groupIds");
		String groupIdsArr[]=StringUtils.split(groupIds, ",");
		ecUser.setAlias(ecUser.getAlias().trim());
		ecUser.setName(ecUser.getName().trim());
		
		List<EcUser> list=ecUserDao.findByAlias(ecUser.getAlias());
		if(list!=null&&list.size()>0){
			EcUser dbUser=list.get(0);
			if(ecUser.getUserId()==null||(ecUser.getUserId().compareTo(dbUser.getUserId())!=0)){
				msgTip.setMsg("对不起，用户登入名"+ecUser.getAlias()+"已经存在，请换一个");
			}
		}
		
		if(msgTip.getCode()==0) {
			//密码加密
			String password=ecUser.getAlias()+ecUser.getPassword();
			MD5 md5=new MD5();
			String md5Password=md5.getMD5ofStr(password);
			
			if(ecUser.getUserId()==null){
				ecUser.setPassword(md5Password);
				logger.info(CasUmsUtil.getUser(request)+" 新增用户："+ecUser.getAlias()+"，"+ecUser.getName());
			}else{
				EcUser ecUser2=ecUserDao.findOne(ecUser.getUserId());
				//密码加密
				if(!(ecUser2.getPassword().equals(ecUser.getPassword()))){
					ecUser.setPassword(md5Password);
				}
				logger.info(CasUmsUtil.getUser(request)+" 修改用户："+ecUser.getAlias()+"，"+ecUser.getName());
			}
			ecUser.setOpTime(new Date());
			umsCommonService.saveUserData(ecUser, ecUserExtend, groupIdsArr);
			msgTip.setData(ecUser);
		}
		
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="modifyProfile",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String modifyProfile(HttpServletRequest request,HttpServletResponse response,EcUser ecUser) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt(); 
		List<EcUser> list=ecUserDao.findByAlias(UmsHolder.getUserAlias());
		if(list!=null&&list.size()>0){
			EcUser dbUser=list.get(0);
			dbUser.setName(ecUser.getName());
			dbUser.setAddress(ecUser.getAddress());
			dbUser.setMobile(ecUser.getMobile());
			dbUser.setTel(ecUser.getTel());
			dbUser.setDuty(ecUser.getDuty());
			dbUser.setAddress(ecUser.getAddress());
			dbUser.setJobNumber(ecUser.getJobNumber());
			dbUser.setQq(ecUser.getQq());
			dbUser.setWechat(ecUser.getWechat());
			dbUser.setEmail(ecUser.getEmail());
			dbUser.setIdcard(ecUser.getIdcard());
			this.ecUserDao.save(dbUser);
		}else {
			msgTip.setMsg("对不起，用户不存在");
		}
		
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}
	
	@RequestMapping(value="edit",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String edit(HttpServletRequest request,HttpServletResponse response,EcUser ecUser) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		EcUserInfoView ecUserInfoView=null;
		if(ecUser.getUserId()==null){
			String userName=CasUmsUtil.getUser(request);
			ecUserInfoView =ecUserDao.getByAlias(userName);
		}else{
			ecUserInfoView =ecUserDao.getById(ecUser.getUserId());
		}
		
		ecUserInfoView.setConfirmPassword(ecUserInfoView.getPassword());
		msgTip.setData(ecUserInfoView);
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="del",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String del(HttpServletRequest request,HttpServletResponse response,@RequestParam String selIds) throws Exception {
		MsgTip msgTip=new MsgTip();
		String ids=request.getParameter("selIds");
		Object idsArr[]=JSON.parseArray(ids).toArray();
		String idsStr=StringUtils.join(idsArr, ",");
		List<EcUser> list=getEcUserDao().findUserListByIds(idsStr);
		//
		List<Command<ResultMsg, String>> userFilterList=appPluginService.getUserFilterList();
		if(userFilterList!=null&&userFilterList.size()>0){
			ResultMsg resultMsg=null;
			for (Command<ResultMsg, String> cmd : userFilterList) {
				resultMsg=cmd.execute(idsStr);
				if(!resultMsg.isResult()){
					msgTip.setCode(4002);
					msgTip.setMsg(resultMsg.getMsg());
					break;
				}
			}
		}
		
		if(msgTip.getCode()==0) {
			umsCommonService.removeUsers(idsArr);
			for (EcUser ecUser : list) {
				logger.info(CasUmsUtil.getUser(request)+" 删除用户："+ecUser.getAlias()+"，"+ecUser.getName());
			}
		}
		
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}	
	
	/**
	 * 保存文件到服务器
	 * @param linkPerson
	 * @param file
	 * @param request
	 * @return 返回保存的文件路径 以及列数
	 */
	@RequestMapping(value = "fileUpload",method = RequestMethod.POST)
	@ResponseBody
	public MsgTipExt fileUpload(MultipartFile file,HttpServletRequest request) {
		MsgTipExt msgTip=new MsgTipExt();
		int coloumNum = 0;
		String originalFilename = file.getOriginalFilename();	
		String fileSuffix=StringUtils.substringAfterLast(originalFilename, ".");
		try {
			String filePath=FileUtil.saveFile(originalFilename,getConfigBean().getUploadPath(), file.getInputStream());
			// 计算出当前文件的列数
			if(fileSuffix.equals("xlsx")) {// 如果是EXCEL文件
				XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
				XSSFSheet sheet = wb.getSheetAt(0);
				coloumNum=sheet.getRow(0).getPhysicalNumberOfCells();//获得总列数
			}else if(fileSuffix.equals("xls")) {
				HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
				HSSFSheet sheet = wb.getSheetAt(0);
				coloumNum=sheet.getRow(0).getPhysicalNumberOfCells();//获得总列数
			}else {
				msgTip.setCode(4002);
				msgTip.setMsg("请上传xlsx、xls文件后缀的excel文件");
			}
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("filePath", filePath);
			map.put("fileColoumNum", coloumNum);
			msgTip.setData(map);
			
		} catch (IOException e) {
			logger.error("文件上传失败：",e);
			msgTip.setCode(4002);
			msgTip.setMsg("文件上传失败："+e.getMessage());
		}
		return msgTip;
	}
	
	@RequestMapping(value = "impFileData",method = RequestMethod.POST)
	@ResponseBody
	public String impFileData(EcUserInfoView ecUser,HttpServletRequest request) {
		MsgTipExt msgTip=new MsgTipExt();
		String filePath = request.getParameter("filePath");// 获取已上传的文件路径
		String startNum = request.getParameter("startNum");// 从第几行开始导入
		Integer count=Integer.valueOf(startNum)-1;
		
		if(!"".equals(startNum) || null != startNum) {
			count=Integer.valueOf(startNum);
		}
		
		String extString = filePath.substring(filePath.lastIndexOf("."));
		if(".xlsx".equals(extString)) {// 读取EXCEL文件
			msgTip=this.excelXlsxImp(count, filePath, ecUser);
		}else if(".xls".equals(extString)) {
			msgTip=this.excelXlsImp(count, filePath, ecUser);
		}
		
		if(msgTip.getCode()==0) {
			//进行用户部门信息校验
			List<EcUserInfoView> list=(List<EcUserInfoView>)msgTip.getData();
			Map<String, EcGroup> groupMap=new HashMap<String, EcGroup>();
			EcGroup tmpGroup=new EcGroup();
			for (EcUserInfoView ecUserInfoView : list) {
				tmpGroup=new EcGroup();
				tmpGroup.setName(ecUserInfoView.getName());
				groupMap.put(ecUserInfoView.getGroupName(), tmpGroup);
			}
			List<EcGroup> dbGroupList=null;
			for (String oneGroupName : groupMap.keySet()) {
				dbGroupList=ecGroupDao.findByName(oneGroupName);
				if(dbGroupList==null||dbGroupList.size()<=0) {
					msgTip.setCode(4002);
					msgTip.setMsg(oneGroupName+" 用户部门不存在，请先添加");
					break;
				}else {
					groupMap.get(oneGroupName).setGroupId(dbGroupList.get(0).getGroupId());
				}
			}
			if(msgTip.getCode()==0) {//判斷用戶
				umsCommonService.saveBatchUserData(list, groupMap);
			}
		}
		
		msgTip.setData(null);
		String respStr=JSON.toJSONString(msgTip);
		return respStr;
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "modifyPassword")
	@ResponseBody
	public MsgTip modifyPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		int resulCode=4002;
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			String newPassword=request.getParameter("newPassword");
			String confirmPassword=request.getParameter("confirmPassword");
			String oldPassword=request.getParameter("oldPassword");
			if(StringUtils.isNotBlank(oldPassword)&&StringUtils.isNotBlank(newPassword)&&StringUtils.isNotBlank(confirmPassword)){
				if(StringUtils.equals(newPassword, confirmPassword)){
					boolean isSucess=this.umsClientDao.updateUserPassword(UmsHolder.getUserAlias(), oldPassword, newPassword);
					if(isSucess){
						resulCode=0;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("msg", e.getMessage());
		}
		msgTip.setCode(resulCode);
		return msgTip;
	}	
	
	public MsgTipExt excelXlsxImp(Integer count,String filePath,EcUserInfoView colInfo) {
		MsgTipExt msgTip=new MsgTipExt();
		List<EcUserInfoView> list=new LinkedList<EcUserInfoView>();
		try {
			File file = new File(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell=null;
			EcUserInfoView tmp=null;
			for (int i = count;i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if (row == null) {// 当读取行为空时
					continue;// 跳过空行
				}
				
				tmp =new EcUserInfoView();
				if(StringUtils.isNotBlank(colInfo.getName())) {
					cell = row.getCell(Integer.valueOf(colInfo.getName()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setName(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"行姓名不能为空");
						break;
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getAlias())) {
					cell = row.getCell(Integer.valueOf(colInfo.getAlias()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setAlias(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"行帐号不能为空");
						break;
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getIdcard())) {
					cell = row.getCell(Integer.valueOf(colInfo.getIdcard()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setIdcard(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"行身份证不能为空");
						break;
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getMobile())) {
					cell = row.getCell(Integer.valueOf(colInfo.getMobile()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);// 设置单元格的类型 把数字当作字符串取出
						tmp.setMobile(cell.toString().trim());
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getGroupName())) {
					cell = row.getCell(Integer.valueOf(colInfo.getGroupName()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setGroupName(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"行用户部门不能为空");
						break;
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getHardwareSn())) {
					cell = row.getCell(Integer.valueOf(colInfo.getHardwareSn()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setHardwareSn(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"硬件介质SN不能为空");
						break;
					}
				}
				
				list.add(tmp);
			}
		} catch (Exception e) {
			logger.error("文件解析出错：",e);
			msgTip.setCode(4002);
			msgTip.setMsg("文件解析出错："+e.getMessage());
		}
		msgTip.setData(list);
		return msgTip;
    }
	
	public MsgTipExt excelXlsImp(Integer count, String filePath, EcUserInfoView colInfo) {
		MsgTipExt msgTip = new MsgTipExt();
		List<EcUserInfoView> list=new LinkedList<EcUserInfoView>();
		try {
			//处理ecxel2003
			File file = new File(filePath);
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row;
			HSSFCell cell = null;
			EcUserInfoView tmp=null;
			for (int i = count; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				tmp =new EcUserInfoView();
				if(StringUtils.isNotBlank(colInfo.getName())) {
					cell = row.getCell(Integer.valueOf(colInfo.getName()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setName(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"行姓名不能为空");
						break;
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getAlias())) {
					cell = row.getCell(Integer.valueOf(colInfo.getAlias()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setAlias(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"行身份证不能为空");
						break;
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getMobile())) {
					cell = row.getCell(Integer.valueOf(colInfo.getMobile()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置单元格的类型 把数字当作字符串取出
						tmp.setMobile(cell.toString().trim());
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getGroupName())) {
					cell = row.getCell(Integer.valueOf(colInfo.getGroupName()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setGroupName(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"行科室名称不能为空");
						break;
					}
				}
				
				if(StringUtils.isNotBlank(colInfo.getHardwareSn())) {
					cell = row.getCell(Integer.valueOf(colInfo.getHardwareSn()));
					if(cell!=null&&StringUtils.isNotBlank(cell.toString())) {
						tmp.setHardwareSn(cell.toString().trim());
					}else {
						msgTip.setMsg("第"+i+"硬件介质不能为空");
						break;
					}
				}
				
				list.add(tmp);
				
			}
		} catch (IOException e) {
			logger.error("文件解析出错：", e);
			msgTip.setCode(4002);
			msgTip.setMsg("文件解析出错：" + e.getMessage());
		}
		msgTip.setData(list);
		return msgTip;
	}	
	
	public EcUserDao getEcUserDao() {
		return ecUserDao;
	}
	public void setEcUserDao(EcUserDao ecUserDao) {
		this.ecUserDao = ecUserDao;
	}
	public EcGroupDao getEcGroupDao() {
		return ecGroupDao;
	}
	public void setEcGroupDao(EcGroupDao ecGroupDao) {
		this.ecGroupDao = ecGroupDao;
	}
	public UmsCommonService getUmsCommonService() {
		return umsCommonService;
	}
	public void setUmsCommonService(UmsCommonService umsCommonService) {
		this.umsCommonService = umsCommonService;
	}
	public AppPluginService getAppPluginService() {
		return appPluginService;
	}
	public void setAppPluginService(AppPluginService appPluginService) {
		this.appPluginService = appPluginService;
	}

	public ConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(ConfigBean configBean) {
		this.configBean = configBean;
	}


	
}
