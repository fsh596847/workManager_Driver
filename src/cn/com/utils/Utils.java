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
		builder.setMessage("ȷ��Ҫ�˳���?");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ExitApplication.getInstance().exit();
					}

				});

		builder.setNegativeButton("ȡ��",
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
		/* Math.PI:���κ�����ֵ�����ӽ� pi����Բ���ܳ���ֱ��֮�ȣ��� double ֵ�� */
		double radLat1 = lat1 * Math.PI / 180.0;
		double radLat2 = lat2 * Math.PI / 180.0;
		double a = radLat1 - radLat2;
		double b = (lng1 * Math.PI / 180.0) - (lng2 * Math.PI / 180.0);
		/*
		 * asin:����һ��ֵ�ķ����ң����صĽǶȷ�Χ�� -pi/2 �� pi/2 ֮�䡣 sqrt:������ȷ����� double ֵ����ƽ������
		 * cos:���ؽǵ��������ҡ� pow:���ص�һ�������ĵڶ����������ݵ�ֵ��
		 */
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137;
		/*
		 * int round(float a):a - Ҫ����Ϊ�����ĸ���ֵ�� ���أ� ����Ϊ��ӽ��� int ֵ�Ĳ���ֵ��
		 */
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	//С���㱣��1λ
	public static double GetdoubleNumber(double dou) {
		double s = dou;
		BigDecimal b = new BigDecimal(s);
		s = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		return s;
	}

	/**
	 * ���ݾ�������ͻ�
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

	public static boolean checkNet(Context context) {// ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ���
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// ��ȡ�������ӹ���Ķ���
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// �жϵ�ǰ�����Ƿ��Ѿ�����
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

	// ��ȡ�汾��֤��ַ
	public static String getUpgradeURL(String app, String filename) {
		String urlString = String.format("http://%s.orwlw.com/%s", app,
				filename).toString();
		return urlString;
	}

	public static final String UPDATE_SAVENAME = "WorkManager_Driver.apk";
	public static final String UPDATE_VERJSON = "ver_driver.txt";
	public static final String APP = "ouran";

	// ��ȡ����ʹ�õ�����İ汾��
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
