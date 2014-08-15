package cn.com.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.orwlw.bean.Comm;

public class Shop_detailesAdapter extends BaseAdapter {
	private List<Comm> list;
	private Context context;

	public Shop_detailesAdapter(List<Comm> list, Context context) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView name;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					com.orwlw.WorkManager_Driver.R.layout.orderdetial, null);
			name = (TextView) convertView
					.findViewById(com.orwlw.WorkManager_Driver.R.id.detailes);
			ViewCache cache = new ViewCache();
			cache.name = name;
			convertView.setTag(cache);

		} else {
			ViewCache cache = (ViewCache) convertView.getTag();
			name = cache.name;

		}
		Comm weather1 = list.get(position);
		name.setText(weather1.getF_COMM_NAME() );

		return convertView;
	}

	private final class ViewCache {

		TextView name;

	}

}
