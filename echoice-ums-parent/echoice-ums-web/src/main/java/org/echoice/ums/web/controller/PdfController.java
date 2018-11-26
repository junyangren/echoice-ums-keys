package org.echoice.ums.web.controller;

import org.springframework.stereotype.Controller;

@Controller
public class PdfController {
	
	public String pdfView() {
		return "pdf/view.html";
	}
}
