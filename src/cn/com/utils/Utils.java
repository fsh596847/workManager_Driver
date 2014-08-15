package cn.com.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.orwlw.bean.Custom;

public class Utils {

	public static void dialog(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ExitApplication.getInstance().exit();
					}

				});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	public static double GetDistance(double lat1, double lng1, double lat2,
			double lng2) {
		/* Math.PI:比任何其他值都更接近 pi（即圆的周长与直径之比）的 double 值。 */
		double radLat1 = lat1 * Math.PI / 180.0;
		double radLat2 = lat2 * Math.PI / 180.0;
		double a = radLat1 - radLat2;
		double b = (lng1 * Math.PI / 180.0) - (lng2 * Math.PI / 180.0);
		/*
		 * asin:返回一个值的反正弦；返回的角度范围在 -pi/2 到 pi/2 之间。 sqrt:返回正确舍入的 double 值的正平方根。
		 * cos:返回角的三角余弦。 pow:返回第一个参数的第二个参数次幂的值。
		 */
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137;
		/*
		 * int round(float a):a - 要舍入为整数的浮点值。 返回： 舍入为最接近的 int 值的参数值。
		 */
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	//小数点保留1位
	public static double GetdoubleNumber(double dou) {
		double s = dou;
		BigDecimal b = new BigDecimal(s);
		s = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		return s;
	}

	/**
	 * 根据距离排序客户
	 * 
	 * @param customs
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static List<Custom> sortCustoms(List<Custom> customs, double lat,
			double lng) {

		HashMap<Integer, Custom> maps = new HashMap<Integer, Custom>();

		List<Custom> cus = new ArrayList<Custom>();
		for (Custom s : customs) {
			int temp = (int) GetDistance(Double.parseDouble(s.getF_LAT()),
					Double.parseDouble(s.getF_LON()), lat, lng);
			maps.put(temp, s);
		}

		List<Map.Entry<Integer, Custom>> infoIds = new ArrayList<Map.Entry<Integer, Custom>>(
				maps.entrySet());

		Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Custom>>() {

			@Override
			public int compare(Entry<Integer, Custom> o1,
					Entry<Integer, Custom> o2) {
				// TODO Auto-generated method stub
				return (o1.getKey() - o2.getKey());
			}
		});
		for (int i = 0; i < infoIds.size(); i++) {
			Entry<Integer, Custom> ent = infoIds.get(i);
			cus.add(ent.getValue());
		}

		return cus;
	}

	public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static String GetDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		return date;
	}

	public static String GetTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		return date;
	}

	public static String GetYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String date = sdf.format(new Date());
		return date;
	}

	public static String GetMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		String date = sdf.format(new Date());
		return date;
	}

	public static String GetDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		String date = sdf.format(new Date());
		return date;
	}

	// 获取版本验证地址
	public static String getUpgradeURL(String app, String filename) {
		String urlString = String.format("http://%s.orwlw.com/%s", app,
				filename).toString();
		return urlString;
	}

	public static final String UPDATE_SAVENAME = "WorkManager_Driver.apk";
	public static final String UPDATE_VERJSON = "ver_driver.txt";
	public static final String APP = "ouran";

	// 获取正在使用的软件的版本号
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"com.orwlw.WorkManager_Driver", 0).versionCode;
		} catch (NameNotFoundException e) {

		}
		return verCode;
	}

}
