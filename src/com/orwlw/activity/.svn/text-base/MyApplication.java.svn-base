package com.orwlw.activity;

import com.orwlw.bean.Localdata;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application {
	public static boolean Debug = true;
	private BluetoothAdapter mBluetoothAdapter = null;	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
	}

	public Localdata Getlocaldata() {
		Localdata localdata = new Localdata();
		SharedPreferences localsharedata = getSharedPreferences("localdata", 0);
		localdata.personno = localsharedata.getString("personno", "");
		localdata.personname = localsharedata.getString("personname", "");

		localdata.lastlat = localsharedata.getString("lastlat", "0");
		localdata.lastlon = localsharedata.getString("lastlon", "0");
		localdata.current_custom_code = localsharedata.getString(
				"current_custom_code", "");
		localdata.current_custom_name = localsharedata.getString(
				"current_custom_name", "");

		localdata.print_device = localsharedata.getString("print_device", "");
		localdata.Space = localsharedata.getString("Space", "5");
		localdata.StartWorktime = localsharedata.getString("StartWorktime",
				"08:00");
		localdata.EndWorktime = localsharedata
				.getString("EndWorktime", "18:00");
		localdata.LAST_SYSNC_DATE = localsharedata.getString("LAST_SYSNC_DATE",
				"1990-01-01");
		
		return localdata;
	}

	public void Savelocaldata(String key, String value) {
		Editor localdata = getSharedPreferences("localdata", 0).edit();
		localdata.putString(key, value);
		localdata.commit();
	}

	public void SavelocalInt(String key, int value) {
		Editor localdata = getSharedPreferences("localdata", 0).edit();
		localdata.putInt(key, value);
		localdata.commit();
	}

	/**
	 *  ‰≥ˆ»’÷æ
	 */
	public static void WriteLog(String paramString) {
		if (Debug) {
			Log.e("Driver_log£∫", paramString);
		}
	}
}
