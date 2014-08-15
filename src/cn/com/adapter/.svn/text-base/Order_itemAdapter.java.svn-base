package cn.com.adapter;

import java.util.List;
import com.orwlw.bean.OrderGoods;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.activity.item.Order_Item;

public class Order_itemAdapter extends BaseAdapter {
	private List<OrderGoods> list;
	private Context context;
	boolean statues = false;

	public Order_itemAdapter(List<OrderGoods> list, Context context) {
		this.list = list;
		this.context = context;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = new Order_Item(context);
			holder.num1 = ((Order_Item) convertView).Quantity;			
			holder.sell_atm = ((Order_Item) convertView).Sell_ATM;

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		((Order_Item) convertView).set(list.get(position));
		return convertView;
	}

	private class Holder {
		TextView num1;		
		TextView sell_atm;
	}

}