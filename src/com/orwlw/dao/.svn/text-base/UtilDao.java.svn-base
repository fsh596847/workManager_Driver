package com.orwlw.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.format.Time;
import android.widget.ImageView;
import cn.com.utils.BitmapUtil;
import cn.com.utils.Utils;

import com.orwlw.bean.Custom;
import com.orwlw.bean.Order;
import com.orwlw.bean.OrderDetail;
import com.orwlw.bean.OrderGoods;
import com.orwlw.net.ConvertToBean;

public class UtilDao {

	private static MyDBHelper myDBHelper;

	private ConvertToBean convertToBean;

	public UtilDao(Context context) {
		// TODO Auto-generated constructor stub
		myDBHelper = MyDBHelper.getInstance(context);

		convertToBean = new ConvertToBean(context);

	}

	/**
	 * 根据用户的id值完成对订单的查询
	 * 
	 * @param customId
	 *            客户编号
	 * @return
	 */
	public List<OrderGoods> getGoodsByCustom(String customId) {
		List<OrderGoods> goodsList = new ArrayList<OrderGoods>();
		List<Order> orders = myDBHelper.getOrderByCustomId(customId);
		if (orders != null) {
			for (Order order : orders) {
				List<OrderDetail> orderDetails = myDBHelper
						.getAllOrderDetailByNo(order.getF_ORDER_NO());
				if (orderDetails != null) {
					for (OrderDetail orderDetail : orderDetails) {
						OrderGoods goods = new OrderGoods();
						goods.setF_COMM_CODE(orderDetail.getF_COMM_CODE());
						goods.setF_COMM_NAME(orderDetail.getF_COMM_NAME());
						goods.setF_DATE(order.getF_DATE());
						goods.setF_NUM1(orderDetail.getF_NUM1());
						goods.setF_NUM2(orderDetail.getF_NUM2());
						goods.setF_ORDER_PERSON(order.getF_ORDER_PERSON());
						goods.setF_ORDER_NO(orderDetail.getF_ORDER_NO());
						goods.setF_COMM_STATUS(orderDetail.getF_COMM_STATUS());
						goods.setF_SELL_ATM(orderDetail.getF_SELL_ATM());
						goodsList.add(goods);
					}
				}

			}
		}

		return goodsList;
	}

	/**
	 * 根据用户标识获取用户列表
	 * 
	 * @param status
	 *            用户标识
	 * @return
	 */
	public List<Custom> getCustomsByStatus(String status) {

		return myDBHelper.getCustomByStatus(status);
	}

	/**
	 * 获取所有用户
	 * 
	 * @return
	 */
	public List<Custom> getCustoms() {
		return myDBHelper.getAllCustom();
	}

	/**
	 * 订单提交
	 * 
	 * @param personNO
	 *            司机工号
	 * @param customCode
	 *            客户编号
	 * @param goods
	 *            商品详情
	 * @return
	 */
	public boolean subOrderDetail(String personNO, String customCode,
			List<OrderGoods> list) {

		String methodName = "Driver_Save_Truck_Out_";
		HashMap<String, String> propertys = new HashMap<String, String>();
		propertys.put("personno", personNO);
		propertys.put("custom_code", customCode);
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			OrderGoods goods = list.get(i);
			str += goods.getF_COMM_CODE() + "," + goods.getF_NUM1() + ","
					+ goods.getF_NUM2() + "," + goods.getF_SELL_ATM() + ";";
		}

		propertys.put("str", str);
		propertys.put("orderNo", list.get(0).getF_ORDER_NO());

		if (convertToBean.saveNewOrder(methodName, propertys)) {
			return true;
			
		} else {
			return false;
		}

	}

	/**
	 * 将bitmap存到本地
	 * 
	 * @param bitmap
	 * @param custom
	 */
	public static void saveImgSdCard(Bitmap bitmap, Custom custom) {
		System.out.println("图片本地化");

		String imgUrl = BitmapUtil.saveBitmap(bitmap, custom.getF_CUSTOM_PIC());
		if (imgUrl != "")
			myDBHelper.updateCustomImg(imgUrl, custom.getF_CUSTOM_CODE());
	}

	/**
	 * 获取客户的bitmap图片
	 * 
	 * @param custom
	 * @return
	 */

	public static boolean getBitmapByCustom(final Custom custom,
			final ImageView imgView, final Handler handler) {
		// final Bitmap customPic=null;
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		System.out.println(custom.getF_CUSTOM_PIC());
		File file = new File(custom.getF_CUSTOM_PIC());
		if (file.exists()) {
			// 本地存在

			final Bitmap customPic = BitmapFactory.decodeFile(custom
					.getF_CUSTOM_PIC());
			/*
			 * Message msg=myHandler.obtainMessage(); msg.obj=customPic;
			 * myHandler.sendMessage(msg);
			 */
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					imgView.setImageBitmap(customPic);
				}
			});

			return true;
		} else {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					byte[] data = BitmapUtil.getImage(custom.getF_CUSTOM_PIC());
					final Bitmap bit = BitmapFactory.decodeByteArray(data, 0,
							data.length);

					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							imgView.setImageBitmap(bit);
						}

					});

					// 保存到本地
					saveImgSdCard(bit, custom);

					/*
					 * Message msg=myHandler.obtainMessage(); msg.obj=bit;
					 * myHandler.sendMessage(msg);
					 */

				}

			});

		}

		return true;
	}

	/**
	 * 返回新增订单号
	 * 
	 * @return
	 */
	public String createOrderUUID() {
		String s = UUID.randomUUID().toString();
		return s;
	}

	/**
	 * 添加一个订单
	 * 
	 * @param customId
	 *            客户id
	 * @param orderPerson
	 *            下单人姓名
	 */
	public String addOneOrder(String customId, String orderPerson) {
		String orderId = createOrderUUID();

		Order order = new Order();
		order.setF_ORDER_NO(orderId);
		order.setF_CUSTOM_CODE(customId);
		order.setF_ORDER_PERSON(orderPerson);
		order.setF_STATUS(0);
		order.setF_DATE(Utils.GetTime());

		myDBHelper.addOneOrder(order);

		return orderId;
	}

	public void ClearLocalOrder(String custom_code) {
		myDBHelper.deleteOneOrder(custom_code);
		// if (myDBHelper.deleteOneOrder(orderNo)
		// && myDBHelper.deleteOrderDetial(orderNo)) {
		// return true;
		// } else {
		// return false;
		// }
	}

	/**
	 * 添加商品到订单
	 * 
	 * @param orderId
	 *            订单号
	 * @param commName
	 *            商品名
	 * @param commId
	 *            商品Id
	 */
	public OrderDetail addOneGoods(String orderId, String commName,
			String commId) {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setF_ORDER_NO(orderId);
		orderDetail.setF_COMM_CODE(commId);
		orderDetail.setF_COMM_NAME(commName);
		orderDetail.setF_COMM_STATUS(0);
		orderDetail.setF_NUM1(0);
		orderDetail.setF_NUM2(0);
		orderDetail.setF_SELL_ATM(0 + "");
		myDBHelper.addOneOrderDetail(orderDetail);
		return orderDetail;
	}

	public Custom getCustomInfo(String custom_code) {
		return myDBHelper.getCustomByCode(custom_code);
	}

	public boolean haveOrderDetial(String custom_code, String comm_code) {
		return myDBHelper.haveOrderDetial(custom_code, comm_code);
	}

	public void updateOrderDetail_Status(String custom_code) {
		myDBHelper.updateOrderDetail_Status(custom_code);
	}

}
