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
	 * ������пͻ����б�
	 * 
	 * @param methodName
	 *            ������
	 * @param propertys
	 *            ��Ӳ���
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

		// ��ɾ�����ض�Ӧ���ŵ�����
		for (int i = 0; i < customs.size(); i++) {
			myDBHelper.DeleteCustomBy_code((customs.get(i).getF_CUSTOM_CODE()));
		}

		// ͬ�����������ݿ�
		if (customs != null)
			myDBHelper.insertAllCustom(customs);
		return customs;
	}

	/**
	 * ���������ͻ�����Ϣ
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
	 * ������˻�����еĶ���
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
		// ͬ��������
		if (orders != null)
			myDBHelper.insertAllOrder(orders);
		return orders;
	}

	/**
	 * ������еĶ�����ϸ
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

		// ͬ��������
		if (orderDetails != null)
			myDBHelper.inserAllOrderDetail(orderDetails);

		return orderDetails;
	}

	/***
	 * ͬ��������Ʒ������
	 * 
	 * @param methodName
	 *            ��GetComms��
	 * @param propertys
	 *            ��personno��
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

		// ��ɾ��������Ʒ����
		for (int i = 0; i < comms.size(); i++) {
			myDBHelper.DeleteCommBy_code((comms.get(i).getF_COMM_CODE()));
		}

		// �������Ʒ����
		myDBHelper.addCommList(comms);

		return comms;
	}

	/**
	 * ��������ύ�¶���
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

	// �����豸�Ż�ȡ��Ա��Ϣ
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
