package com.orwlw.activity;

import tf.RfidDemo.SerialPort;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.utils.Utils;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.net.ConvertToBean;

/**
 * 主界面
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends ActivityGroup {
	private FrameLayout container = null;
	private ImageButton btnModule1;
	private ImageButton btnModule2;
	private Button btn_all;
	private Button btn_nr;
	private Button btn_scan;
	private ConvertToBean toBean;
	private boolean statue_list = false;
	private boolean statue_map = false;
	private boolean btn_top = false;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; // 需要自己定义标志

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
				FLAG_HOMEKEY_DISPATCHED);// 关键代码
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		init();
		btn_all.setOnClickListener(chuandi);
		btn_nr.setOnClickListener(chuandi);
		btn_scan.setOnClickListener(chuandi);
		btnModule1.setImageDrawable(getResources().getDrawable(
				R.drawable.liebiao));
		// 列表点击事件
		btnModule1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				statue_list = false;
				statue_map = true;

				container.removeAllViews();
				container.addView(getLocalActivityManager().startActivity(
						"ShopListActivity",
						new Intent(MainActivity.this, ShopListActivity.class)
								.putExtra("AllorNo", "1").addFlags(
										Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				// 设置按钮背景图片
				btnModule1.setImageDrawable(getResources().getDrawable(
						R.drawable.liebiao));
				btnModule2.setImageDrawable(getResources().getDrawable(
						R.drawable.daohang));
				if (btn_top == false && statue_list == false) {// 全部+点击地图
					chuandi("1");
				} else if (btn_top == true && statue_list == false) {
					chuandi("2");
				}
			}
		});
		// 地图点击事件
		btnModule2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				statue_list = true;
				statue_map = false;

				container.removeAllViews();
				container.addView(getLocalActivityManager().startActivity(
						"MapActivity",
						new Intent(MainActivity.this, MapActivity.class)
								.putExtra("AllorNo", 3).addFlags(
										Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
				// 设置按钮背景图片
				btnModule1.setImageDrawable(getResources().getDrawable(
						R.drawable.liebiaoup));
				btnModule2.setImageDrawable(getResources().getDrawable(
						R.drawable.daohangdown));
				if (btn_top == false && statue_map == false) {// 全部+点击地图
					chuandi1("1");
				} else if (btn_top == true && statue_map == false) {
					chuandi1("2");
				}

			}
		});
	}

	/* 初始化 */
	private void init() {
		container = (FrameLayout) findViewById(R.id.frameLayout1);
		btnModule2 = (ImageButton) findViewById(R.id.button2);
		btnModule1 = (ImageButton) findViewById(R.id.button1);
		btn_all = (Button) findViewById(R.id.all);
		btn_nr = (Button) findViewById(R.id.no);
		btn_scan = (Button) findViewById(R.id.btn_scan);

	}

	// 未配送和所有客户，命令的传递
	private OnClickListener chuandi = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all:
				btn_top = false;
				btn_all.setBackgroundResource(R.drawable.one);
				btn_nr.setBackgroundResource(R.drawable.title_btn_right);
				btn_scan.setBackgroundResource(R.drawable.title_btn_right);
				// btn_all.setTextColor(R.color.black);
				// btn_nr.setTextColor(R.color.white);
				if (statue_list == false && btn_top == false) {// 客户列表点击
																// ：点击列表+点击全部客户
					chuandi("1");
				} else if (statue_map == false && btn_top == false) {// 地图列表
																		// ：点击地图列表+点击全部客户
					chuandi1("1");
				}
				break;
			case R.id.no:
				btn_top = true;
				btn_nr.setBackgroundResource(R.drawable.one);
				btn_all.setBackgroundResource(R.drawable.title_btn_right);
				btn_scan.setBackgroundResource(R.drawable.title_btn_right);
				// btn_nr.setTextColor(R.color.white);
				// btn_all.setTextColor(R.color.black);

				if (statue_list == false && btn_top == true) {// 客户列表点击
																// ：点击列表+部分客户
					chuandi("2");
				} else if (statue_map == false && btn_top == true) {// 地图列表
																	// ：点击地图列表+部分客户
					chuandi1("2");
				}
				break;
			case R.id.btn_scan:
				// 扫描RFID卡
				btn_top = true;
				// btn_scan.setBackgroundResource(R.drawable.one);
				// btn_all.setBackgroundResource(R.drawable.one);
				// btn_nr.setBackgroundResource(R.drawable.one);
				// btn_nr.setTextColor(R.color.white);
				// btn_all.setTextColor(R.color.black);
				Intent intent = new Intent(MainActivity.this,
						ScanRfidActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}

		}
	};

	// 传递更新指令-》ShopListActivity
	private void chuandi(String zhiling) {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				"ShopListActivity",
				new Intent(MainActivity.this, ShopListActivity.class).putExtra(
						"AllorNo", zhiling).addFlags(
						Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
	}

	// 传递更新指令-》MapActivity
	private void chuandi1(String zhiling) {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				"MapActivity",
				new Intent(MainActivity.this, MapActivity.class).putExtra(
						"AllorNo", zhiling).addFlags(
						Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		statue_list = false;
		statue_map = true;
		super.onResume();

		// 设置按钮背景图片
		btnModule1.setImageDrawable(getResources().getDrawable(
				R.drawable.liebiao));
		btnModule2.setImageDrawable(getResources().getDrawable(
				R.drawable.daohang));
		// btn_all.setBackgroundResource(R.drawable.two);
		// btn_nr.setBackgroundResource(R.color.transparent);
		// btn_all.setTextColor(R.color.black);
		// btn_nr.setTextColor(R.color.white);
		// 传递命令打开全部客户
		chuandi("1");

		Intent intent2 = new Intent(MainActivity.this, Checkserver.class);
		startService(intent2);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			// Utils.dialog(MainActivity.this);
			btn_top = true;
			Intent intent = new Intent(MainActivity.this,
					ScanRfidActivity.class);
			startActivity(intent);
			return true;
		}
		if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {//
			return true;
		}
		return true;
	}

	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
		
	}
    
}
