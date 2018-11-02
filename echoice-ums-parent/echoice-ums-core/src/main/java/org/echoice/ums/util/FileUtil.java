package org.echoice.ums.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class FileUtil {

	private static final AtomicInteger FILE_IDX=new AtomicInteger(0);
	public static String saveFile(String originalFilename,String saveBasePath, InputStream in) throws IOException {
		String fileSuffix=StringUtils.substringAfterLast(originalFilename, ".");
		String fold=genFold();
		String fileName=genFileName(fileSuffix);
		String filePath = saveBasePath+File.separator +fold+File.separator+fileName ;
		
		File file=new File(filePath);
		FileUtils.forceMkdirParent(file);
		OutputStream out = new FileOutputStream(filePath);
		IOUtils.copy(in, out);
		in.close();
		out.close();
		return filePath;
	}
	
	public static File genFilePath(String saveBasePath,String fileSuffix) throws IOException{
		String fold=genFold();
		String fileName=genFileName(fileSuffix);
		String filePath = saveBasePath+File.separator +fold+File.separator+fileName ;
		File file=new File(filePath);
		FileUtils.forceMkdirParent(file);
		return file;
	}
	
	private static String genFileName(String fileSuffix) {
		int fileIdx=FILE_IDX.incrementAndGet()%1000;
		String fileIdxStr=String.format("%04d", fileIdx);
		String fileName=DateFormatUtils.format(new Date(), "yyyyMMddhhmmss")+"-"+fileIdxStr +"."+fileSuffix;
		return fileName;
	}
	
	private static String genFold() {
		Date now=new Date();
		String foldPattern="yyyy"+File.separator+"MM"+File.separator+"dd";
		String fold=DateFormatUtils.format(now, foldPattern);
		return fold;
	}
	
}
