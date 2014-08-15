package com.orwlw.activity;

import java.math.BigDecimal;
import java.util.List;

import cn.com.utils.ExitApplication;
import cn.com.utils.Utils;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.OrderGoods;
import com.orwlw.dao.UtilDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 整体订单预览界面
 * 
 * @author Administrator
 * 
 */
public class AllOfOrder_Activity extends Activity {
	private String customcode;
	private String customname;
	private String personname;
	private String driver_name;
	private String orderNo;
	private TextView textview;
	private TextView shop;
	private TextView ddr;
	private String date1;
	private TextView date;

	// sql
	private UtilDao UtilDao;
	// 订单商品列表
	private List<OrderGoods> list_orderGoods;
	private ImageButton print_btn_img;

	Button btn_ok;
	public static Button btn_start_print;

	private ExitApplication instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		ExitApplication.getInstance().addActivity(this);

		init();

		customcode = ((MyApplication) getApplication()).Getlocaldata().current_custom_code;
		customname = ((MyApplication) getApplication()).Getlocaldata().current_custom_name;

		// 根据门店编码取回商品列表
		UtilDao = new UtilDao(AllOfOrder_Activity.this);
		list_orderGoods = UtilDao.getGoodsByCustom(customcode);
		String text = getShowString(list_orderGoods);
		textview.setText(text);
		shop.setText(customname);
		ddr.setText(personname);
		date.setText(date1);
		if (list_orderGoods.size() > 0) {
			orderNo = list_orderGoods.get(0).getF_ORDER_NO();
			if (list_orderGoods.get(0).getF_COMM_STATUS() == 1) {
				btn_ok.setText("已完成");
				btn_ok.setClickable(false);
			}
		}

		instance = ExitApplication.getInstance();
		instance.addActivity(this);

		driver_name = ((MyApplication) getApplication()).Getlocaldata().personname;

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void message(int x, String strcontent) {
		Message attaget = Message.obtain();
		attaget.what = x;
		attaget.obj = strcontent;
		Order_Activity.handler1.sendMessage(attaget);
	}

	void init() {
		textview = (TextView) findViewById(R.id.commview);// 商品内容
		shop = (TextView) findViewById(R.id.shop);// 下单门店
		ddr = (TextView) findViewById(R.id.ddr);// 下单人
		date = (TextView) findViewById(R.id.time);// 订单日期
		print_btn_img = (ImageButton) findViewById(R.id.print_btn_img);// 订单日期
		print_btn_img.setOnClickListener(onClickListener);
		btn_ok = (Button) findViewById(R.id.print_btn_ok);
		btn_start_print = (Button) findViewById(R.id.print_btn_start);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.print_btn_img:
				// 订单项 --Order_item_Activity
				// Intent in = new Intent(AllOfOrder_Activity.this,
				// Print_OrderActivity.class);
				// startActivity(in);
				break;

			default:
				break;
			}

		}
	};

	// 预览商品内容
	public String getShowString(List<OrderGoods> list_orderGoods) {
		double ALL_ATM = 0;
		String text = "\n\n货物名称								数量*单价\n";
		for (OrderGoods OrderGoods : list_orderGoods) {
			double atm = OrderGoods.getF_NUM1()
					* Double.parseDouble(OrderGoods.getF_SELL_ATM()
							.equalsIgnoreCase("") ? "0" : OrderGoods
							.getF_SELL_ATM());
			BigDecimal b = new BigDecimal(atm);
			atm = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

			text += OrderGoods.getF_COMM_NAME();
			text += "\n";
			text += "										";
			text += OrderGoods.getF_NUM1() + " * " + OrderGoods.getF_SELL_ATM()
					+ "\n";
			text += "										";
			text += "小计：";
			text += atm + "";
			text += "\n";
			personname = OrderGoods.getF_ORDER_PERSON();
			date1 = (String) (OrderGoods.getF_DATE());
			ALL_ATM += atm;
		}
		text += "									";
		text += "共计：" + Utils.GetdoubleNumber(ALL_ATM);
		return text;
	}

	// 打印商品内容
	public String getPrintString(List<OrderGoods> list_orderGoods) {
		double ALL_ATM = 0;
		String text = "货物名称               数量*单价\n";
		for (OrderGoods OrderGoods : list_orderGoods) {
			double atm = OrderGoods.getF_NUM1()
					* Double.parseDouble(OrderGoods.getF_SELL_ATM()
							.equalsIgnoreCase("") ? "0" : OrderGoods
							.getF_SELL_ATM());
			BigDecimal b = new BigDecimal(atm);
			atm = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

			text += OrderGoods.getF_COMM_NAME();
			text += "\n";
			text += "                         ";
			text += OrderGoods.getF_NUM1()
					+ "*"
					+ (OrderGoods.getF_SELL_ATM().equalsIgnoreCase("") ? "0"
							: OrderGoods.getF_SELL_ATM()) + "\n";
			text += "                     ";
			text += "小计：";
			text += atm + "";
			text += "\n";
			personname = OrderGoods.getF_ORDER_PERSON();
			date1 = (String) ("\n" + OrderGoods.getF_DATE() + "\n");
			ALL_ATM += atm;
		}
		text += "\n                   ";
		text += "共计：" + Utils.GetdoubleNumber(ALL_ATM) + "\n";
		return text;
	}

	public void btn_print(View v) {
		btn_ok.setText("提交中...");
		btn_ok.setClickable(false);
		String personno = instance.getpersoncode();
		if (list_orderGoods.size() > 0) {
			boolean i = UtilDao.subOrderDetail(personno, customcode,
					list_orderGoods);
			if (i) {
				// ==函数执行成功
				btn_ok.setText("已完成！");
				btn_ok.setClickable(false);
				UtilDao.updateOrderDetail_Status(customcode);
			} else {
				// ------函数执行失败
				btn_ok.setText("请点击重新提交！");
				btn_ok.setClickable(true);
			}
		} else {
			Toast.makeText(this, "请添加商品后提交上报！", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 打印小票
	 * 
	 * @param v
	 */
	public void btn_print_start(View v) {
		if (!btn_ok.isClickable()) {
			String message = "打印内容";
			String printstr = "      *伊利牛奶【配送单】*       \n"
					+ "================================"
					+ getPrintString(list_orderGoods)
					+ "================================"
					+ "\n门店："
					+ shop.getText().toString()
					+ "\n时间："
					+ (date.getText().toString() + "\n配送：" + driver_name + "\n"
							+ (orderNo) + "\n签字：\n\n\n");
			message(2, printstr);
			

		} else {
			Toast.makeText(this, "请下单成功后进行打印！", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return true;
	}

}
