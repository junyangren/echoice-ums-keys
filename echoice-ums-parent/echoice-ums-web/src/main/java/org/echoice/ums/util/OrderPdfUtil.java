package org.echoice.ums.util;

import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.echoice.ums.domain.CakeyOrder;
import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.service.impl.UserCakeyServiceImpl;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

public class OrderPdfUtil {

	
	public static void createPdf(CakeyOrder cakeyOrder,List<CakeyOrderDetail> list,OutputStream os,byte[] signImageBytes) throws Exception {
		
		PdfFont font=PdfFontFactory.createFont("STSong-Light","UniGB-UCS2-H");
		
		PdfWriter writer = new PdfWriter(os);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);
		
		document.setFont(font);
		
		String orderInfo="确认单号："+cakeyOrder.getOrderId();
		document.add(new Paragraph(orderInfo).setBold());
		
		Table table=new Table(7);
		table.setWidth(UnitValue.createPercentValue(100));

		table.addCell(new Cell().add(new Paragraph("工号")).setBold());
		table.addCell(new Cell().add(new Paragraph("姓名")).setBold());
		table.addCell(new Cell().add(new Paragraph("科室")).setBold());
		table.addCell(new Cell().add(new Paragraph("身份证号")).setBold());
		table.addCell(new Cell().add(new Paragraph("硬件介质SN")).setBold());
		table.addCell(new Cell().add(new Paragraph("受理类型")).setBold());
		table.addCell(new Cell().add(new Paragraph("受理时间")).setBold());
		String dft="yyyy-MM-dd";
		//for(int i=0;i<30;i++) {
			for (CakeyOrderDetail cakeyOrderDetail : list) {
				table.addCell(new Cell().add(new Paragraph(cakeyOrderDetail.getJobNumber())));
				table.addCell(new Cell().add(new Paragraph(cakeyOrderDetail.getName())));
				table.addCell(new Cell().add(new Paragraph(cakeyOrderDetail.getGroupName())));
				table.addCell(new Cell().add(new Paragraph(cakeyOrderDetail.getIdcard())));
				table.addCell(new Cell().add(new Paragraph(cakeyOrderDetail.getHardwareSn())));
				table.addCell(new Cell().add(new Paragraph(UserCakeyServiceImpl.OPTS_MAP.get(cakeyOrder.getOpType()))));
				table.addCell(new Cell().add(new Paragraph(DateFormatUtils.format(cakeyOrderDetail.getCreateTime(), dft))));
			}
		//}
		document.add(table);
		document.add(new Paragraph());
		
		Paragraph signParagraph=new Paragraph("签字确认").setBold();
		//signParagraph.setFixedPosition(200, 300,UnitValue.POINT);
		document.add(signParagraph);
		if(signImageBytes!=null) {
			Image signImage=new Image(ImageDataFactory.create(signImageBytes));
			signImage.setAutoScale(true);
			//signImage.setFixedPosition(300, 0);
			document.add(signImage);
		}
		document.close();
	}
}
