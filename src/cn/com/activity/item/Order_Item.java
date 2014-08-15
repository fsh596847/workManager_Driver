package cn.com.activity.item;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.utils.ExitApplication;

import com.orwlw.WorkManager_Driver.R;
import com.orwlw.activity.EditNum_PriceDialog;
import com.orwlw.activity.Order_item_Activity;
import com.orwlw.bean.OrderDetail;
import com.orwlw.bean.OrderGoods;
import com.orwlw.dao.MyDBHelper;
import com.orwlw.dao.UtilDao;

/**
 * 订单详细
 * 
 * @author FreeSoul
 * 
 */

public class Order_Item extends LinearLayout implements OnClickListener {

	private View view;
	private TextView name;// 商品名称
	public TextView Quantity;// 商品大单位
	public TextView Sell_ATM;// 商品小计
	private ImageView imageView;
	private RelativeLayout layout2;
	private RelativeLayout layout3;
	private String orderno;
	private String commcode;
	private int status;
	private UtilDao UtilDao;
	private ExitApplication ExitApplication;
	private List<OrderGoods> list_orderGoods;
	private boolean statues = false;
	private Context mcontext;
	OrderGoods tempog;

	Handler refreshHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			ExitApplication = ExitApplication.getInstance();
			super.handleMessage(msg);
		}

	};

	public Order_Item(final Context context) {
		super(context);
		mcontext = context;
		view = LayoutInflater.from(context).inflate(R.layout.order_list_item,
				this);
		name = (TextView) view.findViewById(R.id.Commodity_name);

		Quantity = (TextView) view.findViewById(R.id.et_Quantity);// 商品大单位
		Quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
		Quantity.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (Quantity.hasFocus() == false) {
					// 编辑框失去焦点后 保存数量和状态
					// SaveOrderDetail();
				}
			}
		});

		Sell_ATM = (TextView) view.findViewById(R.id.et_price);// 单位价格

		Sell_ATM.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (Sell_ATM.hasFocus() == false) {
					// 编辑框失去焦点后 保存
					// SaveOrderDetail();
				}
			}
		});

		imageView = (ImageView) view.findViewById(R.id.Commodity_image);
		layout2 = (RelativeLayout) view.findViewById(R.id.layout2);
		layout3 = (RelativeLayout) view.findViewById(R.id.layout3);
		layout3.setOnClickListener(this);
		layout2.setOnClickListener(this);
		layout2.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Dialog dialog = new AlertDialog.Builder(context)
						.setMessage("删除该商品")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										MyDBHelper.getInstance(context)
												.deleteOrderDetail(orderno,
														commcode);
										context.sendBroadcast(new Intent(
												Order_item_Activity.ACTION));
									}

								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.cancel();
									}
								}).create();
				dialog.show();
				return false;
			}
		});

	}

	/**
	 * 点击事件，点击下拉，再次点击收回
	 */
	@Override
	public void onClick(View v) {

		if (layout2 == v) {
			// if (statues == false) {
			// imageView.setImageResource(R.drawable.ic_right_normal);
			// layout3.setVisibility(View.GONE);
			// statues = true;
			// } else {
			// imageView.setImageResource(R.drawable.ic_down_normal);
			// layout3.setVisibility(View.VISIBLE);
			// statues = false;
			// }
		}
		if (layout3 == v) {

			if (tempog.getF_COMM_STATUS() == 0) {
				EditNum_PriceDialog dialog = new EditNum_PriceDialog(mcontext,
						tempog);
				dialog.show();
			} else {
				Toast toast = Toast.makeText(mcontext, "已提交单据请打印",
						Toast.LENGTH_LONG);
				toast.show();
			}

		}
	}

	/**
	 * 设置订单属性
	 * 
	 * @param orderGoods
	 *            shop
	 */

	public void set(OrderGoods orderGoods) {
		tempog = orderGoods;
		name.setText(orderGoods.getF_COMM_NAME());

		String jiantext = (orderGoods.getF_NUM1() == 0 ? "" : orderGoods
				.getF_NUM1() + "");
		Quantity.setText(jiantext);

		Sell_ATM.setText(orderGoods.getF_SELL_ATM());

		orderno = orderGoods.getF_ORDER_NO();
		commcode = orderGoods.getF_COMM_CODE();
		status = orderGoods.getF_COMM_STATUS();
		if (status == 1) {
		}

		imageView.setImageResource(R.drawable.ic_right_normal);
	}

	/* 保存订单明细 */
	void SaveOrderDetail() {
		int commda = Integer.parseInt(Quantity.getText().toString()
				.equalsIgnoreCase("") ? "0" : Quantity.getText().toString());

		String atm = Sell_ATM.getText().toString().equalsIgnoreCase("") ? ""
				: Sell_ATM.getText().toString();
		OrderDetail odtl = new OrderDetail();
		odtl.setF_COMM_NAME(name.getText().toString());
		odtl.setF_NUM1(commda);
		odtl.setF_COMM_STATUS(2);
		odtl.setF_SELL_ATM(atm);
		(MyDBHelper.getInstance(mcontext)).updateOrderDetail(odtl, orderno,
				commcode);
	}

}
