package com.orwlw.net;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orwlw.bean.Comm;
import com.orwlw.bean.Custom;
import com.orwlw.bean.Order;
import com.orwlw.bean.OrderDetail;
import com.orwlw.bean.PersonInfo;
import com.orwlw.dao.MyDBHelper;

public class ConvertToBean {

	private final static String serviceURL = "http://orserver.orwlw.com/DataBaseUnite.asmx";
	// private final static String
	// serviceURL="http://211.144.85.125:809/DataBaseUnite.asmx";
	private String nameSpace = "http://tempuri.org/";
	private SyncHelper syncHelper;
	private MyDBHelper myDBHelper;

	public ConvertToBean(Context context) {
		// TODO Auto-generated constructor stub
		syncHelper = new SyncHelper();
		myDBHelper = MyDBHelper.getInstance(context);
	}

	/**
	 * 获得所有客户的列表
	 * 
	 * @param methodName
	 *            方法名
	 * @param propertys
	 *            添加参数
	 * @return
	 */
	public List<Custom> getAllCustom(String methodName,
			HashMap<String, String> propertys) {

		String soapAction = nameSpace + methodName;

		String json = syncHelper.getServerMsg(nameSpace, methodName,
				serviceURL, soapAction, propertys);
		System.out.println("json=" + json);

		Type listType = new TypeToken<LinkedList<Custom>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<Custom> customs = gson.fromJson(json, listType);

		// 先删除本地对应的门店资料
		for (int i = 0; i < customs.size(); i++) {
			myDBHelper.DeleteCustomBy_code((customs.get(i).getF_CUSTOM_CODE()));
		}

		// 同步到本地数据库
		if (customs != null)
			myDBHelper.insertAllCustom(customs);
		return customs;
	}

	/**
	 * 解析单个客户的信息
	 * 
	 * @param methodName
	 * @param propertys
	 * @return
	 */
	public Custom getCustom(String methodName, HashMap<String, String> propertys) {
		Custom custom = new Custom();

		String soapAction = nameSpace + methodName;
		String json = syncHelper.getServerMsg(nameSpace, methodName,
				serviceURL, soapAction, propertys);

		Gson gson = new Gson();
		custom = gson.fromJson(json, Custom.class);

		return custom;
	}

	/**
	 * 从网络端获得所有的订单
	 * 
	 * @param methodName
	 * @param propertys
	 * @return
	 */
	public List<Order> getAllOrder(String methodName,
			HashMap<String, String> propertys) {
		String soapAction = nameSpace + methodName;
		String json = syncHelper.getServerMsg(nameSpace, methodName,
				serviceURL, soapAction, propertys);
		System.out.println("json=" + json);

		Type listType = new TypeToken<LinkedList<Order>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<Order> orders = gson.fromJson(json, listType);
		// 同步到本地
		if (orders != null)
			myDBHelper.insertAllOrder(orders);
		return orders;
	}

	/**
	 * 获得所有的订单明细
	 * 
	 * @param methodName
	 * @param propertys
	 * @return
	 */
	public List<OrderDetail> getAllOrderDetail(String methodName,
			HashMap<String, String> propertys) {
		String soapAction = nameSpace + methodName;
		String json = syncHelper.getServerMsg(nameSpace, methodName,
				serviceURL, soapAction, propertys);
		System.out.println("json=" + json);

		Type listType = new TypeToken<LinkedList<OrderDetail>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<OrderDetail> orderDetails = gson.fromJson(json, listType);

		// 同步到本地
		if (orderDetails != null)
			myDBHelper.inserAllOrderDetail(orderDetails);

		return orderDetails;
	}

	/***
	 * 同步所有商品到本地
	 * 
	 * @param methodName
	 *            ‘GetComms’
	 * @param propertys
	 *            ‘personno’
	 * @return
	 */
	public List<Comm> getAllComm(String methodName,
			HashMap<String, String> propertys) {
		String soapAction = nameSpace + methodName;
		String json = syncHelper.getServerMsg(nameSpace, methodName,
				serviceURL, soapAction, propertys);
		System.out.println("json=" + json);

		Type listType = new TypeToken<LinkedList<Comm>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<Comm> comms = gson.fromJson(json, listType);

		// 先删除本地商品资料
		for (int i = 0; i < comms.size(); i++) {
			myDBHelper.DeleteCommBy_code((comms.get(i).getF_COMM_CODE()));
		}

		// 后添加商品资料
		myDBHelper.addCommList(comms);

		return comms;
	}

	/**
	 * 向服务器提交新订单
	 * 
	 * @param methodName
	 * @param propertys
	 */
	public boolean saveNewOrder(String methodName,
			HashMap<String, String> propertys) {
		String soapAction = nameSpace + methodName;
		syncHelper.getServerMsg(nameSpace, methodName, serviceURL, soapAction,
				propertys);

		return true;
	}

	// 根据设备号获取人员信息
	public String getPersonInfo(String methodName,
			HashMap<String, String> propertys) {
		String soapAction = nameSpace + methodName;
		String json = syncHelper.getServerMsg(nameSpace, methodName,
				serviceURL, soapAction, propertys);

		Type listType = new TypeToken<LinkedList<PersonInfo>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<PersonInfo> ps = gson.fromJson(json, listType);
		if (ps.size() > 0) {
			return ps.get(0).F_PSNNAME;
		} else {
			return "";
		}

	}

}
