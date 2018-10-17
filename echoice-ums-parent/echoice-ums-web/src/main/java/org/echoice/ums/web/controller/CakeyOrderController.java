package org.echoice.ums.web.controller;

import java.io.BufferedOutputStream;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.echoice.modules.web.MsgTip;
import org.echoice.ums.config.ConfigBean;
import org.echoice.ums.domain.CakeyOrder;
import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.service.CakeyOrderDetailService;
import org.echoice.ums.service.CakeyOrderService;
import org.echoice.ums.service.impl.UserCakeyServiceImpl;
import org.echoice.ums.util.FileUtil;
import org.echoice.ums.util.JSONUtil;
import org.echoice.ums.web.UmsHolder;
import org.echoice.ums.web.view.MsgTipExt;
import org.echoice.ums.web.view.UserCakeyReportView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
        String respStr=JSONUtil.getGridFastJSON(page.getTotalElements(), page.getContent(), null, null);
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
	
	@RequestMapping(value = "downPdf")
	public String downPdf(CakeyOrder cakeyOrder,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String orderId=request.getParameter("orderId");
		response.setContentType("application/x-msdownload"); 
		response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(orderId + ".pdf", "UTF-8") + "\"");
		
		List<CakeyOrderDetail> list=this.cakeyOrderDetailService.getCakeyOrderDetailDao().findByOrderId(orderId);
		
		OutputStream os = response.getOutputStream();
		BufferedOutputStream osbf=new BufferedOutputStream(os);
		
		
		Document document=new Document();
		PdfWriter.getInstance(document,osbf);
        document.open(); 
        
        BaseFont bfChinese=BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font keyfont = new Font(bfChinese, 8, Font.BOLD);
        Font textfont = new Font(bfChinese, 8, Font.NORMAL);
        
        PdfPTable table = new PdfPTable(5);
        table.setTotalWidth(520); 
        table.setLockedWidth(true); 
        table.setHorizontalAlignment(Element.ALIGN_CENTER);      
        table.getDefaultCell().setBorder(1);
        
        table.addCell(createCell("工单号："+orderId, keyfont,Element.ALIGN_LEFT,5,false)); 
        
        table.addCell(createCell("姓名", keyfont, Element.ALIGN_CENTER)); 
        table.addCell(createCell("身份证号", keyfont, Element.ALIGN_CENTER)); 
        table.addCell(createCell("硬件介质SN", keyfont, Element.ALIGN_CENTER));
        table.addCell(createCell("办理类型", keyfont, Element.ALIGN_CENTER));
        table.addCell(createCell("办理时间", keyfont, Element.ALIGN_CENTER));
		String dft="yyyy-MM-dd";

		for (CakeyOrderDetail cakeyOrderDetail : list) {
            table.addCell(createCell(cakeyOrderDetail.getName(), textfont)); 
            table.addCell(createCell(cakeyOrderDetail.getIdcard(), textfont)); 
            table.addCell(createCell(cakeyOrderDetail.getHardwareSn(), textfont)); 
            table.addCell(createCell(UserCakeyServiceImpl.OPTS_MAP.get(cakeyOrderDetail.getOpType()), textfont));
            table.addCell(createCell(DateFormatUtils.format(cakeyOrderDetail.getCreateTime(), dft), textfont));
		}

        document.add(table);
        document.close();
        
        
		return null;
	}
	
    private PdfPCell createCell(String value,Font font){ 
        PdfPCell cell = new PdfPCell(); 
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);  
        cell.setPhrase(new Phrase(value,font)); 
       return cell; 
   }
	
    private PdfPCell createCell(String value,Font font,int align){ 
        PdfPCell cell = new PdfPCell(); 
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);         
        cell.setHorizontalAlignment(align);     
        cell.setPhrase(new Phrase(value,font)); 
       return cell; 
   }
	
    private PdfPCell createCell(String value,Font font,int align,int colspan,boolean boderFlag){ 
        PdfPCell cell = new PdfPCell(); 
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
        cell.setHorizontalAlignment(align);     
        cell.setColspan(colspan); 
        cell.setPhrase(new Phrase(value,font)); 
        cell.setPadding(3.0f); 
        if(!boderFlag){ 
            cell.setBorder(0); 
            cell.setPaddingTop(15.0f); 
            cell.setPaddingBottom(8.0f); 
        } 
       return cell; 
   }
   
    
}
