package cn.com.test;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.Custom;
import com.orwlw.bean.Order;
import com.orwlw.bean.OrderDetail;
import com.orwlw.bean.OrderGoods;
import com.orwlw.dao.MyDBHelper;
import com.orwlw.dao.UtilDao;
import com.orwlw.net.ConvertToBean;

public class MainActivity extends Activity {

	MyDBHelper myDBHelper;
	UtilDao utilDao;
	ConvertToBean toBean;
	
	TextView msg_Custom;
	TextView msg_order;
	TextView msg_order_detail;
	TextView msg_customorder_detail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		myDBHelper=MyDBHelper.getInstance(this);
		utilDao=new UtilDao(this);
		
		TextView ceshi=(TextView)findViewById(R.id.ceshi);
		msg_Custom=(TextView)findViewById(R.id.msg_custom);
		msg_order=(TextView)findViewById(R.id.msg_order);
		msg_order_detail=(TextView)findViewById(R.id.msg_order_detail);
		msg_customorder_detail=(TextView)findViewById(R.id.msg_customorder_detail);
		toBean=new ConvertToBean(MainActivity.this);
		
		ceshi.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
								
				String methodName="Driver_GetCustomList";
				String methodName1="Driver_GetCustomOrder";
				String methodName2="Driver_GetCustomOrderDetial";
				HashMap<String,String> propertys=new HashMap<String, String>();
				propertys.put("personno", "");
				
				
				toBean.getAllCustom(methodName,propertys);
				toBean.getAllOrder(methodName1, propertys);
				toBean.getAllOrderDetail(methodName2, propertys);
				
				
			}
		
		});
		
		TextView custom=(TextView)findViewById(R.id.custom);
		custom.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("获取客户信息+++++++");
				List<Custom> customs=myDBHelper.getAllCustom();
				String msg="";
				for(Custom custom:customs){
				msg+="客户编码:"+custom.getF_CUSTOM_CODE()+"\n"
							+"客户名称:"+custom.getF_CUSTOM_NAME()+"\n"
							+"联系人:"+custom.getF_CONTACT_PERSON()+"\n"
							+"地址:"+custom.getF_ADDRESS()+"\n"
							+"电话:"+custom.getF_TEL()+"\n"
							+"经度:"+custom.getF_LON()+"\n"
							+"纬度:"+custom.getF_LAT()+"\n"
							+"类型:"+custom.getF_STATUS()+"\n";
				}
				System.out.println(msg);
				msg_Custom.setText(msg);
				
				
			}
		
		});
		
		
		
		
		
		TextView customorder_detail=(TextView)findViewById(R.id.customorder_detail);
		customorder_detail.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("获取信息+++++++");
				
				
				List<OrderGoods> orderGoods=utilDao.getGoodsByCustom("ouran26002");
				String msg="";
				for(OrderGoods orderGood:orderGoods){
					msg+="商品名称:"+orderGood.getF_COMM_NAME()+"\n"
							+"数量（件）:"+orderGood.getF_NUM1()+"\n"
							+"数量（个）:"+orderGood.getF_NUM2()+"\n"
							+"下单人:"+orderGood.getF_ORDER_PERSON()+"\n"
							+"订单号:"+orderGood.getF_ORDER_NO()+"\n"
							+"下单时间:"+orderGood.getF_DATE()+"\n"
							+"商品编码:"+orderGood.getF_COMM_CODE()+"\n"
							;
				}
				System.out.println(msg);
				msg_customorder_detail.setText(msg);
				
				
			}
		
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
