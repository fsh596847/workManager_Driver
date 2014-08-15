package com.orwlw.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.utils.NetworkTool;
import cn.com.utils.Utils;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;

public class Checkserver extends Service {

	private int newVerCode = 0;
	private String newVerName = "";
	private String newUpdateShow = "";
	private String Updatetime = "";
	private String StartWorktime = "";
	private String EndWorktime = "";
	private String Space = "";
	/** 所有的APN记录。 */
	private static final Uri ALL_APN_URI = Uri

	.parse("content://telephony/carriers");

	/** 当前default的APN记录。 */

	private static final Uri PREFERRED_APN_URI = Uri

	.parse("content://telephony/carriers/preferapn");

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//android.os.Debug.waitForDebugger();
		new Thread() {
			public void run() {
				Looper.prepare();

			
				// 修改apn
				String currentapnString = GetCurrentAPN();
				int in = currentapnString.indexOf("wap");
				if (in != -1) {
					String apn_id = Getnetapninfo();
					if (!apn_id.equalsIgnoreCase("")) {
						if (updateapn(apn_id)) {
							MyApplication.WriteLog("修改接入点为net成功");
						}
					}
				} else {
					MyApplication.WriteLog("当前为net不用修改");
				}

				// 监测升级版本
				checkverson();

				Looper.loop();
			};
		}.start();

		return Service.START_STICKY;
	}

	private String GetCurrentAPN() {
		ContentResolver contentResolver = getContentResolver();

		Cursor cursor = contentResolver.query(PREFERRED_APN_URI, null, null,
				null, null);
		String apnvalue = "";
		if (cursor != null) {

			String key = "";
			while (cursor.moveToNext()) {
				key = "apn";
				apnvalue = cursor.getString(cursor.getColumnIndex(key));
			}
			cursor.close();

		}
		return apnvalue;
	}

	private String Getnetapninfo() {
		try {
			ContentResolver contentResolver = getContentResolver();

			Cursor cursor = contentResolver.query(ALL_APN_URI, null, null,
					null, null);

			if (cursor != null) {
				String key = "";
				String key2 = "";
				String key3 = "";
				while (cursor.moveToNext()) {
					key2 = "current";
					int id = cursor.getShort(cursor.getColumnIndex(key2));
					if (id == 1) {
						key3 = "apn";
						String value = cursor.getString(cursor
								.getColumnIndex(key3));
						if (value.indexOf("net") != -1) {
							key = "_id";
							String apn_id = cursor.getString(cursor
									.getColumnIndex(key));
							return apn_id;
						}
					}
				}
				cursor.close();
			}
		} catch (Exception e) {
			// Log.i("GetnetAPNinfo_ERROR", e.getMessage() + "");
		}

		return "";
	}

	public boolean updateapn(String apn_id) {
		try {
			ContentResolver resolver = getContentResolver();
			ContentValues values = new ContentValues();
			values.put("apn_id", apn_id);
			if (resolver.update(PREFERRED_APN_URI, values, null, null) > 0) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			// Log.i("updateAPN_ERROR", e.getMessage() + "");
			return false;

		}

	}

	// 检验版本
	void checkverson() {
		if (getServerVerCode()) {
			int vercode = Utils.getVerCode(this);
			MyApplication.WriteLog("本地版本" + vercode);
			// newVerCode = 26;
			if (newVerCode > vercode) {
				doNewVersionUpdate();
			} else {
				stopSelf();
			}
		}
	}

	// 获取服务器上新的版本号和名称信息
	private boolean getServerVerCode() {
		try {
			String verjson = NetworkTool.getContent(Utils.getUpgradeURL(
					Utils.APP, Utils.UPDATE_VERJSON));
			verjson = "[" + verjson + "]";

			JSONArray array = new JSONArray(verjson);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				try {
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					newVerName = obj.getString("verName");
					newUpdateShow = obj.getString("newUpdate");
					Updatetime = obj.getString("updatetime");
					StartWorktime = obj.getString("startworktime");
					EndWorktime = obj.getString("endworktime");
					Space = obj.getString("space");

					((MyApplication) getApplication()).Savelocaldata("Space",
							Space);
					((MyApplication) getApplication()).Savelocaldata(
							"StartWorktime", StartWorktime);
					((MyApplication) getApplication()).Savelocaldata(
							"EndWorktime", EndWorktime);

				} catch (Exception e) {
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// 有新版本提示
	private void doNewVersionUpdate() {
		try {
			Intent it = new Intent(this, UpgradeActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.putExtra("newVerName", newVerName);
			it.putExtra("newUpdateShow", newUpdateShow);
			it.putExtra("updatetime", Updatetime);
			startActivity(it);
			stopSelf();

		} catch (Exception e) {
			// Log.i("全局弹框错误", e.getMessage());
		}

	}

}
