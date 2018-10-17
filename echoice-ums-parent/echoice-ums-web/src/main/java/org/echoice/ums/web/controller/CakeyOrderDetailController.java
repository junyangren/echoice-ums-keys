package org.echoice.ums.web.controller;

import javax.servlet.ServletRequest;

import org.echoice.modules.web.json.EasyUIUtil;
import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.service.CakeyOrderDetailService;
import org.echoice.ums.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
* 描述：caKey操作工单明细 控制层
* @author test
* @date 2018/10/01
*/
@Controller
@RequestMapping(value = "/console/cakeyOrderDetail")
public class CakeyOrderDetailController{
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	private static final String PAGE_SIZE = "20";
	@Autowired
	private CakeyOrderDetailService cakeyOrderDetailService;
	

    @RequestMapping(value = "searchJSON",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String searchJSON(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "rows", defaultValue = PAGE_SIZE) int pageSize,CakeyOrderDetail searchForm,ServletRequest request) {
        Page<CakeyOrderDetail> page=cakeyOrderDetailService.getCakeyOrderDetailDao().findPageList(searchForm, pageNumber, pageSize);
        String respStr=JSONUtil.getGridFastJSON(page.getTotalElements(), page.getContent(), null, null);
        logger.debug("respStr:{}",respStr);
        return respStr;
    }
}
