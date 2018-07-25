package com.plat.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLParser {
	/**
	 * 解析xml，返回Map
	 * @param xmlString
	 * @return
	 */
	public static Map<String, String> xml2Map(String xmlString){
		Map<String, String> map = new HashMap<String, String>();
		try{
			Document document = DocumentHelper.parseText(xmlString);  
            Element nodeElement = document.getRootElement();  
            List node = nodeElement.elements();  
            for (Iterator it = node.iterator(); it.hasNext();) {  
                Element elm = (Element) it.next();  
                map.put(elm.getName(), elm.getText());  
                elm = null;  
            }  
            node = null;  
            nodeElement = null;  
            document = null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
}
