package cn.com.adapter;

import java.util.List;

import android.content.Context;

import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.orwlw.WorkManager_Driver.R;
import com.orwlw.bean.Custom;
import com.orwlw.dao.UtilDao;

public class Shop_itemAdapter extends BaseAdapter {
	private List<Custom> list;
	private Context context;
	private UtilDao utildao;

	private Handler handler;

	public Shop_itemAdapter(List<Custom> list, Context context, Handler handler) {
		this.list = list;
		this.context = context;

		utildao = new UtilDao(context);

		this.handler = handler;

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
		ImageView imageView;
		TextView address;
		ImageView biaoji = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.detaile_order, null);
			name = (TextView) convertView.findViewById(R.id.tv_name);
			imageView = (ImageView) convertView
					.findViewById(R.id.image_myExam_infoIcon);
			address = (TextView) convertView
					.findViewById(R.id.text_myExam_content);
			biaoji = (ImageView) convertView
					.findViewById(R.id.image_myExam_tringle);
			ViewCache cache = new ViewCache(name, imageView, address, biaoji);
			Custom custom1 = new Custom();
			custom1 = list.get(position);
			// boolean a = utildao.getBitmapByCustom(custom1, imageView,
			// handler);
			// if (a == true) {
			cache.name = name;
			cache.imageView = imageView;
			cache.address = address;
			convertView.setTag(cache);
			// }
		} else {
			ViewCache cache = (ViewCache) convertView.getTag();
			name = cache.name;
			imageView = cache.imageView;
			address = cache.address;
			biaoji = cache.biaoji;
		}
		Custom weather1 = list.get(position);
		name.setText(weather1.getF_CUSTOM_NAME());
		address.setText(weather1.getF_ADDRESS());
		/* 1是订单，2是非订单 */
		if (weather1.getF_STATUS() == "1") {
			biaoji.setImageResource(R.drawable.ic_right_selected);
		} else if (weather1.getF_STATUS() == "2") {
			biaoji.setImageResource(R.drawable.ic_right_normal);
		}
		return convertView;
	}

	final class ViewCache {
		TextView name;
		TextView address;
		ImageView imageView;
		ImageView biaoji;

		public ViewCache(TextView name, ImageView imageView, TextView address,
				ImageView biaoji) {
			this.name = name;
			this.imageView = imageView;
			this.address = address;
			this.biaoji = biaoji;
		}
	}
}