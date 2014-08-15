package com.orwlw.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import tf.test.SerialPort;
import tf.test.SpecialKey;
import android.app.Activity;
import android.app.ActivityGroup;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.utils.BluetoothService;
import cn.com.utils.ExitApplication;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.Comm;
import com.orwlw.bean.OrderDetail;
import com.orwlw.bean.OrderGoods;
import com.orwlw.dao.MyDBHelper;
import com.orwlw.dao.UtilDao;

/**
 * �Ż�����
 * 
 * @author Administrator
 * 
 */
public class Order_Activity extends ActivityGroup {
	private FrameLayout container = null;
	private List<Comm> list = null;// ��Ʒ�б�
	private ImageButton btnModule1;
	private ImageButton btnModule2;
	private Button btn_back;
	private ImageButton btn_add;
	private String customcode;
	private ExitApplication instance;
	private static TextView ordertext;
	private String customname = "";
	private String personcode;

	public static Order_Activity oractivity;

	private UtilDao UtilDao;
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; // ��Ҫ�Լ������־

	/* һ��Ϊɨ������������� */
	private SpecialKey mSpecialKey = new SpecialKey();
	private String content = "6901721197038";
	private int fdUart = -1;
	private String mContent = "ABCDEF";
	private String mDevName = "/dev/ttyMT0";
	private static final String TAG = "TestEVB";
	private static final String PREFERENCE_NAME = "testEVB";
	private Context mContext;
	KeyPressReceiver mReceiver;
	private static String ACTION_SPECIALKEY = "tf.test.SpecialKeyPressed";

	PowerManager mPowerManager = null;
	WakeLock mWakeLock = null;
	// ��
	private Vibrator mVibrator;
	public static OrderGoods tempog = new OrderGoods();

	Handler refreshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			EditNum_PriceDialog dialog = new EditNum_PriceDialog(
					Order_Activity.this, tempog);
			dialog.show();

			// container.removeAllViews();
			// // ���ض����б�Activity
			// container.addView(getLocalActivityManager().startActivity(
			// "Order_item_Activity",
			// new Intent(Order_Activity.this, Order_item_Activity.class)
			// .putExtra("customcode", customcode)
			// .putExtra("customname", customname)
			// .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
			// .getDecorView());
			// super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
				FLAG_HOMEKEY_DISPATCHED);// �ؼ�����
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order);
		oractivity = this;
		init();

		/* һ��Ϊɨ�����벿�ִ��� */
		mContext = getApplicationContext();
		scanDataFunc_1();
		mSpecialKey.startListenthread();
		mSpecialKey.SendContext(mContext);
		mReceiver = new KeyPressReceiver();
		mPowerManager = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"My Lock");
		mVibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);

		// ��ӡ���ִ���
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "���豸��֧���������ܣ���ӡ���ܽ�����ʹ��", Toast.LENGTH_LONG)
					.show();
		}

		container.removeAllViews();
		// ���ؿͻ������б����
		container.addView(getLocalActivityManager().startActivity(
				"Order_item_Activity",
				new Intent(Order_Activity.this, Order_item_Activity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
		restartBS();

	}

	/* ��ʼ���ؼ� */
	private void init() {
		/* ��ȡ�������Ĳ��� */
		get_Parameter();
		// ��ʼ��
		container = (FrameLayout) findViewById(R.id.frameLayout1);
		btnModule1 = (ImageButton) findViewById(R.id.button1);
		btn_back = (Button) findViewById(R.id.back);
		btn_add = (ImageButton) findViewById(R.id.add);
		btnModule2 = (ImageButton) findViewById(R.id.button2);
		// btnModule1.setBackgroundResource(R.drawable.btn_down1);
		btn_back.setOnClickListener(onClickListener);
		btnModule1.setOnClickListener(onClickListener);
		btnModule2.setOnClickListener(onClickListener);
		btn_add.setOnClickListener(onClickListener);
		ordertext = (TextView) findViewById(R.id.ordertext);
	}

	/* ��ȡ�������Ĳ��� */
	private void get_Parameter() {
		instance = ExitApplication.getInstance();
		instance.addActivity(this);
		personcode = instance.getpersoncode();

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button1:
				// ������ --Order_item_Activity
				orderItem_show();
				break;
			case R.id.button2:
				// ������Ԥ�� --AllOfOrder_Activity
				orderView_show();
				autoConnectBS();
				break;
			case R.id.back:
				// ���ط���
				Order_Activity.this.finish();
				break;
			case R.id.add:
				// ������Ʒ
				AddCommDialog dialog = new AddCommDialog(Order_Activity.this,
						refreshHandler, 0, list, customcode, personcode);
				// ---���õ������͸����
				Window wd = dialog.getWindow();
				WindowManager.LayoutParams lp = wd.getAttributes();
				lp.alpha = 0.5f;
				wd.setAttributes(lp);// -------------------
				dialog.show();
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		btnModule1.setImageDrawable(getResources().getDrawable(
				R.drawable.dingdandown));
		btnModule2.setImageDrawable(getResources().getDrawable(
				R.drawable.zhengtidingdan2));
		customcode = ((MyApplication) getApplication()).Getlocaldata().current_custom_code;
		customname = ((MyApplication) getApplication()).Getlocaldata().current_custom_name;
		ordertext.setText("" + customname + "");
		oractivity = this;
		// restartBS();// ����2����������� ��ֹ�ոչص�

		/* ɨ�貿�� */
		mWakeLock.acquire();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SPECIALKEY);
		mContext.registerReceiver(mReceiver, filter);

	}

	void restartBS() {

		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}

		if (mService != null) {
			if (mService.getState() == BluetoothService.STATE_NONE) {
				mService.start();
			}
		} else {
			mService = new BluetoothService(mContext, handler1);
			if (mService.getState() == BluetoothService.STATE_NONE) {
				mService.start();
				MyApplication.WriteLog("mService.start()");
			}
		}

	}

	void autoConnectBS() {
		// new Thread() {
		// @Override
		// public void run() {
		//
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		if (mService != null) {
			if (mService.getState() == BluetoothService.STATE_CONNECTED) {
				AllOfOrder_Activity.btn_start_print.setText("���ӳɹ�,�����ӡ");
			} else {

				if (!((MyApplication) getApplication()).Getlocaldata().print_device
						.equalsIgnoreCase("")) {
					String address = ((MyApplication) getApplication())
							.Getlocaldata().print_device;
					if (BluetoothAdapter.checkBluetoothAddress(address)) {
						BluetoothDevice device = mBluetoothAdapter
								.getRemoteDevice(address);
						// Attempt to connect to the device
						if (device != null) {
							DEVICE_ID = address;
							mService.connect(device);
						}

					}
				}
			}
		}
		// super.run();
		// }
		// }.start();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/* ɨ�貿�� */
		releaseDevice();
		/* new request */
		mSpecialKey.stopListenthread();

		/* ��ӡ���� */
		if (mService != null) {
			mService.stop();
			MyApplication.WriteLog("mService stop()");
		}

		// if (mBluetoothAdapter != null) {
		// if (mBluetoothAdapter.isEnabled()) {
		// mBluetoothAdapter.disable();
		// }
		// }
	}

	@Override
	protected void onPause() {
		super.onPause();
		/* ɨ�貿�� */
		mWakeLock.release();
		mContext.unregisterReceiver(mReceiver);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * ������ϸActivity
	 */
	void orderItem_show() {
		btnModule1.setImageDrawable(getResources().getDrawable(
				R.drawable.dingdandown));
		btnModule2.setImageDrawable(getResources().getDrawable(
				R.drawable.zhengtidingdan2));
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				"Order_item_Activity",
				new Intent(Order_Activity.this, Order_item_Activity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
	}

	/**
	 * ������ӡԤ��Activity
	 */
	void orderView_show() {
		btnModule1.setImageDrawable(getResources().getDrawable(
				R.drawable.dingdan));
		btnModule2.setImageDrawable(getResources().getDrawable(
				R.drawable.zhengtidingdandown));
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				"AllOfOrder_Activity",
				new Intent(Order_Activity.this, AllOfOrder_Activity.class)
						.putExtra("customcode", customcode)
						.putExtra("customname", customname)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
	}

	class KeyPressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_SPECIALKEY.equals(intent.getAction())) {
				scanDataFunc_2();
				// }
			}
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

	/* new request: all function in one key */
	private void scanDataFunc_1() {
		mDevName = "/dev/ttyMT0";
		try {
			fdUart = SerialPort.open(mDevName);
			if (fdUart <= 0) {
				Toast toast = Toast.makeText(this, "uart open failed.",
						Toast.LENGTH_LONG);
				toast.show();
			} else {
				// Toast toast = Toast.makeText(this, "uart open Success.",
				// Toast.LENGTH_LONG);
				// toast.show();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}

	private void scanDataFunc_2() {
		int ret = 0;
		String card_type;
		String card_id;

		if (fdUart <= 0) {
			Toast toast = Toast.makeText(this, "func_ic_data Uart not open.",
					Toast.LENGTH_LONG);
			toast.show();
			Log.e(TAG, "scanDataFunc_2 Uart not open");
			scanDataFunc_1();
			return;
		}

		/* scan data */
		mContent = "";
		try {
			mContent = SerialPort.receiveData(fdUart);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		if ((mContent != null) && (!mContent.isEmpty() && mContent != content)) {
			/* ɨ�赽���ַ���ΪmContent��ֵ */
			// Toast toast = Toast.makeText(this, mContent, Toast.LENGTH_LONG);
			// toast.show();
			mVibrator.vibrate(200);
			List<Comm> resultlist = new ArrayList<Comm>();
			resultlist = (MyDBHelper.getInstance(mContext))
					.getCommby_bar(mContent);
			if (resultlist.size() > 0) {
				String commname = resultlist.get(0).getF_COMM_NAME();
				String commcode = resultlist.get(0).getF_COMM_CODE();

				UtilDao = new UtilDao(mContext);
				if (!UtilDao.haveOrderDetial(customcode, commcode)) {
					// ��������¶���
					String orderid = UtilDao
							.addOneOrder(customcode, personcode);
					// ���Ӷ�����Ʒ
					OrderDetail odts = UtilDao.addOneGoods(orderid, commname,
							commcode);
					tempog.setF_ORDER_NO(odts.getF_ORDER_NO());
					tempog.setF_COMM_CODE(odts.getF_COMM_CODE());
					tempog.setF_COMM_NAME(odts.getF_COMM_NAME());
					tempog.setF_SELL_ATM(odts.getF_SELL_ATM());

					refreshHandler.sendEmptyMessage(0);
				} else {
					Toast toast = Toast.makeText(this, commname + "�����",
							Toast.LENGTH_LONG);
					toast.show();
				}

			}

		} else {
			// logger.debug("Error:" + mContent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.option_menu, menu);
		menu.add(Menu.NONE, 0, Menu.NONE, "ɨ�������豸");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			try {
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) {
					// Get the device MAC address
					String address = data.getExtras().getString(
							DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					// Get the BLuetoothDevice object
					if (BluetoothAdapter.checkBluetoothAddress(address)) {
						BluetoothDevice device = mBluetoothAdapter
								.getRemoteDevice(address);
						// Attempt to connect to the device
						DEVICE_ID = address;
						mService.connect(device);
					}
				}
			} catch (Exception e) {
				Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a session
				// init();
			} else {
				// User did not enable Bluetooth or an error occured
				Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return true;
	}

	// ����������ӡ���ļ�������״̬
	public static final int MESSAGE_STATE_CHANGE = 3;
	public static final int MESSAGE_READ = 4;
	public static final int MESSAGE_WRITE = 5;
	public static final int MESSAGE_DEVICE_NAME = 6;
	public static final int MESSAGE_TOAST = 7;

	public static final String DEVICE_NAME = "device_name";
	public static String DEVICE_ID = "";
	public static final String TOAST = "toast";

	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	private static String mConnectedDeviceName = null;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothService mService = null;

	public static Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				//
				String str = (String) msg.obj;
				if (oractivity.sendMessage_(str)) {
					// ɾ�����ض���
					oractivity.clear();
				}
				break;

			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					ordertext.setText("�����Ӵ�ӡ�豸");
					if (AllOfOrder_Activity.btn_start_print != null)
						AllOfOrder_Activity.btn_start_print
								.setText("���ӳɹ�,�����ӡ");
					// �����ӡ�豸��Ϣ
					((MyApplication) oractivity.getApplication())
							.Savelocaldata("print_device", DEVICE_ID);

					break;
				case BluetoothService.STATE_CONNECTING:
					ordertext.setText("�������Ӵ�ӡ�豸��");
					if (AllOfOrder_Activity.btn_start_print != null)
						AllOfOrder_Activity.btn_start_print
								.setText("�������Ӵ�ӡ�豸��");
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					ordertext.setText("δ���ӵ���ӡ�豸");
					if (AllOfOrder_Activity.btn_start_print != null)
						AllOfOrder_Activity.btn_start_print.setText("δ���ӵ���ӡ�豸");
					break;
				}
				break;
			case MESSAGE_WRITE:
				break;
			case MESSAGE_READ:
				break;
			case MESSAGE_DEVICE_NAME:
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(oractivity.getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(oractivity.getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	public void clear() {
		UtilDao.ClearLocalOrder(customcode);
	}

	public boolean sendMessage_(String message) {
		// Check that we're actually connected before trying anything
		if (mService.getState() != BluetoothService.STATE_CONNECTED) {
			// Toast.makeText(this, "δ���Ӵ�ӡ��,���ɴ�ӡ", Toast.LENGTH_SHORT).show();
			if (!((MyApplication) getApplication()).Getlocaldata().print_device
					.equalsIgnoreCase("")) {
				String address = ((MyApplication) getApplication())
						.Getlocaldata().print_device;
				if (BluetoothAdapter.checkBluetoothAddress(address)) {
					mService.connect(mBluetoothAdapter.getRemoteDevice(address));
				}
			} else {
				Toast.makeText(this, "����˵���->ɨ�� �����豸��", Toast.LENGTH_SHORT)
						.show();
			}

			return false;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothService to write
			byte[] send;
			try {
				send = message.getBytes("GBK");
			} catch (UnsupportedEncodingException e) {
				send = message.getBytes();
			}
			mService.write(send);
			// if (AllOfOrder_Activity.btn_start_print != null) {
			// AllOfOrder_Activity.btn_start_print.setClickable(false);
			// }
		}
		return true;
	}
}
