package org.echoice.ums.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.echoice.modules.web.MsgTip;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.domain.CakeyOrder;
import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.service.CakeyOrderDetailService;
import org.echoice.ums.service.CakeyOrderService;
import org.echoice.ums.service.UserCakeyService;
import org.echoice.ums.service.impl.UserCakeyServiceImpl;
import org.echoice.ums.util.FileUtil;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.util.OrderPdfUtil;
import org.echoice.ums.web.UmsHolder;
import org.echoice.ums.web.view.MsgTipExt;
import org.echoice.ums.web.view.UserCakeyReportView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.AxisType;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import com.google.common.collect.Lists;
/**
* 描述：caKey操作工单 控制层
* @author wujy
* @date 2018/10/01
*/
@Controller
@RequestMapping(value = "/console/cakeyOrder")
public class CakeyOrderController{
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	private static final String PAGE_SIZE = "20";
	
	@Autowired
	private CakeyOrderService cakeyOrderService;
	
	@Autowired
	private CakeyOrderDetailService cakeyOrderDetailService;
	
	@Autowired
	private UserCakeyService userCakeyService;
	
	@Autowired
	private ConfigBean configBean;
	
	@RequestMapping(value="index")
	public String index(){
		return "/cakeyOrder/index";
	}

    @RequestMapping(value = "searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,CakeyOrder searchForm,ServletRequest request) {
        Page<CakeyOrder> page=cakeyOrderService.getCakeyOrderDao().findPageList(searchForm, pageNumber, pageSize);
        String respStr=JSONUtil.getGridFastJSON(page.getTotalElements(), page.getContent());
        logger.debug("respStr:{}",respStr);
        return respStr;
    }
    
    @RequestMapping(value="report")
	public String report(){
		return "/cakeyOrder/report";
	}
    
    /**
     * 统计每天Key的领取情况
     * @param pageNo
     * @param pageSize
     * @param searchForm
     * @param request
     * @return
     */
    @RequestMapping(value = "searchReportJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String searchReportJSON(@RequestParam(value = "page", defaultValue = "1") int pageNo,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,CakeyOrderDetail searchForm,ServletRequest request) {
		String groupFieldsStr=request.getParameter("groupFields");
		String groupFields[]=null;
		if(StringUtils.isNoneBlank(groupFieldsStr)){
			groupFields=StringUtils.splitByWholeSeparator(groupFieldsStr, ",");
		}else {
			groupFields=new String[] {"reportTime","status"};
		}
		
    	Page<UserCakeyReportView> page=cakeyOrderDetailService.getCakeyOrderDetailDao().searchReportPageList(searchForm, groupFields, pageNo, pageSize);
        String respStr=JSONUtil.getGridFastJSON(page.getTotalElements(), page.getContent(), null, null);
        logger.debug("respStr:{}",respStr);
        return respStr;
    }
    
    @RequestMapping(value = "findReportJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String findReportJSON(@RequestParam(value = "page", defaultValue = "1") int pageNo,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,CakeyOrderDetail searchForm,ServletRequest request) {
		String groupFieldsStr=request.getParameter("groupFields");
		String groupFields[]=null;
		if(StringUtils.isNoneBlank(groupFieldsStr)){
			groupFields=StringUtils.splitByWholeSeparator(groupFieldsStr, ",");
		}else {
			groupFields=new String[] {"reportTime","status"};
		}
		
    	Page<UserCakeyReportView> page=cakeyOrderDetailService.getCakeyOrderDetailDao().searchReportPageList(searchForm, groupFields, pageNo, pageSize);
        String respStr=JSONUtil.getGridFastJSON(page.getTotalElements(), page.getContent(), null, null);
        logger.debug("respStr:{}",respStr);
        return respStr;
    }
    
    /**
     * 首页key echart图表数据
     * @param searchForm
     * @param request
     * @return
     */
    @RequestMapping(value = "indexReportJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String indexReportJSON(CakeyOrderDetail searchForm,ServletRequest request) {
		String groupFieldsStr=request.getParameter("groupFields");
		String groupFields[]=null;
		if(StringUtils.isNoneBlank(groupFieldsStr)){
			groupFields=StringUtils.splitByWholeSeparator(groupFieldsStr, ",");
		}else {
			groupFields=new String[] {"reportTime","status"};
		}
		
		int rangeDay=7;
		
		String datePT="yyyy-MM-dd";
		Calendar ca=Calendar.getInstance();
		searchForm.setAppFormEndTime(DateFormatUtils.format(ca,datePT));
		
		ca.add(Calendar.DATE, (-rangeDay));
		searchForm.setAppFormStartTime(DateFormatUtils.format(ca,datePT));
		
    	List<UserCakeyReportView> dataList=cakeyOrderDetailService.getCakeyOrderDetailDao().findReportList(searchForm, groupFields);
    	
    	Map<String,String> optsMap=UserCakeyServiceImpl.OPTS_MAP;
    	
    	Option option=new Option();
    	
    	Title title=new Title();
    	title.setText("");
    	option.setTitle(title);;

    	//生成legend
    	Legend legend=new Legend();
    	legend.setData(Lists.newLinkedList(optsMap.values()));
    	option.setLegend(legend);
    	
    	//xAxis
    	List<Axis> xAxis=Lists.newLinkedList(); 
    	CategoryAxis oneAxis=new CategoryAxis();
    	oneAxis.setName("日期");
    	
    	List<String> xAxisData=Lists.newLinkedList();
    	for (int i=0;i<rangeDay;i++) {
    		ca.add(Calendar.DATE, 1);
    		xAxisData.add(DateFormatUtils.format(ca,datePT));
		}
    	oneAxis.setData(xAxisData);
    	oneAxis.setBoundaryGap(false);
    	xAxis.add(oneAxis);
    	option.setxAxis(xAxis);
    	
    	//yAxis
    	List<Axis> yAxis=Lists.newLinkedList();
    	CategoryAxis oneYAxis=new CategoryAxis();
    	oneYAxis.setType(AxisType.value);
    	yAxis.add(oneYAxis);
    	option.setyAxis(yAxis);
    	
    	//series
    	List<Series> series=Lists.newLinkedList();
    	
    	Series oneSeries=null;
    	List<Long> oneSeriesData=null;
    	Long curTotal=0L;
    	for (String opType : optsMap.keySet()) {
    		oneSeries=new Line();
    		oneSeries.setName(optsMap.get(opType));
    		oneSeries.setStack("数量");
    		oneSeriesData=Lists.newLinkedList();
    		for (String xAxisOne : xAxisData) {//每天的数据
    			curTotal=0L;
				for (UserCakeyReportView userCakeyReportView : dataList) {
					if(StringUtils.equalsIgnoreCase(userCakeyReportView.getReportTime(), xAxisOne)
							&&StringUtils.equalsIgnoreCase(userCakeyReportView.getStatus(),opType)) {
						curTotal=userCakeyReportView.getTotal();
						break;
					}
				}
				oneSeriesData.add(curTotal);
			}
    		oneSeries.setData(oneSeriesData);
    		series.add(oneSeries);
		}
    	option.setSeries(series);
        return JSON.toJSONString(option);
    }

	@RequestMapping(value = "uploadPdf",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public MsgTip uploadPdf(MultipartFile file,HttpServletRequest request) {
		MsgTipExt msgTip=new MsgTipExt();
		msgTip.setMsg("文件上传成功");
		String orderId=request.getParameter("orderId");
		logger.info("工单号:{}，签名PDF文件上传",orderId);
		String originalFilename = file.getOriginalFilename();
		try {
			CakeyOrder cakeyOrder= cakeyOrderService.getCakeyOrderDao().findByOrderId(orderId);
			if(cakeyOrder!=null) {
				String filePath=FileUtil.saveFile(originalFilename, this.configBean.getUploadPath(), file.getInputStream());
				cakeyOrder.setSignPdf(filePath);
				cakeyOrder.setOpTime(new Date());
				cakeyOrder.setOpUser(UmsHolder.getUserAlias());
				cakeyOrderService.getCakeyOrderDao().save(cakeyOrder);
			}else {
				msgTip.setCode(4002);
				msgTip.setMsg(String.format("工单号:%s,未找到", orderId));
			}
		} catch (IOException e) {
			logger.error("文件上传失败：",e);
			msgTip.setCode(4002);
			msgTip.setMsg("文件上传失败："+e.getMessage());
		}	
		return msgTip;
	}
	
	/**
	 * 下载未签名PDF
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "downPdf")
	public String downPdf(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String orderId=request.getParameter("orderId");
		response.setContentType("application/pdf"); //application/x-msdownload
		response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(orderId + ".pdf", "UTF-8") + "\"");
		CakeyOrder cakeyOrder= cakeyOrderService.getCakeyOrderDao().findByOrderId(orderId);
		List<CakeyOrderDetail> list=this.cakeyOrderDetailService.getCakeyOrderDetailDao().findByOrderId(orderId);
		OutputStream os = response.getOutputStream();
		OrderPdfUtil.createPdf(cakeyOrder,list,os,null);
		return null;
	}
	/**
	 * 下载签名PDF
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "downSignPdf")
	public String downSignPdf(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String orderId=request.getParameter("orderId");
		CakeyOrder cakeyOrder=this.cakeyOrderService.getCakeyOrderDao().findByOrderId(orderId);
		if(cakeyOrder!=null&&StringUtils.isNotBlank(cakeyOrder.getSignPdf())) {
			File file=new File(cakeyOrder.getSignPdf());
			if(file.exists()) {
				response.setContentType("application/pdf"); 
				response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(orderId + ".pdf", "UTF-8") + "\"");
				FileInputStream fis=new FileInputStream(file);
				IOUtils.copy(fis, response.getOutputStream());
				fis.close();
				return null;
			}	
		}
		return "exception/404.html";
	}
	
	/**
	 * 下载签名PDF
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "downSignPdf/{orderId}")
	public String downSignPdfP(@PathVariable("orderId") String orderId,HttpServletRequest request,HttpServletResponse response) throws Exception{
		CakeyOrder cakeyOrder=this.cakeyOrderService.getCakeyOrderDao().findByOrderId(orderId);
		if(cakeyOrder!=null&&StringUtils.isNotBlank(cakeyOrder.getSignPdf())) {
			File file=new File(cakeyOrder.getSignPdf());
			if(file.exists()) {
				response.setContentType("application/pdf"); 
				response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(orderId + ".pdf", "UTF-8") + "\"");
				FileInputStream fis=new FileInputStream(file);
				IOUtils.copy(fis, response.getOutputStream());
				fis.close();
				return null;
			}	
		}
		return "exception/404.html";
	}
}
