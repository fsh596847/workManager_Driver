package com.orwlw.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.OrderDetail;
import com.orwlw.bean.OrderGoods;
import com.orwlw.dao.MyDBHelper;
import com.orwlw.dao.UtilDao;

public class EditNum_PriceDialog extends Dialog {

	private Context context;

	private TextView comm_name;
	private EditText edit_number;
	private EditText edit_price;
	private Button btn_ok;

	public List<EditText> editviews = new ArrayList<EditText>();
	OrderGoods tempog = null;

	private MyDBHelper dbHelper;
	private UtilDao UtilDao;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; // 需要自己定义标志

	private Handler handler;

	public EditNum_PriceDialog(final Context context, OrderGoods og) {
		super(context, R.style.dialog);
		this.context = context;
		tempog = og;
		dbHelper = MyDBHelper.getInstance(context);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED,
				FLAG_HOMEKEY_DISPATCHED);// 关键代码
		this.setContentView(com.orwlw.WorkManager_Driver.R.layout.edit_number_price);
		comm_name = (TextView) findViewById(R.id.textView1);
		edit_number = (EditText) findViewById(R.id.et_Quantity);
		edit_number.setInputType(InputType.TYPE_CLASS_NUMBER);
		edit_price = (EditText) findViewById(R.id.et_price);
		editviews.add(edit_number);
		editviews.add(edit_price);

		if (tempog != null) {
			comm_name.setText(tempog.getF_COMM_NAME() + "");
			edit_number.setText(tempog.getF_NUM1() == 0 ? "" : tempog
					.getF_NUM1() + "");
			edit_price.setText(tempog.getF_SELL_ATM().equals("0") ? "" : tempog
					.getF_SELL_ATM());
		}

		btn_ok = (Button) findViewById(R.id.tijiao);
		btn_ok.setOnClickListener(new OKListener());
	}

	private class OKListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			SaveOrderDetail();
			EditNum_PriceDialog.this.dismiss();
			context.sendBroadcast(new Intent(Order_item_Activity.ACTION));
		}
	}

	/* 保存订单明细 */
	void SaveOrderDetail() {
		int commda = Integer.parseInt(edit_number.getText().toString()
				.equalsIgnoreCase("") ? "0" : edit_number.getText().toString());
		String atm = edit_price.getText().toString().equalsIgnoreCase("") ? "0"
				: edit_price.getText().toString();
		OrderDetail odtl = new OrderDetail();
		odtl.setF_NUM1(commda);
		odtl.setF_SELL_ATM(atm);
		(MyDBHelper.getInstance(context)).updateOrderDetail(odtl,
				tempog.getF_ORDER_NO(), tempog.getF_COMM_CODE());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 18) {

			if (edit_price.hasFocus()) {
				edit_price.setText(edit_price.getText() + ".");
				CharSequence text = edit_price.getText();
				if (text instanceof Spannable) {
					Spannable spanText = (Spannable) text;
					Selection.setSelection(spanText, text.length());
				}
			}

			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				View focusview = getCurrentFocus();
				EditText ET = ((EditText) focusview);
				ET.setText("");
			} catch (Exception e) {

			}
			// Utils.dialog(Order_item_Activity.this);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			focusedit();
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			focusedit();
		}

		return true;
	}

	boolean flag = true;

	void focusedit() {
		if (flag) {
			editviews.get(1).requestFocus();
			flag = false;
		} else {
			editviews.get(0).requestFocus();
			flag = true;
		}

	}
}
