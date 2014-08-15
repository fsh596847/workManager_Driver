package com.orwlw.activity;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.adapter.Order_itemAdapter;
import cn.com.utils.ExitApplication;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.OrderGoods;
import com.orwlw.dao.UtilDao;

/**
 * 订单商品界面
 * 
 * @author Administrator
 * 
 */
public class Order_item_Activity extends Activity implements
		OnItemClickListener {

	private ListView listView;
	private Order_itemAdapter adapter;
	private ExitApplication ExitApplication;
	private String customcode;
	private ExitApplication instance;
	// sql
	private UtilDao UtilDao;
	// 订单商品列表
	private List<OrderGoods> list_orderGoods;
	private Context mContext;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_list);

		instance = ExitApplication.getInstance();

		listView = (ListView) findViewById(R.id.listView1);
		mContext = getApplicationContext();
		listView.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		EditNum_PriceDialog dialog = new EditNum_PriceDialog(
				Order_item_Activity.this, list_orderGoods.get(arg2));
		dialog.show();
	}

	@Override
	protected void onResume() {

		customcode = ((MyApplication) getApplication()).Getlocaldata().current_custom_code;

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION);
		mContext.registerReceiver(myReceiver, filter);

		// 根据客户编码获取该客户订单配送商品 加载到listview
		UtilDao = new UtilDao(Order_item_Activity.this);
		list_orderGoods = UtilDao.getGoodsByCustom(customcode);
		adapter = new Order_itemAdapter(list_orderGoods,
				Order_item_Activity.this);
		listView.setAdapter(adapter);

		super.onResume();
	}

	@Override
	protected void onPause() {
		mContext.unregisterReceiver(myReceiver);
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 回退键位清空输入
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			try {
				View focusview = getCurrentFocus();
				EditText ET = ((EditText) focusview);
				ET.setText("");
			} catch (Exception e) {

			}
			// Utils.dialog(Order_item_Activity.this);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		}
		return true;
	}

	// listview至指定位置
	private void setListViewPos(int pos) {
		if (android.os.Build.VERSION.SDK_INT >= list_orderGoods.size()) {
			listView.smoothScrollToPosition(pos);
		} else {
			listView.setSelection(pos);
		}
	}

	public static final String ACTION = "cn.etzmico.broadcastreceiverregister.SENDBROADCAST";
	// 刷新广播
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			list_orderGoods = UtilDao.getGoodsByCustom(customcode);
			adapter = new Order_itemAdapter(list_orderGoods,
					Order_item_Activity.this);
			listView.setAdapter(adapter);
		}
	};
}
