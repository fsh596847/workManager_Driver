package com.orwlw.activity;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import cn.com.adapter.Shop_detailesAdapter;
import cn.com.utils.ExitApplication;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.Comm;
import com.orwlw.dao.MyDBHelper;

//商品列表
public class CommDialog extends Dialog {

	private List<Comm> list =null;
	private Context context;
	private EditText etSearch;
	private ListView listView;
	private MyDBHelper dbHelper;
	private Shop_detailesAdapter  adapter;
	private String str;
	private ExitApplication instance;
	private Handler handler;

	public CommDialog( final Context context, int which, List<Comm> list,Handler  handler) {
		super(context, R.style.dialog);
		this.context = context;
		this.list = list;
		this.handler = handler;
		dbHelper = MyDBHelper.getInstance(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		list=dbHelper.getAllCommByName(str);
		if(list!=null){
		 adapter = new Shop_detailesAdapter(list, context);
		listView.setAdapter(adapter);
		}
		else 
		{
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
			list=dbHelper.getAllCommByName(str);
			if(list!=null){
			 adapter = new Shop_detailesAdapter(list, context);
			listView.setAdapter(adapter);
			}
			else 
			{
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

			Intent it = new Intent();
			Comm weather1 = list.get(arg2);
			String name1 = weather1.getF_COMM_NAME();
			instance = ExitApplication.getInstance();
			instance.setccommname(name1);
			handler.sendEmptyMessage(0);
			Bundle mBundle = new Bundle();
			mBundle.putString("name", name1);// 压入数据
			//mBundle.putString("personname", "闫成柱");// 压入数据
			it.putExtra(name1, true);
			it.setClass(context, Order_Activity.class);
			CommDialog.this.dismiss();

		}

	};
}
