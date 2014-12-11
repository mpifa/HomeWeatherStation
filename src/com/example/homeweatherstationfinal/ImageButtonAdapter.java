package com.example.homeweatherstationfinal;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageButtonAdapter extends BaseAdapter {

	private List<Item> sensorLst;
	private Context mContext;

	public ImageButtonAdapter(Context applicationContext,
			List<Item> sensorLst) {
		this.sensorLst = sensorLst;
		this.mContext = applicationContext;

	}

	@Override
	public int getCount() {
		return sensorLst.size();
	}

	@Override
	public Object getItem(int position) {
		return sensorLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return sensorLst.get(position).getDrawableID();
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {

		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(sensorLst.get(position).getDrawableID());
		return imageView;
	}
}