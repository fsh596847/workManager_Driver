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
	/* 实际数据 */
	private List<Custom> list_custom = null;

	private MyDBHelper myDBHelper;
	String AllorNo;
	String methodName = "Driver_GetCustomList";

	/**
	 * overlay 位置坐标 112.591716,37.866353
	 */

	double mLon5 = 112.591716;
	double mLat5 = 37.866353;

	// 定位相关
	LocationClient mLocClient;
	LocationData locData = new LocationData();

	public MyLocationListenner myListener = new MyLocationListenner();
	// 定位图层
	MyLocationOverlay myLocationOverlay = null;

	// UI相关
	Button requestLocButton = null;
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位
	boolean isLocationClientStop = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(MapActivity.this);
		mBMapMan.init("2BC7F319416C6B880C3808E7F4E85CE96ADFE4B6", null);

		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.map_main);
		instance = ExitApplication.getInstance();
		instance.addActivity(this);
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		// 定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		// 设置定位数据
		myLocationOverlay.setData(locData);
		// 添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);

		myLocationOverlay.enableCompass();
		// 修改定位数据后刷新图层生效
		mMapView.refresh();
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(14);
		// 定位初始化
		myListener = new MyLocationListenner();
		mLocClient = new LocationClient(getApplicationContext());
		setLocationOption();
		mLocClient.registerLocationListener(myListener);
		mLocClient.start();
		// 定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);

		MapController mMapController = mMapView.getController();
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(17);// 设置地图zoom级别
		get_list();
		init();

	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setAddrType("all"); // 设置地址信息，仅设置为“all”时有地址信息，默认无地址信息

		option.setScanSpan(10000); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		mLocClient.setLocOption(option);
	}

	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(MapActivity.this, "正在定位…", Toast.LENGTH_SHORT).show();
	}
	/* 初始化 */
	private void init() {

//		initOverlay(list_custom);

		/**
		 * 设定地图中心点 112.568289,37.85906.
		 */
		GeoPoint p = new GeoPoint((int) (mLat5 * 1E6), (int) (mLon5 * 1E6));
		mMapController.setCenter(p);

	}

	/*
	 * 更新显示list
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
	 * 从数据库获取完整的list
	 */
	private void get_list() {
		/**
		 * 准备overlay 数据
		 */
		HashMap<String, String> propertys = new HashMap<String, String>();
		propertys.put("personno", "");
		myDBHelper = MyDBHelper.getInstance(MapActivity.this);
//		list_custom = myDBHelper.getAllCustom();

	}

	public void initOverlay(List<Custom> list) {

		/**
		 * 创建自定义overlay
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
				 * 设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
				 */
				item1.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
				/**
				 * 将item 添加到overlay中 注意： 同一个itme只能add一次
				 */
				mOverlay.addItem(item1);
			}
			 
		}
		/**
		 * 保存所有item，以便overlay在reset后重新添加
		 */
		mItems = new ArrayList<OverlayItem>();
		mItems.addAll(mOverlay.getAllItem());
		/**
		 * 将overlay 添加至MapView中
		 */
		mMapView.getOverlays().add(mOverlay);
		/**
		 * 刷新地图
		 */
		mMapView.refresh();

		/**
		 * 向地图添加自定义View.
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
		 * 创建一个popupoverlay
		 */
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				if (index == 0) {
					// 更新item位置
					pop.hidePop();
					GeoPoint p = new GeoPoint(mCurItem.getPoint()
							.getLatitudeE6() + 5000, mCurItem.getPoint()
							.getLongitudeE6() + 5000);
					mCurItem.setGeoPoint(p);
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				} else if (index == 2) {
					// 更新图标
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
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || isLocationClientStop)
				return;
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			// 更新定位数据
			myLocationOverlay.setData(locData);
			mMapView.getOverlays().remove(myLocationOverlay);
			mMapView.getOverlays().add(myLocationOverlay);
			System.out.println("==========" + locData.latitude);
			// 更新图层数据执行刷新后生效
			mMapView.refresh();

			// 是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc) {
				// 移动地图到定位点
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				isRequest = false;
			}
			// 首次定位完成
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
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		/*
		 * 地图更新未配送客户，和全部客户,获得传过来的值
		 */
		isLocationClientStop = false;
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
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
