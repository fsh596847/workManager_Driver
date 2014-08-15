package com.orwlw.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 获取json数据的公共类
 * @author Administrator
 *
 */

public class SyncHelper {

	public SyncHelper() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 获得服务器端json数据
	 * @param nameSpace 调用的方法名
	 * @param methodName 将方法名和名称空间绑定在一起
	 * @param serviceURL WSDL文档URL
	 * @param soapAction 获得返回请求对象
	 * @param propertys 设置需要返回请求对象的参数
	 * @return
	 */
	public String getServerMsg(String nameSpace,String methodName,String serviceURL,
			String soapAction,HashMap<String,String> propertys){
		
		// 获得返回请求对象
		SoapObject request = new SoapObject(nameSpace, methodName);
		
		if(propertys!=null){
			Iterator iter=propertys.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry=(Map.Entry)iter.next();
				String key=(String)entry.getKey();
				String value=(String)entry.getValue();
				System.out.println("key==="+key);
				request.addProperty(key,value);
			}
			
		}
		
		// 设置soap的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// 设置是否调用的是dotNet开发的
		envelope.dotNet = true;
		
		envelope.bodyOut = request;
		HttpTransportSE hts = new HttpTransportSE(serviceURL, 15000);
		// web service请求
		try {
			hts.call(soapAction, envelope);
			Object o = envelope.getResponse();
			if(o!=null){
				System.out.println(o.toString());
				return o.toString();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

}
