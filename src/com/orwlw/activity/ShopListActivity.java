package com.orwlw.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import cn.com.adapter.Shop_itemAdapter;
import cn.com.utils.ExitApplication;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.Custom;
import com.orwlw.dao.MyDBHelper;
import com.orwlw.net.ConvertToBean;

/**
 * 客户列表界面
 * 
 * @author Administrator
 * 
 */
public class ShopListActivity extends Activity {
	private MyListView listView;
	private ExitApplication ExitApplication;
	private Shop_itemAdapter adapter;
	private MyDBHelper myDBHelper;
	private ConvertToBean toBean;
	private String AllorNo;
	private ExitApplication instance;
	private List<Custom> list_custom;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shop_main);

		// 1.初始化控件
		initview();

		// 2.获取数据
		Bundle bundle = this.getIntent().getExtras();
		AllorNo = bundle.getString("AllorNo");
		list_custom = myDBHelper.getAllCustom();

		// 3.加载数据给界面
		initdata();

	}

	// 初始化view控件
	private void initview() {
		myDBHelper = MyDBHelper.getInstance(this);
		toBean = new ConvertToBean(ShopListActivity.this);
		instance = ExitApplication.getInstance();
		instance.addActivity(getParent());
		listView = (MyListView) findViewById(R.id.listView1);
		listView.setOnItemClickListener(listener);
		listView.setonRefreshListener(new com.orwlw.activity.MyListView.OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							HashMap<String, String> propertys = new HashMap<String, String>();
							propertys.put("personno", instance.getpersoncode());

							String synstime = ((MyApplication) getApplication())
									.Getlocaldata().LAST_SYSNC_DATE;
							propertys.put("LastTime", synstime);
							// 同步客户
							list_custom=toBean.getAllCustom(
									"Driver_GetCustoms_ByLastdate_Driver",
									propertys);
							initdata();
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					protected void onPostExecute(Void result) {
						listView.onRefreshComplete();
					}

				}.execute();
			}
		});

	}

	// 初始化数据
	private void initdata() {
		if (list_custom != null) {
			adapter = new Shop_itemAdapter(list_custom, ShopListActivity.this,
					handler);
			listView.setAdapter(adapter);

		} else {
			Toast.makeText(ShopListActivity.this, "获取数据失败", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onStart() {
		// 获得传过来的值
		Intent intent = getIntent();
		String num = intent.getStringExtra("AllorNo");
		update_list(num);
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// 切换加载所有客户数据还是加载未配送客户数据
	private void update_list(String which) {
		if (which == "1") {// 加载所有客户数据
			adapter = new Shop_itemAdapter(list_custom, ShopListActivity.this,
					handler);
			listView.setAdapter(adapter);
		} else if (which == "2") {// 加载未配送客户数据
			list_custom = myDBHelper.getCustomByStatus(AllorNo);
			adapter = new Shop_itemAdapter(list_custom, ShopListActivity.this,
					handler);
			listView.setAdapter(adapter);
		}
	}

	// 客户列表跳转--》订单界面
	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String customcode = list_custom.get(arg2 - 1).getF_CUSTOM_CODE();
			String customname = list_custom.get(arg2 - 1).getF_CUSTOM_NAME();
			((MyApplication) getApplication()).Savelocaldata(
					"current_custom_code", customcode);
			((MyApplication) getApplication()).Savelocaldata(
					"current_custom_name", customname);
			// instance.setcustomcode(customcode);

			Intent intent = new Intent(ShopListActivity.this,
					Order_Activity.class).putExtra("customcode", customcode)
					.putExtra("customname", customname);
			startActivity(intent);
		}
	};
}
