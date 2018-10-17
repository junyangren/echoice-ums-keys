package org.echoice.ums.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfTest {

	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
		Document document = new Document();
		document.open();
		File file=new File("d:\\a.pdf");
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			PdfWriter.getInstance(document, outputStream);
			document.open();
			document.add(new Paragraph("Hello World"));
			document.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
