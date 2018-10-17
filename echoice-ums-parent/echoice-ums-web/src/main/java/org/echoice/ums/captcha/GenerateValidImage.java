package org.echoice.ums.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;


public final class GenerateValidImage {
	
	private static int imageType = BufferedImage.TYPE_INT_BGR;	//验证码图片类型
	private static int width = 120;			//验证码宽度
	private static int height = 40;			//验证码高度
	private static int characterSize = 4;		//验证码个数
	private static int disturbSize = 40;		//干扰线条的个数
	private static String key = "123456789abcdefghijkmnpqrstuvwxyz"; //验证码关键字
	private static String fontFamily = "Arial Bold";	//验证码的字体
	private static int fontStyle = Font.BOLD;			//验证码样式
	private static boolean isItalic = false;	//验证码是否显示斜体
	public final static int TEXT_SIZE_MIN=24;
	
	public static String getImage(OutputStream out){
		return getImage(GenerateValidImage.width,GenerateValidImage.height,GenerateValidImage.characterSize,GenerateValidImage.isItalic,GenerateValidImage.disturbSize,out);
	}
	
	public static String getImage(int width,int height,int disturbSize,OutputStream out){
		return getImage(width,height,GenerateValidImage.characterSize,GenerateValidImage.isItalic,disturbSize,out);
	}
	
	public static String getImage(int width,int height,int characterSize,boolean isItalic,int disturbSize,OutputStream out)
	{
		BufferedImage image = new BufferedImage(width, height, imageType);
		//使用Graphics的扩展类Graphics2D可以实现一些特殊的效果
		Graphics2D graphics = (Graphics2D) image.getGraphics(); 
		//设置背景色
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		//设置边框颜色
		//graphics.setColor(Color.GRAY);
		//graphics.drawRect(0, 0, width-1, height-1);
		Random r = new Random();
		int keyLength = key.length();
		String validStr = "";
		int textSize=16;
		int str_x=0;
		int str_y=0;
		for(int i=0;i<characterSize;i++)
		{
			graphics.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255),r.nextInt(55)+200));
			char c = key.charAt(r.nextInt(keyLength));
			validStr += c;
			textSize=height*3/4+(r.nextInt(height/4));
			if(textSize<TEXT_SIZE_MIN){
				textSize=TEXT_SIZE_MIN;
			}
			
			double theta = (r.nextInt(90)-45)*Math.PI/180;
			str_x = width/characterSize*i+5;
			//str_y = height*2/3+(r.nextInt(height/3)-height/6);
			str_y=height-(height-textSize)/2;
			str_y=textSize+(height-textSize)/2-textSize/4;
			if(isItalic){
				graphics.rotate(theta,str_x,str_y);
			}
			
			Font font = new Font(fontFamily, fontStyle, textSize);
			graphics.setFont(font);
			graphics.drawString(c+"",str_x,str_y);
			if(isItalic){
				graphics.rotate(-theta,str_x,str_y);
			}
		}
		
		//设置干扰项
		for(int i=0;i<disturbSize;i++)
		{
			graphics.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			graphics.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width), r.nextInt(height));
		}
		
		try {
			ImageIO.write(image, "JPEG", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return validStr;
	}	
	
	public static int getImageType() {
		return imageType;
	}
	/**
	 * setImageType(设置BufferImage的imageType类型)
	 * @param imageType 
	 */
	public static void setImageType(int imageType) {
		GenerateValidImage.imageType = imageType;
	}
	public static int getWidth() {
		return width;
	}
	/**
	 * setWidth(设置验证码的宽度) 
	 * @param width
	 */
	public static void setWidth(int width) {
		GenerateValidImage.width = width;
	}
	public static int getHeight() {
		return height;
	}
	/**
	 * setHeight(设置验证码的高度) 
	 * @param height
	 */
	public static void setHeight(int height) {
		GenerateValidImage.height = height;
	}
	public static int getCharacterSize() {
		return characterSize;
	}
	/**
	 * setCharacterSize(设置验证码的个数)
	 * @param characterSize
	 */
	public static void setCharacterSize(int characterSize) {
		GenerateValidImage.characterSize = characterSize;
	}
	public static int getDisturbSize() {
		return disturbSize;
	}
	/**
	 * setDisturbSize(设置干扰线条的个数)
	 * @param disturbSize
	 */
	public static void setDisturbSize(int disturbSize) {
		GenerateValidImage.disturbSize = disturbSize;
	}
	public static String getKey() {
		return key;
	}
	/**
	 * setKey(设置验证码的关键字)
	 * @param key 支持0-9 a-z A-Z 组成的字符串
	 */
	public static void setKey(String key) {
		GenerateValidImage.key = key;
	}
	public static String getFontFamily() {
		return fontFamily;
	}
	/**
	 * setFontFamily(设置验证码的字体) 
	 * @param fontFamily
	 */
	public static void setFontFamily(String fontFamily) {
		GenerateValidImage.fontFamily = fontFamily;
	}
	public static int getFontStyle() {
		return fontStyle;
	}
	/**
	 * setFontStyle(验证码的样式)
	 * @param fontStyle
	 */
	public static void setFontStyle(int fontStyle) {
		GenerateValidImage.fontStyle = fontStyle;
	}
	public static boolean isItalic() {
		return isItalic;
	}
	/**
	 * setItalic(验证码是否斜体)  
	 * @param isItalic
	 */
	public static void setItalic(boolean isItalic) {
		GenerateValidImage.isItalic = isItalic;
	}
	
	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 10; i++) {
			FileOutputStream out=new FileOutputStream(new File("d:\\testCode"+i+".png"));
			GenerateValidImage.setItalic(false);
			GenerateValidImage.setDisturbSize(0);
			GenerateValidImage.getImage(120,40,10,out);
			out.close();
		}
	}
}
