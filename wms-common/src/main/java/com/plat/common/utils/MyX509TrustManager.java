package com.plat.common.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;
/**
 * 
 * <p>
 * 名称: MyX509TrustManager
 * </p>
 * <p>
 * 描述: 证书信任管理器（用于https请求）
 * </p>
 * <p>
 * 版权: Copyright (c) 2014
 * </p>
 * <p>
 * 公司: 融通千向
 * </p>
 * 
 * @author MaJingLei
 * @version 1.0 update by MajingLei 2014-4-29 下午12:14:05
 */
public class MyX509TrustManager implements X509TrustManager {  
	    
    public X509Certificate[] getAcceptedIssuers() {  
        return null;  
    }

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		
	}  
}
