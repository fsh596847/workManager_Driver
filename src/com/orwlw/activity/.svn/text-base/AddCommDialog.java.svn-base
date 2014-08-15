package com.orwlw.activity;

import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import cn.com.adapter.Shop_detailesAdapter;
import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.Comm;
import com.orwlw.bean.OrderDetail;
import com.orwlw.dao.MyDBHelper;
import com.orwlw.dao.UtilDao;

/**
 * 添加商品列表窗体
 * 
 * @author Administrator
 * 
 */
public class AddCommDialog extends Dialog {

	private List<Comm> list = null;
	private Context context;
	private EditText etSearch;
	private String customcode;
	private String personcode;
	private ListView listView;
	private MyDBHelper dbHelper;
	private UtilDao UtilDao;
	private Shop_detailesAdapter adapter;
	private String str;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; // 需要自己定义标志

	private Handler handler;

	public AddCommDialog(final Context context, Handler handler, int which,
			List<Comm> list, String customcode, String personcode) {
		super(context, R.style.dialog);
		this.context = context;
		this.list = list;
		this.customcode = customcode;
		this.personcode = personcode;
		dbHelper = MyDBHelper.getInstance(context);
		this.handler = handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
				FLAG_HOMEKEY_DISPATCHED);// 关键代码
		this.setContentView(com.orwlw.WorkManager_Driver.R.layout.dialog_detail);
		etSearch = (EditText) findViewById(R.id.et_searchComm);
		listView = (ListView) findViewById(R.id.listView1);

		str = etSearch.getText().toString();

		etSearch.addTextChangedListener(twETSeaCon);
		uodate_listView();
	}

	// 初始化listView的各项数据
	private void uodate_listView() {
		dbHelper = MyDBHelper.getInstance(context);
		list = dbHelper.getAllCommByName(str);
		if (list != null) {
			adapter = new Shop_detailesAdapter(list, context);
			listView.setAdapter(adapter);
		} else {
			listView.setAdapter(null);
		}
		listView.setOnItemClickListener(litener);
	}

	// 编辑框监听输入的关键字
	private TextWatcher twETSeaCon = new TextWatcher() {

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		// 文本框输入完成后执行此方法
		public void afterTextChanged(Editable s) {
			str = etSearch.getText().toString();
			dbHelper = MyDBHelper.getInstance(context);
			list = dbHelper.getAllCommByName(str);
			if (list != null) {
				adapter = new Shop_detailesAdapter(list, context);
				listView.setAdapter(adapter);
			} else {
				listView.setAdapter(null);
			}
		}
	};

	// 点击商品列表将商品信息返回到输入框
	OnItemClickListener litener = new OnItemClickListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Comm weather1 = list.get(arg2);
			String name1 = weather1.getF_COMM_NAME();
			String name2 = weather1.getF_COMM_CODE();
			UtilDao = new UtilDao(context);
			// 本地添加新订单
			String orderid = UtilDao.addOneOrder(customcode, personcode);
			// 增加订单商品
			OrderDetail odts = UtilDao.addOneGoods(orderid, name1, name2);
			Order_Activity.tempog.setF_ORDER_NO(odts.getF_ORDER_NO());
			Order_Activity.tempog.setF_COMM_CODE(odts.getF_COMM_CODE());
			Order_Activity.tempog.setF_COMM_NAME(odts.getF_COMM_NAME());
			Order_Activity.tempog.setF_SELL_ATM(0+"");
			// 通知重新加载订单商品列表
			handler.sendEmptyMessage(0);
			AddCommDialog.this.dismiss();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.dismiss();
		}
		return true;
	}
}
