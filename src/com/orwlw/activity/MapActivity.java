package com.orwlw.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.utils.BMapUtil;
import cn.com.utils.ExitApplication;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.Custom;
import com.orwlw.dao.MyDBHelper;

public class MapActivity extends Activity {
	BMapManager mBMapMan = null;
	MapView mMapView = null;

	private MapController mMapController = null;
	private MyOverlay mOverlay = null;
	private PopupOverlay pop = null;
	private ArrayList<OverlayItem> mItems = null;
	private TextView popupText = null;
	private View viewCache = null;
	private View popupInfo = null;
	private View popupLeft = null;
	private View popupRight = null;
	private Button button = null;
	private ExitApplication instance;
	private OverlayItem mCurItem = null;
	/* ʵ������ */
	private List<Custom> list_custom = null;

	private MyDBHelper myDBHelper;
	String AllorNo;
	String methodName = "Driver_GetCustomList";

	/**
	 * overlay λ������ 112.591716,37.866353
	 */

	double mLon5 = 112.591716;
	double mLat5 = 37.866353;

	// ��λ���
	LocationClient mLocClient;
	LocationData locData = new LocationData();

	public MyLocationListenner myListener = new MyLocationListenner();
	// ��λͼ��
	MyLocationOverlay myLocationOverlay = null;

	// UI���
	Button requestLocButton = null;
	boolean isRequest = false;// �Ƿ��ֶ���������λ
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	boolean isLocationClientStop = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(MapActivity.this);
		mBMapMan.init("2BC7F319416C6B880C3808E7F4E85CE96ADFE4B6", null);

		// ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��
		setContentView(R.layout.map_main);
		instance = ExitApplication.getInstance();
		instance.addActivity(this);
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		// ��λͼ���ʼ��
		myLocationOverlay = new MyLocationOverlay(mMapView);
		// ���ö�λ����
		myLocationOverlay.setData(locData);
		// ��Ӷ�λͼ��
		mMapView.getOverlays().add(myLocationOverlay);

		myLocationOverlay.enableCompass();
		// �޸Ķ�λ���ݺ�ˢ��ͼ����Ч
		mMapView.refresh();
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(14);
		// ��λ��ʼ��
		myListener = new MyLocationListenner();
		mLocClient = new LocationClient(getApplicationContext());
		setLocationOption();
		mLocClient.registerLocationListener(myListener);
		mLocClient.start();
		// ��λͼ���ʼ��
		myLocationOverlay = new MyLocationOverlay(mMapView);

		MapController mMapController = mMapView.getController();
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));
		mMapController.setCenter(point);// ���õ�ͼ���ĵ�
		mMapController.setZoom(17);// ���õ�ͼzoom����
		get_list();
		init();

	}

	// ������ز���
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setAddrType("all"); // ���õ�ַ��Ϣ��������Ϊ��all��ʱ�е�ַ��Ϣ��Ĭ���޵�ַ��Ϣ

		option.setScanSpan(10000); // ���ö�λģʽ��С��1����һ�ζ�λ;���ڵ���1����ʱ��λ
		mLocClient.setLocOption(option);
	}

	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(MapActivity.this, "���ڶ�λ��", Toast.LENGTH_SHORT).show();
	}
	/* ��ʼ�� */
	private void init() {

//		initOverlay(list_custom);

		/**
		 * �趨��ͼ���ĵ� 112.568289,37.85906.
		 */
		GeoPoint p = new GeoPoint((int) (mLat5 * 1E6), (int) (mLon5 * 1E6));
		mMapController.setCenter(p);

	}

	/*
	 * ������ʾlist
	 */
//	private void update_list() {
//		if (AllorNo == "1") {
//			mOverlay.removeAll();
//			initOverlay(list_custom);
//			if (pop != null) {
//				pop.hidePop();
//			}
//
//			mMapView.refresh();
//		} else if (AllorNo == "2") {
//			list_custom = myDBHelper.getCustomByStatus(AllorNo);
//			System.out.println(list_custom + "-------------");
//			mOverlay.removeAll();
//			initOverlay(list_custom);
//			if (pop != null) {
//				pop.hidePop();
//			}
//
//			mMapView.refresh();
//
//		}
//
//	}

	/*
	 * �����ݿ��ȡ������list
	 */
	private void get_list() {
		/**
		 * ׼��overlay ����
		 */
		HashMap<String, String> propertys = new HashMap<String, String>();
		propertys.put("personno", "");
		myDBHelper = MyDBHelper.getInstance(MapActivity.this);
//		list_custom = myDBHelper.getAllCustom();

	}

	public void initOverlay(List<Custom> list) {

		/**
		 * �����Զ���overlay
		 */
		mOverlay = new MyOverlay(getResources().getDrawable(
				R.drawable.icon_gcoding), mMapView);
		int size = list.size();
		GeoPoint p1=null;
		for (int i = 0; i < size; i++) {
			try{
				p1 = new GeoPoint((int) (Float.parseFloat(list.get(i).getF_LAT()) * 1E6),
						(int) (Float.parseFloat(list.get(i).getF_LON()) * 1E6));
				
			}catch(Exception e){
				
			}
			OverlayItem item1;
			if(p1!=null){
				item1 = new OverlayItem(p1, list.get(i)
						.getF_CUSTOM_NAME(), "");
				/**
				 * ����overlayͼ�꣬�粻���ã���ʹ�ô���ItemizedOverlayʱ��Ĭ��ͼ��.
				 */
				item1.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
				/**
				 * ��item ��ӵ�overlay�� ע�⣺ ͬһ��itmeֻ��addһ��
				 */
				mOverlay.addItem(item1);
			}
			 
		}
		/**
		 * ��������item���Ա�overlay��reset���������
		 */
		mItems = new ArrayList<OverlayItem>();
		mItems.addAll(mOverlay.getAllItem());
		/**
		 * ��overlay �����MapView��
		 */
		mMapView.getOverlays().add(mOverlay);
		/**
		 * ˢ�µ�ͼ
		 */
		mMapView.refresh();

		/**
		 * ���ͼ����Զ���View.
		 */

		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupInfo = (View) viewCache.findViewById(R.id.popinfo);
		popupLeft = (View) viewCache.findViewById(R.id.popleft);
		popupRight = (View) viewCache.findViewById(R.id.popright);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);

		button = new Button(this);
		button.setBackgroundResource(R.drawable.popup);

		/**
		 * ����һ��popupoverlay
		 */
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				if (index == 0) {
					// ����itemλ��
					pop.hidePop();
					GeoPoint p = new GeoPoint(mCurItem.getPoint()
							.getLatitudeE6() + 5000, mCurItem.getPoint()
							.getLongitudeE6() + 5000);
					mCurItem.setGeoPoint(p);
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				} else if (index == 2) {
					// ����ͼ��
					mCurItem.setMarker(getResources().getDrawable(
							R.drawable.nav_turn_via_1));
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				} else if (index == 1) {
//					String customcode = list_custom.get(a).getF_CUSTOM_CODE();
//					instance = ExitApplication.getInstance();
//					instance.addActivity(getParent());
//					instance.setcustomcode(customcode);
//					String customname = list_custom.get(a).getF_CUSTOM_NAME();
//					Intent intent = new Intent(MapActivity.this,
//							Order_Activity.class).putExtra("customcode",
//							customcode).putExtra("customname", customname);
//					startActivity(intent);
				}
			}
		};
		pop = new PopupOverlay(mMapView, popListener);
	}

	/**
	 * ��������������λ�õ�ʱ�򣬸�ʽ�����ַ������������Ļ��
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || isLocationClientStop)
				return;
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// �������ʾ��λ����Ȧ����accuracy��ֵΪ0����
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			// ���¶�λ����
			myLocationOverlay.setData(locData);
			mMapView.getOverlays().remove(myLocationOverlay);
			mMapView.getOverlays().add(myLocationOverlay);
			System.out.println("==========" + locData.latitude);
			// ����ͼ������ִ��ˢ�º���Ч
			mMapView.refresh();

			// ���ֶ�����������״ζ�λʱ���ƶ�����λ��
			if (isRequest || isFirstLoc) {
				// �ƶ���ͼ����λ��
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				isRequest = false;
			}
			// �״ζ�λ���
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	@Override
	protected void onPause() {
		isLocationClientStop = true;
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onStart() {
		AllorNo = getIntent().getStringExtra("AllorNo");
		System.out.println(AllorNo + "----------------------------------");
//		update_list();
		super.onStart();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView������������Activityͬ������activity�ָ�ʱ�����MapView.onResume()
		 */
		/*
		 * ��ͼ����δ���Ϳͻ�����ȫ���ͻ�,��ô�������ֵ
		 */
		isLocationClientStop = false;
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		if (mLocClient != null)
			mLocClient.stop();
		isLocationClientStop = true;
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	int a;

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			OverlayItem item = getItem(index);
			mCurItem = item;
			a = index;
			System.out.println("a=" + a);
			popupText.setText(getItem(index).getTitle());
			Bitmap[] bitMaps = { BMapUtil.getBitmapFromView(popupLeft),
					BMapUtil.getBitmapFromView(popupInfo),
					BMapUtil.getBitmapFromView(popupRight) };
			pop.showPopup(bitMaps, item.getPoint(), 32);

			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (pop != null) {
				pop.hidePop();
				mMapView.removeView(button);
			}
			return false;
		}

	}

}
