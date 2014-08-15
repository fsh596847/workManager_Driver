package com.orwlw.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.orwlw.bean.Comm;
import com.orwlw.bean.Custom;
import com.orwlw.bean.Order;
import com.orwlw.bean.OrderDetail;

public class MyDBHelper extends SQLiteOpenHelper {

	private static MyDBHelper myDBHelper;
	private static final String DBName = "hgj_driver";

	public static MyDBHelper getInstance(Context context) {
		if (myDBHelper == null) {
			myDBHelper = new MyDBHelper(context, DBName, null, 1);
		}
		return myDBHelper;
	}

	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		/**
		 * ���ͻ���
		 */
		String sql = "CREATE TABLE IF NOT EXISTS T_CUSTOM (F_CUSTOM_CODE varchar(20),F_CUSTOM_NAME varchar(80),F_CUSTOM_PIC varchar(300),"
				+ "F_CONTACT_PERSON varchar(30),F_ADDRESS varchar(200),F_TEL varchar(15),F_LON varchar(20),F_LAT varchar(20),F_ORDER_NO varchar(20)"
				+ ")";
		db.execSQL(sql);
		System.out.println("�ͻ�����������");

		/**
		 * ����������
		 */
		sql = "CREATE TABLE IF NOT EXISTS T_ORDER (F_ORDER_NO varchar(100),F_CUSTOM_CODE varchar(20),"
				+ "F_DATE varchar(30),F_ORDER_PERSON varchar(20),F_STATUS varchar(10)"
				+ ")";
		db.execSQL(sql);
		System.out.println("��������������");

		/**
		 * ������ϸ��
		 */
		sql = "CREATE TABLE IF NOT EXISTS T_ORDER_COMM (id INTEGER primary key autoincrement,F_ORDER_NO varchar(100),F_COMM_CODE varchar(30),"
				+ "F_COMM_NAME varchar(80),F_NUM1 INTEGER,F_NUM2 INTEGER,F_COMM_STATUS INTEGER,F_SELL_ATM VARCHAR(100)"
				+ ")";
		db.execSQL(sql);
		System.out.println("������ϸ����������");

		/**
		 * ��Ʒ��
		 */
		sql = "CREATE TABLE IF NOT EXISTS T_COMM (id INTEGER primary key autoincrement,F_COMM_CODE varchar(30),F_COMM_NAME varchar(80),F_BAR_CODE VARCHAR(80),F_COMM_SPEC VARCHAR(80))";
		db.execSQL(sql);
		System.out.println("��Ʒ����������");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS T_CUSTOM";
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_ORDER";
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_ORDER_COMM";
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS T_COMM";
		db.execSQL(sql);

		onCreate(db);
	}

	/**
	 * ͨ���ͻ������ÿͻ���Ϣ
	 * 
	 * @param code
	 * @return
	 */

	public Custom getCustomByCode(String code) {

		Custom custom = new Custom();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("T_CUSTOM", null, "F_CUSTOM_CODE=?",
				new String[] { code }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			// cursor.moveToFirst();
		} else {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			db.close();
			return custom;
		}
		if (cursor.moveToNext()) {

			custom.setF_CUSTOM_CODE(cursor.getString(cursor
					.getColumnIndex("F_CUSTOM_CODE")));
			custom.setF_CUSTOM_NAME(cursor.getString(cursor
					.getColumnIndex("F_CUSTOM_NAME")));
			// custom.setF_CUSTOM_PIC(cursor.getString(cursor
			// .getColumnIndex("F_CUSTOM_PIC")));
			custom.setF_CONTACT_PERSON(cursor.getString(cursor
					.getColumnIndex("F_CONTACT_PERSON")));
			custom.setF_ADDRESS(cursor.getString(cursor
					.getColumnIndex("F_ADDRESS")));
			custom.setF_TEL(cursor.getString(cursor.getColumnIndex("F_TEL")));
			custom.setF_LAT(cursor.getString(cursor.getColumnIndex("F_LAT")));
			custom.setF_LON(cursor.getString(cursor.getColumnIndex("F_LON")));
			custom.setF_STATUS(cursor.getString(cursor
					.getColumnIndex("F_ORDER_NO")));

		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();
		return custom;
	}

	/**
	 * ���ݱ�ʶ��ȡ�ͻ��б�
	 * 
	 * @param status
	 *            �û���ʶ
	 * @return
	 */
	public List<Custom> getCustomByStatus(String status) {

		List<Custom> customs = new ArrayList<Custom>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("T_CUSTOM", null, "F_ORDER_NO=?",
				new String[] { status }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			// cursor.moveToFirst();
		} else {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			db.close();
			return customs;
		}
		while (cursor.moveToNext()) {
			Custom custom = new Custom();
			custom.setF_CUSTOM_CODE(cursor.getString(cursor
					.getColumnIndex("F_CUSTOM_CODE")));
			custom.setF_CUSTOM_NAME(cursor.getString(cursor
					.getColumnIndex("F_CUSTOM_NAME")));
			// custom.setF_CUSTOM_PIC(cursor.getString(cursor
			// .getColumnIndex("F_CUSTOM_PIC")));
			custom.setF_CONTACT_PERSON(cursor.getString(cursor
					.getColumnIndex("F_CONTACT_PERSON")));
			custom.setF_ADDRESS(cursor.getString(cursor
					.getColumnIndex("F_ADDRESS")));
			custom.setF_TEL(cursor.getString(cursor.getColumnIndex("F_TEL")));
			custom.setF_LAT(cursor.getString(cursor.getColumnIndex("F_LAT")));
			custom.setF_LON(cursor.getString(cursor.getColumnIndex("F_LON")));
			custom.setF_STATUS(cursor.getString(cursor
					.getColumnIndex("F_ORDER_NO")));

			System.out.println("helper=custom code="
					+ custom.getF_CUSTOM_CODE());
			// System.out.println("helper=custom pic=" +
			// custom.getF_CUSTOM_PIC());
			customs.add(custom);

		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();
		return customs;
	}

	/**
	 * �޸�ͼƬ��·��
	 * 
	 * @param imgUrl
	 *            �µ�ַ
	 * @param customId
	 *            �ͻ�idֵ
	 */
	public void updateCustomImg(String imgUrl, String customId) {
		SQLiteDatabase db = getWritableDatabase();

		System.out.println("����customId=" + customId);

		String sql = "update T_CUSTOM set F_CUSTOM_PIC='" + imgUrl
				+ "' where F_CUSTOM_CODE='" + customId + "'";
		db.execSQL(sql);

		db.close();
	}

	/**
	 * ������еĿͻ�
	 * 
	 * @return
	 */
	public List<Custom> getAllCustom() {

		List<Custom> customs = new ArrayList<Custom>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db
				.query("T_CUSTOM", null, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			// cursor.moveToFirst();
		} else {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			db.close();
			return customs;
		}
		while (cursor.moveToNext()) {
			Custom custom = new Custom();
			custom.setF_CUSTOM_CODE(cursor.getString(cursor
					.getColumnIndex("F_CUSTOM_CODE")));
			custom.setF_CUSTOM_NAME(cursor.getString(cursor
					.getColumnIndex("F_CUSTOM_NAME")));
			// custom.setF_CUSTOM_PIC(cursor.getString(cursor
			// .getColumnIndex("F_CUSTOM_PIC")));
			custom.setF_CONTACT_PERSON(cursor.getString(cursor
					.getColumnIndex("F_CONTACT_PERSON")));
			custom.setF_ADDRESS(cursor.getString(cursor
					.getColumnIndex("F_ADDRESS")));
			custom.setF_TEL(cursor.getString(cursor.getColumnIndex("F_TEL")));
			custom.setF_LAT(cursor.getString(cursor.getColumnIndex("F_LAT")));
			custom.setF_LON(cursor.getString(cursor.getColumnIndex("F_LON")));
			custom.setF_STATUS(cursor.getString(cursor
					.getColumnIndex("F_ORDER_NO")));

			customs.add(custom);
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();
		return customs;
	}

	/**
	 * ����һ���ͻ�
	 * 
	 * @param custom
	 * @return
	 */
	public boolean addCustom(Custom custom) {
		/*
		 * if(checkCustom(custom.getF_custom_code())){ //Ϊ���ղ����� return false; }
		 */

		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("F_CUSTOM_CODE", custom.getF_CUSTOM_CODE());
		cv.put("F_CUSTOM_NAME", custom.getF_CUSTOM_NAME());
		// cv.put("F_CUSTOM_PIC", custom.getF_CUSTOM_PIC());
		cv.put("F_CONTACT_PERSON", custom.getF_CONTACT_PERSON());
		cv.put("F_ADDRESS", custom.getF_ADDRESS());
		cv.put("F_TEL", custom.getF_TEL());
		cv.put("F_LAT", custom.getF_LAT());
		cv.put("F_LON", custom.getF_LON());
		cv.put("F_ORDER_NO", custom.getF_STATUS());
		db.insert("T_CUSTOM", null, cv);
		db.close();
		return true;
	}

	/**
	 * �鿴�ÿͻ��ǲ���������
	 * 
	 * @param code
	 * @return
	 */
	public boolean checkCustom(String code) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("", null, "=?", new String[] { code }, null,
				null, null);
		if (cursor.getCount() > 0) {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			db.close();
			return true;
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();
		return false;
	}

	/**
	 * ���ݿͻ�����ɾ���ͻ�
	 */
	public boolean deleteCustomByCode(String code) {
		SQLiteDatabase db = getWritableDatabase();
		int response = db.delete("T_CUSTOM", "F_CUSTOM_CODE=?",
				new String[] { code });
		if (response > 0) {
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * ���������Ʒ�б� ������
	 */
	public List<Comm> getAllComm() {
		List<Comm> commList = new ArrayList<Comm>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("T_COMM", null, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			// cursor.moveToFirst();
		} else {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			db.close();
			return commList;
		}
		while (cursor.moveToNext()) {
			Comm comm = new Comm();
			comm.setID(cursor.getInt(cursor.getColumnIndex("id")));
			comm.setF_COMM_CODE(cursor.getString(cursor
					.getColumnIndex("F_COMM_CODE")));
			comm.setF_COMM_NAME(cursor.getString(cursor
					.getColumnIndex("F_COMM_NAME")));

			commList.add(comm);

		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();

		return commList;
	}

	/**
	 * ���������ȡ��Ʒ ɨ��ʱʹ��
	 */
	public List<Comm> getCommby_bar(String barcode) {
		List<Comm> commList = new ArrayList<Comm>();

		SQLiteDatabase db = getReadableDatabase();
		String sql = "select * from T_COMM WHERE F_BAR_CODE= '"
				+ barcode.trim() + "'";
		Cursor cursor = db.rawQuery(sql, new String[] {});
		// Cursor cursor = db.query("T_COMM", null, "F_BAR_CODE=?",
		// new String[] { barcode }, null, null, null);

		while (cursor.moveToNext()) {
			Comm comm = new Comm();
			comm.setID(cursor.getInt(cursor.getColumnIndex("id")));
			comm.setF_COMM_CODE(cursor.getString(cursor
					.getColumnIndex("F_COMM_CODE")));
			comm.setF_COMM_NAME(cursor.getString(cursor
					.getColumnIndex("F_COMM_NAME")));
			comm.setF_BAR_CODE(cursor.getString(cursor
					.getColumnIndex("F_BAR_CODE")));

			commList.add(comm);

		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();

		return commList;
	}

	/**
	 * ������Ʒ����ģ����ѯ �ֶ�����������Ʒʱʹ��
	 * 
	 */
	public List<Comm> getAllCommByName(String name) {

		// System.out.println("commName=="+name);

		List<Comm> commList = new ArrayList<Comm>();

		// System.out.println("tiojian"+(name.equals("")));

		if (name.trim().equals("")) {
			return commList;
		}

		// System.out.println("commName==����");

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("T_COMM", null, "F_COMM_NAME like ?",
				new String[] { "%" + name + "%" }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			// cursor.moveToFirst();
		} else {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			db.close();
			return commList;
		}
		while (cursor.moveToNext()) {
			Comm comm = new Comm();
			comm.setID(cursor.getInt(cursor.getColumnIndex("id")));
			comm.setF_COMM_CODE(cursor.getString(cursor
					.getColumnIndex("F_COMM_CODE")));
			comm.setF_COMM_NAME(cursor.getString(cursor
					.getColumnIndex("F_COMM_NAME")));

			commList.add(comm);

		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();

		return commList;
	}

	/**
	 * ���ݶ����Ż�ȡ������Ʒ������ϸ
	 */
	public List<OrderDetail> getAllOrderDetailByNo(String orderNo) {
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("T_ORDER_COMM", null, "F_ORDER_NO=?",
				new String[] { orderNo }, null, null, null);

		while (cursor.moveToNext()) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setID(cursor.getInt(cursor.getColumnIndex("id")));
			orderDetail.setF_ORDER_NO(cursor.getString(cursor
					.getColumnIndex("F_ORDER_NO")));
			orderDetail.setF_COMM_CODE(cursor.getString(cursor
					.getColumnIndex("F_COMM_CODE")));
			orderDetail.setF_COMM_NAME(cursor.getString(cursor
					.getColumnIndex("F_COMM_NAME")));
			orderDetail.setF_NUM1(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("F_NUM1"))));
			orderDetail.setF_NUM2(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("F_NUM2"))));
			orderDetail.setF_COMM_STATUS(Integer.parseInt(cursor
					.getString(cursor.getColumnIndex("F_COMM_STATUS"))));
			orderDetail.setF_SELL_ATM(cursor.getString(cursor
					.getColumnIndex("F_SELL_ATM")));

			orderDetailList.add(orderDetail);

		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();

		return orderDetailList;
	}

	/**
	 * ���ݿͻ�id������еĶ���
	 * 
	 * @return
	 */
	public List<Order> getOrderByCustomId(String customId) {
		List<Order> orders = new ArrayList<Order>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("T_ORDER", null, "F_CUSTOM_CODE=?",
				new String[] { customId }, null, null, null);
		while (cursor.moveToNext()) {
			Order order = new Order();
			order.setF_CUSTOM_CODE(cursor.getString(cursor
					.getColumnIndex("F_CUSTOM_CODE")));
			order.setF_ORDER_NO(cursor.getString(cursor
					.getColumnIndex("F_ORDER_NO")));
			order.setF_DATE(cursor.getString(cursor.getColumnIndex("F_DATE")));
			order.setF_ORDER_PERSON(cursor.getString(cursor
					.getColumnIndex("F_ORDER_PERSON")));
			order.setF_STATUS(cursor.getInt(cursor.getColumnIndex("F_STATUS")));

			orders.add(order);
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		db.close();
		return orders;
	}

	/**
	 * ����һ�Ŷ���
	 * 
	 * @param order
	 */
	public void addOneOrder(Order order) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("F_ORDER_NO", order.getF_ORDER_NO());
		cv.put("F_CUSTOM_CODE", order.getF_CUSTOM_CODE());
		cv.put("F_DATE", order.getF_DATE().toString());
		cv.put("F_ORDER_PERSON", order.getF_ORDER_PERSON());
		cv.put("F_STATUS", order.getF_STATUS());

		db.insert("T_ORDER", null, cv);
		db.close();
		if (order.getCOMM_LIST() == null || order.getCOMM_LIST().size() == 0)
			return;

		// ������Ʒ��ϸ��
		for (OrderDetail orderDetail : order.getCOMM_LIST()) {
			addOneOrderDetail(orderDetail);
		}

	}

	/**
	 * ���ݶ�����ɾ������
	 * 
	 * @param orderNo
	 * @return
	 */
	public void deleteOneOrder(String custom_code) {

		SQLiteDatabase db = getWritableDatabase();

		db.execSQL("delete from T_ORDER where F_CUSTOM_CODE='" + custom_code
				+ "'");
		db.execSQL("delete from T_ORDER_COMM where F_ORDER_NO in(select F_ORDER_NO from T_ORDER where F_CUSTOM_CODE='"
				+ custom_code + "')");
		// int response = db.delete("T_ORDER", "F_ORDER_NO=?",
		// new String[] { orderNo });
		// ɾ�������µ���Ʒ��ϸ
		// deleteOrderDetial(orderNo);

		// if (response > 0) {
		// db.close();
		//
		// return true;
		// }
		// db.close();
		// return false;
	}

	/**
	 * ����һ��������ϸ��Ʒ
	 * 
	 * @param orderDetail
	 */
	public void addOneOrderDetail(OrderDetail orderDetail) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("F_ORDER_NO", orderDetail.getF_ORDER_NO());
		cv.put("F_COMM_CODE", orderDetail.getF_COMM_CODE());
		cv.put("F_COMM_NAME", orderDetail.getF_COMM_NAME());
		cv.put("F_NUM1", orderDetail.getF_NUM1());
		cv.put("F_NUM2", orderDetail.getF_NUM2());
		cv.put("F_SELL_ATM", orderDetail.getF_SELL_ATM());

		cv.put("F_COMM_STATUS", orderDetail.getF_COMM_STATUS());

		db.insert("T_ORDER_COMM", null, cv);
		db.close();
	}

	/**
	 * ͨ��������ɾ����Ʒ��ϸ
	 * 
	 * @param orderNo
	 */
	public void deleteOrderDetial(String orderNo) {

		SQLiteDatabase db = getWritableDatabase();

		db.execSQL("delete T_ORDER_COMM where F_ORDER_NO='" + orderNo + "'");
		db.close();
	}

	/**
	 * ���ݶ����ź���Ʒ�Ÿ��¶�������Ʒ����Ϣ
	 * 
	 * @param orderNo
	 * @param CommNo
	 */
	public void updateOrderDetail(OrderDetail orderDetail, String orderNo,
			String CommNo) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();

		// cv.put("F_COMM_STATUS", orderDetail.getF_COMM_STATUS());
		// cv.put("F_COMM_NAME", orderDetail.getF_COMM_NAME());
		cv.put("F_NUM1", orderDetail.getF_NUM1());
		cv.put("F_NUM2", orderDetail.getF_NUM2());
		cv.put("F_SELL_ATM", orderDetail.getF_SELL_ATM());
		db.update("T_ORDER_COMM", cv, "F_ORDER_NO=? and F_COMM_CODE=?",
				new String[] { orderNo, CommNo });

		db.close();
	}

	public void updateOrderDetail_Status(String custom_code) {

		SQLiteDatabase db = getWritableDatabase();
		// db.execSQL("update T_ORDER set F_COMM_STATUS='1'  where F_CUSTOM_CODE='"
		// + custom_code + "')");
		db.execSQL("update T_ORDER_COMM set F_COMM_STATUS='1' where F_ORDER_NO in(select F_ORDER_NO from T_ORDER where F_CUSTOM_CODE='"
				+ custom_code + "')");

		db.close();
	}

	/**
	 * ���ݶ����ź���Ʒ��ɾ����Ʒ����Ϣ
	 * 
	 * @param orderDetail
	 * @param orderNo
	 * @param CommNo
	 */
	public boolean deleteOrderDetail(String orderNo, String CommNo) {
		SQLiteDatabase db = getWritableDatabase();
		int response = db.delete("T_ORDER_COMM",
				"F_ORDER_NO=? and F_COMM_CODE=?", new String[] { orderNo,
						CommNo });
		if (response > 0) {
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	public boolean haveOrderDetial(String custom_code, String comm_code) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "select * from T_ORDER_COMM inner join T_ORDER on T_ORDER_COMM.F_ORDER_NO=T_ORDER.F_ORDER_NO WHERE T_ORDER.F_CUSTOM_CODE= '"
				+ custom_code
				+ "' and T_ORDER_COMM.F_COMM_CODE='"
				+ comm_code
				+ "'";
		Cursor cursor = db.rawQuery(sql, new String[] {});
		if (cursor.getCount() > 0) {
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/* ɾ�����пͻ� */
	public void deleteAllCustom() {
		SQLiteDatabase db = getWritableDatabase();
		int response = db.delete("T_CUSTOM", null, null);
		if (response > 0) {
			db.close();

		}
		db.close();
	}

	/* ɾ��ָ�������Ŀͻ� */
	public void DeleteCustomBy_code(String custom_code) {
		SQLiteDatabase db = getWritableDatabase();
		String[] args = { String.valueOf(custom_code) };
		int response = db.delete("T_CUSTOM", "F_CUSTOM_CODE=?", args);
		if (response > 0) {
			db.close();
		}
		db.close();
	}

	/**
	 * ���ͻ�����ͬ��������
	 * 
	 * @param customs
	 */
	public void insertAllCustom(List<Custom> customs) {

		SQLiteDatabase db = getWritableDatabase();
		for (Custom custom : customs) {
			ContentValues cv = new ContentValues();
			cv.put("F_CUSTOM_CODE", custom.getF_CUSTOM_CODE());
			cv.put("F_CUSTOM_NAME", custom.getF_CUSTOM_NAME());
			// cv.put("F_CUSTOM_PIC", custom.getF_CUSTOM_PIC());
            
			cv.put("F_CONTACT_PERSON", custom.getF_CONTACT_PERSON());
			cv.put("F_ADDRESS", custom.getF_ADDRESS());
			cv.put("F_TEL", custom.getF_TEL());
			cv.put("F_LAT", custom.getF_LAT());
			cv.put("F_LON", custom.getF_LON());
			cv.put("F_ORDER_NO", custom.getF_STATUS());
			db.insert("T_CUSTOM", null, cv);
		}
		db.close();
	}

	/* ��ն����� */
	public boolean deleteOrders() {

		SQLiteDatabase db = getWritableDatabase();
		int response = db.delete("T_ORDER", null, null);

		if (response > 0) {
			db.close();

			return true;
		}
		db.close();
		return false;
	}

	/**
	 * �����ж�������ͬ��������
	 * 
	 * @param orders
	 */
	public void insertAllOrder(List<Order> orders) {
		deleteOrders();
		SQLiteDatabase db = getWritableDatabase();
		for (Order order : orders) {
			ContentValues cv = new ContentValues();
			cv.put("F_ORDER_NO", order.getF_ORDER_NO());
			cv.put("F_CUSTOM_CODE", order.getF_CUSTOM_CODE());
			cv.put("F_DATE", order.getF_DATE());
			cv.put("F_ORDER_PERSON", order.getF_ORDER_PERSON());
			cv.put("F_STATUS", order.getF_STATUS());

			db.insert("T_ORDER", null, cv);

		}
		db.close();

	}

	/* ��ն�����ϸ�� */
	public boolean deleteAllOrderDetial() {

		SQLiteDatabase db = getWritableDatabase();
		int response = db.delete("T_ORDER_COMM", null, null);
		if (response > 0) {
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * �����ж�����ϸͬ��������
	 * 
	 * @param orderDetails
	 */
	public void inserAllOrderDetail(List<OrderDetail> orderDetails) {
		deleteAllOrderDetial();
		SQLiteDatabase db = getWritableDatabase();
		for (OrderDetail orderDetail : orderDetails) {
			ContentValues cv = new ContentValues();
			cv.put("F_ORDER_NO", orderDetail.getF_ORDER_NO());
			cv.put("F_COMM_CODE", orderDetail.getF_COMM_CODE());
			cv.put("F_COMM_NAME", orderDetail.getF_COMM_NAME());
			cv.put("F_NUM1", orderDetail.getF_NUM1());
			cv.put("F_NUM2", orderDetail.getF_NUM2());
			cv.put("F_COMM_STATUS", orderDetail.getF_COMM_STATUS());

			db.insert("T_ORDER_COMM", null, cv);
		}
		db.close();

	}

	/* �����Ʒ�� */
	public void deleteAllComm() {
		SQLiteDatabase db = getWritableDatabase();
		int response = db.delete("T_COMM", null, null);
		if (response > 0) {
			db.close();

		}
		db.close();
	}

	/* ɾ��ָ����������Ʒ */
	public void DeleteCommBy_code(String comm_code) {
		SQLiteDatabase db = getWritableDatabase();
		String[] args = { String.valueOf("c") };
		int response = db.delete("T_COMM", "F_COMM_CODE=?", args);
		if (response > 0) {
			db.close();
		}
		db.close();
	}

	/**
	 * �����洢��Ʒ������ ͬ������ʹ��
	 */
	public void addCommList(List<Comm> commList) {
		SQLiteDatabase db = getWritableDatabase();
		for (Comm item : commList) {
			ContentValues cv = new ContentValues();
			cv.put("F_COMM_CODE", item.getF_COMM_CODE());
			cv.put("F_COMM_NAME", item.getF_COMM_NAME());
			cv.put("F_BAR_CODE", item.getF_BAR_CODE());
			cv.put("F_COMM_SPEC", item.getF_COMM_SPEC());
			db.insert("T_COMM", null, cv);
		}
		db.close();
	}

}