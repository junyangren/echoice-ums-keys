package org.echoice.ums.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.echoice.modules.web.MsgTip;
import org.echoice.modules.web.paper.PageBean;
import org.echoice.ums.dao.EcAccssModeDao;
import org.echoice.ums.domain.EcAccssMode;
import org.echoice.ums.util.CasUmsUtil;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.view.MsgTipExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
@Controller
@RequestMapping("/console/accssMode")
public class AccssModeController extends UmsBaseController {
	private static final String PAGE_SIZE="20";
	private static final String[] EXCLUDE_FIELDS=new String[]{"ecOperators"};
	@Autowired
	private EcAccssModeDao ecAccssModeDao;

	@RequestMapping("index")
	public String index() throws Exception {
		// TODO Auto-generated method stub
		return "accssMode/index";
	}	
	
	@RequestMapping(value="searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,EcAccssMode ecAccssMode,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		PageBean pageBean=getEcAccssModeDao().findPageCondition(ecAccssMode, pageNumber, pageSize);		
		String respStr=JSONUtil.getGridFastJSON(pageBean.getTotalSize(), pageBean.getDataList(),null, EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="edit",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String edit(HttpServletRequest request,HttpServletResponse response,EcAccssMode ecAccssMode) throws Exception {
		// TODO Auto-generated method stub
		MsgTipExt msgTip=new MsgTipExt();
		EcAccssMode dbAccssMode=getEcAccssModeDao().findOne(ecAccssMode.getAccssId());
		msgTip.setData(dbAccssMode);
		String respStr=JSONUtil.toJSONString(msgTip, EXCLUDE_FIELDS);
		return respStr;
	}
	
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String save(HttpServletRequest request,HttpServletResponse response,EcAccssMode ecAccssMode) throws Exception {
		MsgTipExt msgTip=new MsgTipExt();
		if(StringUtils.isNotBlank(ecAccssMode.getAlias())) {
			ecAccssMode.setAlias(ecAccssMode.getAlias().trim());
			List<EcAccssMode> list=ecAccssModeDao.findByAlias(ecAccssMode.getAlias());
			if(list!=null&&list.size()>0){
				EcAccssMode dbRec=list.get(0);
				if(ecAccssMode.getAccssId()==null||(dbRec.getAccssId().compareTo(ecAccssMode.getAccssId())!=0)){
					msgTip.setMsg("对不起，操作标识 "+ecAccssMode.getAlias()+" 已经存在，请换一个");
					msgTip.setCode(4002);
				}
			}
		}
		if(msgTip.getCode()==0) {
			ecAccssMode.setOpTime(new Date());
			getEcAccssModeDao().save(ecAccssMode);
			msgTip.setData(ecAccssMode);
		}
		
		logger.info(CasUmsUtil.getUser(request)+" 资源操作："+ecAccssMode.getAlias()+"，"+ecAccssMode.getName());
		String respStr=JSONUtil.toJSONString(msgTip,EXCLUDE_FIELDS);
		return respStr;
	}
	
	/**
	 * 删除操作信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="del",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String del(HttpServletRequest request,HttpServletResponse response,@RequestParam String selIds) throws Exception {
		// TODO Auto-generated method stub
		MsgTip msgTip=new MsgTip();
		List<Long> idsArr=JSON.parseArray(selIds, Long.class);
		for (Long id : idsArr) { 
			getEcAccssModeDao().delete(id);
		}
		String respStr=JSONUtil.toJSONString(msgTip);
		return respStr;
	}

	
	public EcAccssModeDao getEcAccssModeDao() {
		return ecAccssModeDao;
	}
	public void setEcAccssModeDao(EcAccssModeDao ecAccssModeDao) {
		this.ecAccssModeDao = ecAccssModeDao;
	}

	
}
