package org.echoice.ums.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Hex;

public class AesKey {
	/** 
	 * 使用指定的字符串生成秘钥 
	 */  
	public static String getKeyByPassHex(String password){  
		String s = Hex.encodeHexString(getKeyByPass(password)).toLowerCase();
		return s;
	}
	
	public static byte[] getKeyByPass(String password){  
	    //生成秘钥   
	    try {    
	        KeyGenerator kg = KeyGenerator.getInstance("AES");    
	        //kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256    
	        //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。  
	        kg.init(128, new SecureRandom(password.getBytes()));  
	        SecretKey sk = kg.generateKey();
	        byte[] b = sk.getEncoded();
	        return b;
	    } catch (NoSuchAlgorithmException e) {    
	        e.printStackTrace();    
	        System.out.println("没有此算法。");    
	    }
	    return null;
	}
}
