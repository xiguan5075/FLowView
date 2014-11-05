package com.example.flowview;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private float mX, mY;
	private boolean isTouching;
	private Handler handler;

//	private MenuDrawer menuDrawer;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context, Handler handler) {
		super(context);
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}

//	public void setMenuDrawer(MenuDrawer menuDrawer) {
//		this.menuDrawer = menuDrawer;
//	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
		// 设置当前展示的位置
		setCurrentItem(0);
	}

	public void setInfinateAdapter(Handler handler, PagerAdapter adapter) {
		// this.act = (MainActivity)act;
		this.handler = handler;
		setAdapter(adapter);
	}

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// int action = ev.getAction();
	// if (action == MotionEvent.ACTION_DOWN) {
	// // act.isRun = false;
	// // act.isDown = true;
	// handler.removeCallbacksAndMessages(null);
	// System.out
	// .println("InfiniteLoopViewPager  dispatchTouchEvent =====>>> ACTION_DOWN");
	// } else if (action == MotionEvent.ACTION_MOVE) {
	// // act.isDown = true;
	// // act.isRun = false;
	// handler.removeCallbacksAndMessages(null);
	// //
	// System.out.println("InfiniteLoopViewPager  dispatchTouchEvent =====>>> ACTION_MOVE");
	// } else if (action == MotionEvent.ACTION_UP) {
	// // act.isRun = true;
	// // act.isDown = false;
	// handler.removeCallbacksAndMessages(null);
	// handler.sendEmptyMessageDelayed(1, 500);
	// System.out
	// .println("InfiniteLoopViewPager  dispatchTouchEvent =====>>> ACTION_UP");
	// }
	// return super.dispatchTouchEvent(ev);
	// }

	@Override
	public void setOffscreenPageLimit(int limit) {
		super.setOffscreenPageLimit(limit);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mX = ev.getRawX();
			mY = ev.getRawY();
			isTouching = true;
			getParent().requestDisallowInterceptTouchEvent(false);
			if (handler != null) {
				handler.removeCallbacksAndMessages(null);
//				handler.sendEmptyMessageDelayed(2, 1000);
			}
//			if (menuDrawer != null) {
//				menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
//			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isTouching) {
				float dx = Math.abs(ev.getRawX() - mX);
				float dy = Math.abs(ev.getRawY() - mY);
				if (dx > dy) {
					getParent().requestDisallowInterceptTouchEvent(true);
					isTouching = false;
				}
				if (handler != null) {
					handler.removeCallbacksAndMessages(null);
//					handler.sendEmptyMessageDelayed(2, 1000);
				}
			}
			break;
			
		case MotionEvent.ACTION_UP:
			getParent().requestDisallowInterceptTouchEvent(false);
			isTouching = false;
			if (handler != null) {
				handler.removeCallbacksAndMessages(null);
				handler.sendEmptyMessageDelayed(2, 1000);
			}
//			if (menuDrawer != null) {
//				menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
//			}
			break;
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(false);
			isTouching = false;
			if (handler != null) {
				handler.sendEmptyMessageDelayed(2, 1000);
			}
//			if (menuDrawer != null) {
//				menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
//			}
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}
	 
}
