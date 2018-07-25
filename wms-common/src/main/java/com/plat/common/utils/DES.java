/**
 * 
 */
package com.plat.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密、解密操作类
 *
 */
public class DES {
	private static final String TRANSFORMATION = "DES/ECB/PKCS5Padding";
	
	//DES解密
    public static String decryptDES(String decryptString,String secretKey) throws Exception {  
        byte[] byteMi = Base64Osi.decode(decryptString);  
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "DES");  
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);  
        cipher.init(Cipher.DECRYPT_MODE, key);  
        byte decryptedData[] = cipher.doFinal(byteMi);  
        return new String(decryptedData);  
    } 
	
	
	public static String encryptDES(String encryptString,String secretKey)
			throws Exception {
		SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
		return Base64Osi.encode(encryptedData);
	}
}