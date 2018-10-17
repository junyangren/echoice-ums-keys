package org.echoice.ums.config;

import java.util.HashMap;
import java.util.Map;

import org.echoice.ums.web.filter.SecurityPmFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value={SecurityPmFilterProperties.class})
public class SecurityPmFilterConfiguer {
	
	@Bean  
    public FilterRegistrationBean securityUmsPmFillterRegistrationBean(SecurityPmFilterProperties configProps) { 
		FilterRegistrationBean securityUmsPmFillter = new FilterRegistrationBean();
		securityUmsPmFillter.setFilter(new SecurityPmFilter());
		Map<String, String> initParameters = new HashMap<String, String>();  
        initParameters.put("isSecurityFilter", configProps.getIsSecurityFilter());  
        initParameters.put("accessModeAlias", configProps.getAccessModeAlias());
        initParameters.put("urlTag", configProps.getUrlTag());
        initParameters.put("userSecurityObjectsSessionName", configProps.getUserSecurityObjectsSessionName());
        initParameters.put("filterActions", configProps.getFilterActions());
		
		securityUmsPmFillter.setInitParameters(initParameters);
		securityUmsPmFillter.setOrder(configProps.getOrder());  
        securityUmsPmFillter.setUrlPatterns(configProps.getUrlPatterns());
		return securityUmsPmFillter;
	}
}
