package org.echoice.ums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

import net.unicon.cas.client.configuration.EnableCasClient;

@SpringBootApplication
@ImportResource(value = {"classpath:applicationContext-service.xml"})
//@EnableCasClient
@ServletComponentScan
public class UmsApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UmsApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(UmsApplication.class, args);
	}
}
