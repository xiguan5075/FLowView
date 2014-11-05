package com.example.flowview;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ZBCVPAdapter extends PagerAdapter {

	private Context context;
	// ArrayList<View> views = new ArrayList<View>();
	private int[] img;

	public ZBCVPAdapter(Context context, int[] img) {
		this.context = context;
		this.img = img;
		// for (int i = 0; i < img.length; i++) {
		// View view =
		// LayoutInflater.from(context).inflate(R.layout.item_headpic, null,
		// false);
		// views.add(view);
		// }

		// application = (SuperLiveApplication) context.getApplicationContext();
	}

	@Override
	public int getCount() {
		if (img.length == 1) {
			return img.length;
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView(views.get(position % img.length));
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int pos = position % img.length;
		// View view = views.get(pos);
		View view = LayoutInflater.from(context).inflate(R.layout.item_headpic, null, false);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_head_pic);
		iv.setImageResource(img[pos]);
		// if (ImageUtils.isAbleToLoad(context)) {
		// ImageUtils.imageLoader.displayImage(info.getImageUrl(), iv,
		// ImageUtils.options);
		// } else {
		// iv.setOnClickListener(new LoadImageListener(info.getImageUrl(), iv));
		// }

		// iv.setOnClickListener(new MyOnClickListener(info));
		container.addView(view);
		return view;
	}

	// class MyOnClickListener implements OnClickListener {
	// private TuPianInfo info;

	// MyOnClickListener(TuPianInfo info) {
	// this.info = info;
	// }

	// @Override
	// public void onClick(View v) {

	// Intent intent = new Intent(context, SSXQFragmentActivity.class);
	//
	// MatchInfo matchInfo = new MatchInfo();
	// matchInfo.setId(info.getId());
	// matchInfo.setIsQuiz(info.getIsQuiz());
	// matchInfo.setMatchName(info.getMatchName());
	// matchInfo.setMatchInfoID(info.getMatchInfoID());
	// matchInfo.setTimeLong(info.getTimeLong());
	// matchInfo.setHomeName(info.getHomeName());
	// matchInfo.setAwayName(info.getAwayName());
	// intent.putExtra("matchInfo", matchInfo);
	// intent.putExtra("type", type);
	// intent.putExtra("FormType", FormType);
	// intent.putExtra("tag", "defult");
	// intent.putExtra("tabName", tabName);
	//
	// context.startActivity(intent);
	// }

	// }
}
