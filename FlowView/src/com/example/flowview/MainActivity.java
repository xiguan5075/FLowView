package com.example.flowview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnPageChangeListener {
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private void initView() {
		// , R.drawable.join_qxc, R.drawable.join_sd11x5, R.drawable.join_sh11x5
		imgs = new int[] { R.drawable.ic_launcher, R.drawable.join_qxc, R.drawable.join_sd11x5, R.drawable.join_sh11x5 };
		viewPager = (MyViewPager) findViewById(R.id.my_viewpager);
		ZBCVPAdapter adapter = new ZBCVPAdapter(MainActivity.this, imgs);
		// viewPager.setAdapter(adapter);
		viewPager.setInfinateAdapter(handler, adapter);
		if (imgs.length > 1) {
			handler.sendEmptyMessageDelayed(2, 2000);
		}
		viewPager.setOnPageChangeListener(this);
		ll_indicator = (LinearLayout) findViewById(R.id.ll_indicator);
		for (int i = 0; i < imgs.length; i++) {
			ImageView imgView = (ImageView) LayoutInflater.from(MainActivity.this).inflate(R.layout.indcator, null, false);
			imgView.setPadding(5, 0, 5, 0);
			ll_indicator.addView(imgView);
		}
		if (ll_indicator.getChildCount() > 0) {
			((ImageView) ll_indicator.getChildAt(0)).setImageResource(R.drawable.indicator_select);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			index = viewPager.getCurrentItem();
			switch (msg.what) {
			case 2:
				viewPager.setCurrentItem(index + 1);// 收到消息后设置当前要显示的图片
				handler.sendEmptyMessageDelayed(2, 2000);
				break;
			// case HANDLE_MSG:
			// mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
			// break;
			default:
				break;
			}
		};
	};
	private MyViewPager viewPager;
	private int[] imgs;
	private LinearLayout ll_indicator;

	@Override
	public void onPageScrollStateChanged(int arg0) {
		switch (arg0) {
		case 1:// 手势滑动，空闲中
			break;
		case 2:// 界面切换中
			break;
		case 0:// 滑动结束，即切换完毕或者加载完毕
			break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		for (int i = 0; i < imgs.length; i++) {
			if (i == position % imgs.length) {
				((ImageView) ll_indicator.getChildAt(i)).setImageResource(R.drawable.indicator_select);
			} else {
				((ImageView) ll_indicator.getChildAt(i)).setImageResource(R.drawable.indicator_nomal);
			}
		}
	}
}
