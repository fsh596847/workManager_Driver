package com.orwlw.activity;

import cn.com.utils.ExitApplication;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.Custom;
import com.orwlw.dao.UtilDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import tf.RfidDemo.SerialPort;

public class ScanRfidActivity extends Activity implements OnClickListener {

	Thread mthread;
	Button btn;
	private UtilDao UtilDao;
	TextView tv_name;
	TextView tv_address;
	Custom currentcustom;
	private ExitApplication instance;
	String current_custom_code = "";
	Button show_custom_list;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; // 需要自己定义标志

	Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			UtilDao = new UtilDao(ScanRfidActivity.this);
			currentcustom = UtilDao.getCustomInfo(current_custom_code);
			tv_name.setText(currentcustom.getF_CUSTOM_NAME());
			tv_address.setText(currentcustom.getF_ADDRESS());
			goPutTruck();
		}
	};

	@Override
	protected void onDestroy() {
		flag = false;
		super.onDestroy();
	}

	void message(int x) {
		Message attaget = Message.obtain();
		attaget.what = x;
		handler1.sendMessage(attaget);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
				FLAG_HOMEKEY_DISPATCHED);// 关键代码
		setContentView(R.layout.scan_layout);

		instance = ExitApplication.getInstance();
		instance.addActivity(getParent());

		btn = (Button) findViewById(R.id.btn_ok);
		btn.setOnClickListener(this);
		show_custom_list = (Button) findViewById(R.id.show_custom_list);
		show_custom_list.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_address = (TextView) findViewById(R.id.tv_address);
		flag = true;
		scanThread();

	}

	@Override
	public void onClick(View v) {
		if (v == btn) {
			goPutTruck();
		}
		if (v == show_custom_list) {
			Intent intent = new Intent(ScanRfidActivity.this,
					MainActivity.class);
			startActivity(intent);
		}
	}

	void goPutTruck() {
		if (currentcustom != null) {
			String customcode = currentcustom.getF_CUSTOM_CODE();
			String customname = currentcustom.getF_CUSTOM_NAME();
			instance.setcustomcode(customcode);
			Intent intent = new Intent(ScanRfidActivity.this,
					Order_Activity.class).putExtra("customcode", customcode)
					.putExtra("customname", customname);
			startActivity(intent);
		}
		finish();

	}

	boolean flag = false;

	void scanThread() {
		mthread = new Thread() {
			@Override
			public void run() {
				while (flag) {
					try {
						ScanRfid();
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}
				}
				super.run();
			}
		};
		mthread.start();
	}

	void ScanRfid() {
		if (RfidPoweron()) {
			if (func_ic_reset()) {
				if (func_readCardID()) {
					if (func_verify()) {
						if (func_readContent()) {
							if (result_content != null
									&& !result_content.equals("")) {
								String result = result_content;
								result = result.substring(6, 22);
								result = toStringHex(result);
								current_custom_code = result;
								message(1);

								flag = false;
							}

						}
					}
				}
			}

		}
		MyApplication.WriteLog("扫描RFID失败");
	}

	// 转化十六进制编码为字符串
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	private int fdUart = -1;
	private static final String RFID_DEV = "/dev/ttyMT1";
	private static final String cmd_for_reset_ic = "AA01010055";
	// private static final String cmd_for_read_ic = "AA01000055";
	private static final String cmd_for_scan_data = "AA0206000055";
	private static int KEY_LENGTH = 12;
	private static final String str_Key = "FFFFFFFFFFFF";

	private static final String PREFIX_VERIFY = "AA0907";
	private static final String KEY_A_VERIFY = "00";
	private static final String SUFFIX_WRITE = "0055";

	private static final String PREFIX_READ = "AA0308";
	private String result_content = "";

	// 打开serialport
	private boolean RfidPoweron() {
		try {
			fdUart = SerialPort.open(RFID_DEV);
			if (fdUart <= 0) {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	// 初始化IC卡
	private boolean func_ic_reset() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ret = 0;

		if (fdUart <= 0) {
			return false;
		}
		try {
			ret = SerialPort.writeData(fdUart,
					convertToByteArray(cmd_for_reset_ic));
		} catch (Exception ex) {
			return false;
		}

		if (ret < 0) {
			return false;
		}
		return true;
	}

	// 读取CardID
	private boolean func_readCardID() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ret = 0;

		if (fdUart <= 0) {
			return false;
		}

		try {
			ret = SerialPort.writeData(fdUart,
					convertToByteArray(cmd_for_scan_data));
		} catch (Exception ex) {
			return false;
		}

		if (ret <= 0) {
			return false;
		}

		String str_read = "";
		try {
			str_read = SerialPort.receiveData(fdUart);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}

		if ((str_read != null) && strValidityCheck(str_read)) {
			// str_show為CardID
			String str_show = str_read.substring(6, 24);
			return true;
		} else {
			return false;
		}
	}

	// 验证扇区
	private boolean func_verify() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ret = 0;

		if (fdUart <= 0) {
			return false;
		}

		if (str_Key.length() != KEY_LENGTH) {
			return false;
		}

		// 扇区
		String sector_id = "03";
		String str_write = PREFIX_VERIFY + str_Key + KEY_A_VERIFY + sector_id
				+ SUFFIX_WRITE;

		try {
			ret = SerialPort.writeData(fdUart, convertToByteArray(str_write));
		} catch (Exception ex) {
			return false;
		}

		if (ret <= 0) {
			return false;
		}

		String str_read = "";
		try {
			str_read = SerialPort.receiveData(fdUart);
		} catch (Exception ex) {
			return false;
		}

		if ((str_read != null) && (str_read.length() >= 8)) {
			String valid_pos = str_read.substring(4, 8);
			if (valid_pos.equals("0000"))
				// 验证成功
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	// 读取内容
	private boolean func_readContent() {
		int ret = 0;
		if (fdUart <= 0) {
			return false;
		}

		// 扇区
		String sector_id = "03";
		// 块
		String block_id = "00";

		String str_write = PREFIX_READ + block_id + sector_id + SUFFIX_WRITE;

		try {
			ret = SerialPort.writeData(fdUart, convertToByteArray(str_write));
		} catch (Exception ex) {
			return false;
		}

		if (ret <= 0) {
			return false;
		}

		String str_read = "";
		try {
			str_read = SerialPort.receiveData(fdUart);
		} catch (Exception ex) {
			return false;
		}

		if ((!str_read.equals("")) && strValidityCheck(str_read)) {
			// str_read值为内容 编码方式是阿斯卡码编码
			result_content = str_read;
			return true;
		} else {
			result_content = null;
			return false;
		}
	}

	private byte[] convertToByteArray(String content) {
		StringBuilder str16 = new StringBuilder();
		int length = content.length();
		byte byteArray[] = new byte[length / 2];

		if (length % 2 == 0) {
			str16.trimToSize();
			for (int i = 0; i < length / 2; i++) {
				try {
					String first = content.substring(i * 2, i * 2 + 1);
					String second = content.substring(i * 2 + 1, i * 2 + 2);
					int iFirst = Integer.parseInt(first, 16);
					int iSecond = Integer.parseInt(second, 16);
					int newNum = (iFirst * 16 + iSecond) & 0xff;
					byteArray[i] = (byte) (newNum & 0xff);
					char c = (char) (newNum & 0xff);
					str16.append(c);
				} catch (NumberFormatException e) {
					return byteArray;
				}
			}
		} else {
			return byteArray;
		}

		return byteArray;
	}

	private boolean strValidityCheck(String str) {
		if (str == null || str.equals(""))
			return false;

		if (str.length() >= 6) {
			String valid_pos = str.substring(4, 6);

			if (valid_pos.equals("00"))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	private void releaseDevice() {
		try {
			SerialPort.close(fdUart);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		fdUart = -1;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return true;
	}

}
