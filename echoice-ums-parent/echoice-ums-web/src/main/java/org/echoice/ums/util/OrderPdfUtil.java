package org.echoice.ums.util;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.echoice.ums.domain.CakeyOrderDetail;
import org.echoice.ums.service.impl.UserCakeyServiceImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class OrderPdfUtil {
	public static void createPdf(String orderId,List<CakeyOrderDetail> list,OutputStream os) throws Exception {
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
        
        table.addCell(createCell("确认单号："+orderId, keyfont,Element.ALIGN_LEFT,5,false)); 
        
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
	}
	
    private static PdfPCell createCell(String value,Font font){ 
        PdfPCell cell = new PdfPCell(); 
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);  
        cell.setPhrase(new Phrase(value,font)); 
       return cell; 
   }
	
    private static PdfPCell createCell(String value,Font font,int align){ 
        PdfPCell cell = new PdfPCell(); 
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);         
        cell.setHorizontalAlignment(align);     
        cell.setPhrase(new Phrase(value,font)); 
       return cell; 
   }
	
    private static PdfPCell createCell(String value,Font font,int align,int colspan,boolean boderFlag){ 
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
