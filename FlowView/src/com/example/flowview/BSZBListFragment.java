//package com.example.flowview;
//
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.HashSet;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.MarginLayoutParams;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.AdapterView;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ExpandableListView;
//import android.widget.ExpandableListView.OnChildClickListener;
//import android.widget.ExpandableListView.OnGroupClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.joytouch.superlive.R;
//import com.joytouch.superlive.activity.SSXQFragmentActivity;
//import com.joytouch.superlive.adapter.ZBCVPAdapter;
//import com.joytouch.superlive.adapter.ZBExpandableListAdapter;
//import com.joytouch.superlive.adapter.ZBExpandableListAdapter_CM;
//import com.joytouch.superlive.app.Preferences;
//import com.joytouch.superlive.app.SuperLive;
//import com.joytouch.superlive.app.SuperLiveApplication;
//import com.joytouch.superlive.contraller.MenuContraller;
//import com.joytouch.superlive.error.SuperLiveCredentialsException;
//import com.joytouch.superlive.error.SuperLiveError;
//import com.joytouch.superlive.error.SuperLiveException;
//import com.joytouch.superlive.pulltorefresh.library.PullToRefreshBase;
//import com.joytouch.superlive.pulltorefresh.library.PullToRefreshBase.OnBack;
//import com.joytouch.superlive.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.joytouch.superlive.pulltorefresh.library.PullToRefreshExpandableListView;
//import com.joytouch.superlive.type.BSZBForm;
//import com.joytouch.superlive.type.BiFenForm;
//import com.joytouch.superlive.type.BiFenInfo;
//import com.joytouch.superlive.type.Group;
//import com.joytouch.superlive.type.MatchInfo;
//import com.joytouch.superlive.type.PushInfo;
//import com.joytouch.superlive.type.ShaiXuanInfo;
//import com.joytouch.superlive.type.TermNoInfo;
//import com.joytouch.superlive.type.TuPianInfo;
//import com.joytouch.superlive.util.AlarmUtils;
//import com.joytouch.superlive.util.JPushUtils;
//import com.joytouch.superlive.util.NetworkUtils;
//import com.joytouch.superlive.util.NotificationsUtil;
//import com.joytouch.superlive.util.ShaiXuanUtils;
//import com.joytouch.superlive.util.TermUtils;
//import com.joytouch.superlive.widget.MyViewPager;
//import com.umeng.analytics.MobclickAgent;
//
//public class BSZBListFragment extends Fragment implements Refreshable,
//		OnClickListener {
//
//	private SharedPreferences sharedPreferences;
//	private SharedPreferences bfsp;
//	private Context context;
//
//	private String FormType = "";
//	private String activityName = "";
//	private String tabName = "";
//	private int type;
//	private String curDate = "";
//	private String termNo = "";
//	private String TimeMark = "current";
//
//	private SimpleDateFormat format1, format2;
//	private GregorianCalendar calendar;
//
//	// 全部
//	private Group<MatchInfo> matchInfos = new Group<MatchInfo>();
//	// 筛选
//	private ArrayList<ShaiXuanInfo> shaiXuanInfos = new ArrayList<ShaiXuanInfo>();
//	private HashSet<String> leagueSet = new HashSet<String>();
//	private HashSet<String> selectedLeagueSet = new HashSet<String>();
//	private HashSet<String> teamSet = new HashSet<String>();
//	// 状态
//	public HashSet<String> stateSet = new HashSet<String>();
//	private Group<MatchInfo> tempMatchInfos = new Group<MatchInfo>();
//	private HashSet<String> myMatchAlarm = new HashSet<String>();
//	// 分组
//	private Group<Group<MatchInfo>> matchGroups = new Group<Group<MatchInfo>>();
//	// 期号
//	private ArrayList<TermNoInfo> termNoInfos = new ArrayList<TermNoInfo>();
//	// 图片
//	private Group<TuPianInfo> tuPianInfos = new Group<TuPianInfo>();
//	private ArrayList<ImageView> indicators = new ArrayList<ImageView>();
//	// 比分
//	private HashMap<String, String> goalMap = new HashMap<String, String>();
//	private HashMap<String, String> tpGoalMap = new HashMap<String, String>();
//
//	private View mView;
//	private PullToRefreshExpandableListView ptr_epd_lv;
//	private ExpandableListView epd_lv;
//	private LinearLayout ll_list_group_title;
//	private TextView tv_date;
//	private RelativeLayout rl_header_view;
//	private MyViewPager cvp;
//	private LinearLayout ll_indicator;
//
//	private BaseExpandableListAdapter zbExpandableListAdapter;
//	private ZBCVPAdapter zbcvpAdapter;
//
//	private boolean isFirstTime = true;
//	private boolean isRefreshing = false;
//	private boolean isUserVisible = false;
//	private boolean isLoadingMore = false;
//
//	private LinearLayout ll_detail_loading;
//	private boolean progressBarBool = false;
//	private ProgressBar loadingProgressBar;
//
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		// TODO Auto-generated method stub
//		isUserVisible = isVisibleToUser;
//		if (isVisibleToUser) {
//			if (isFirstTime) {
//				new RefreshTask().execute();
//			}
//		}
//		super.setUserVisibleHint(isVisibleToUser);
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		context = getActivity();
//
//		Bundle bundle = getArguments();
//		FormType = bundle.getString("FormType");
//
//		if ("zhibo".equals(FormType)) {
//			activityName = "直播列表";
//		} else if ("huifang".equals(FormType)) {
//			activityName = "回放列表";
//		}else if ("caimin".equals(FormType)) {
//			activityName = "彩民版直播列表";
//		}
//
//		tabName = bundle.getString("tabName");
//		type = bundle.getInt("type", 0);
//		bfsp = context.getSharedPreferences(Preferences.bszbbf_history + type,
//				Context.MODE_PRIVATE);
//
//		if ("caimin".equals(FormType)) {
//			zbExpandableListAdapter = new ZBExpandableListAdapter_CM(context,
//					matchGroups, type, FormType, tabName);
//		} else {
//			zbExpandableListAdapter = new ZBExpandableListAdapter(context,
//					matchGroups, type, FormType, tabName);
//		}
//
//		format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		format2 = new SimpleDateFormat("yyyy-MM-dd");
//		calendar = new GregorianCalendar();
//	}
//
//	@Override
//	public void onDestroyView() {
//		// TODO Auto-generated method stub
//		super.onDestroyView();
//		ptr_epd_lv = null;
//		mView = null;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		sharedPreferences = context.getSharedPreferences(
//				Preferences.preference, Context.MODE_PRIVATE);
//		isRefreshing = false;
//		mView = inflater
//				.inflate(R.layout.pager_content_expandable, null, false);
//
//		loadingProgressBar = (ProgressBar) mView
//				.findViewById(R.id.loadingProgressBar);
//
//		ptr_epd_lv = (PullToRefreshExpandableListView) mView
//				.findViewById(R.id.ptr_epd_lv);
//		epd_lv = ptr_epd_lv.getRefreshableView();
//		ll_list_group_title = (LinearLayout) mView
//				.findViewById(R.id.ll_list_group_title);
//		tv_date = (TextView) mView.findViewById(R.id.tv_date);
//		ll_detail_loading = (LinearLayout) mView
//				.findViewById(R.id.ll_detail_loading);
//		ll_detail_loading.setVisibility(View.GONE);
//		ll_detail_loading.setOnClickListener(this);
//
//		View headerView = inflater.inflate(R.layout.vp_circle, null);
//		rl_header_view = (RelativeLayout) headerView
//				.findViewById(R.id.rl_header_view);
//		ll_indicator = (LinearLayout) headerView
//				.findViewById(R.id.ll_indicator);
//		cvp = (MyViewPager) headerView.findViewById(R.id.cvp);
//		makeCVP();
//		epd_lv.addHeaderView(headerView);
//
//		epd_lv.setAdapter(zbExpandableListAdapter);
//
//		epd_lv.setOnChildClickListener(new OnChildClickListener() {
//
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v,
//					int groupPosition, int childPosition, long id) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(context, SSXQFragmentActivity.class);
//				intent.putExtra("type", type);
//				intent.putExtra("FormType", FormType);
//				intent.putExtra("tag", "defult");
//				intent.putExtra("matchInfo", matchGroups.get(groupPosition)
//						.get(childPosition));
//				intent.putExtra("tabName", tabName);
//				startActivity(intent);
//				return false;
//			}
//		});
//
//		epd_lv.setOnGroupClickListener(new OnGroupClickListener() {
//
//			@Override
//			public boolean onGroupClick(ExpandableListView parent, View v,
//					int groupPosition, long id) {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
//
//		epd_lv.setOnScrollListener(new OnScrollListener() {
//
//			/**
//			 * 当前打开的父节点
//			 */
//			private int the_group_expand_position = -1;
//			/**
//			 * 获取当前打开的节点的高度
//			 */
//			private int indicatorGroupHeight;
//			private boolean boo_is_collapseGroup = false;
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//
//				// 自动加载更多
//				if (isUserVisible
//						&& !isLoadingMore
//						&& matchInfos.size() != 0
//						&& firstVisibleItem + visibleItemCount == totalItemCount) {
//					loadMore();
//				}
//				// 悬浮title
//				int npos = view.pointToPosition(0, 0);
//				if (npos != AdapterView.INVALID_POSITION) {
//					long pos = epd_lv.getExpandableListPosition(npos);
//					int childPos = ExpandableListView
//							.getPackedPositionChild(pos);
//					final int groupPos = ExpandableListView
//							.getPackedPositionGroup(pos);
//					if (childPos == AdapterView.INVALID_POSITION) {
//						View groupView = epd_lv.getChildAt(npos
//								- epd_lv.getFirstVisiblePosition());
//						indicatorGroupHeight = groupView.getHeight();
//					}
//
//					// get an error data, so return now
//					if (indicatorGroupHeight == 0) {
//						return;
//					}
//
//					if (boo_is_collapseGroup) {
//						the_group_expand_position = groupPos;
//					}
//
//					// 当悬浮条滑动到下一个节点的时候自动消失
//					if (the_group_expand_position != groupPos) {
//						the_group_expand_position = groupPos;
//						ll_list_group_title.setVisibility(View.GONE);
//					} else {
//						boo_is_collapseGroup = false;
//						if (epd_lv.isGroupExpanded(the_group_expand_position)) {
//							ll_list_group_title.setVisibility(View.VISIBLE);
//
//							String dateString = matchGroups.get(
//									the_group_expand_position).getType();
//
//							if (!"".equals(dateString)) {
//								tv_date.setText(dateString);
//							} else {
//								ll_list_group_title.setVisibility(View.GONE);
//							}
//						} else {
//							ll_list_group_title.setVisibility(View.GONE);
//						}
//					}
//				}
//
//				if (the_group_expand_position == -1) {
//					return;
//				}
//
//				/**
//				 * calculate point (0,indicatorGroupHeight)
//				 */
//				int showHeight = getHeight();
//				// update group position
//				MarginLayoutParams layoutParams = (MarginLayoutParams) ll_list_group_title
//						.getLayoutParams();
//				// 得到悬浮的条滑出屏幕多少
//				layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
//				ll_list_group_title.setLayoutParams(layoutParams);
//			}
//
//			/**
//			 * 获取头部要移动的距离
//			 * 
//			 * @return
//			 */
//			private int getHeight() {
//				int showHeight = indicatorGroupHeight;
//				// 从顶部到滑动的位置总过滑动了多少个节点
//				int nEndPos = epd_lv.pointToPosition(0, indicatorGroupHeight);
//				if (nEndPos != AdapterView.INVALID_POSITION) {
//					long pos = epd_lv.getExpandableListPosition(nEndPos);
//					// 当前滑动到父节点的什么位置
//					int groupPos = ExpandableListView
//							.getPackedPositionGroup(pos);
//					if (groupPos != the_group_expand_position) {
//						View viewNext = epd_lv.getChildAt(nEndPos
//								- epd_lv.getFirstVisiblePosition());
//						// 悬浮条还有多少在可见（即没有滑出屏幕的部分）当滑动到下一个父列表顶部，悬浮条会随之滑出屏幕
//						showHeight = viewNext.getTop();
//					}
//				}
//				return showHeight;
//			}
//		});
//
//		ptr_epd_lv.setOnBack(new OnBack() {
//
//			@Override
//			public void OnBack(int scrollY) {
//				// TODO Auto-generated method stub
//				if (scrollY <= 0) {
//					ll_list_group_title.setVisibility(View.GONE);
//				}
//			}
//		});
//
//		ptr_epd_lv
//				.setOnRefreshListener(new OnRefreshListener2<ExpandableListView>() {
//
//					@Override
//					public void onPullDownToRefresh(
//							PullToRefreshBase<ExpandableListView> refreshView) {
//						// TODO Auto-generated method stub
//						isRefreshing = true;
//						new RefreshTask().execute();
//						new BiFenTask().execute();
//					}
//
//					@Override
//					public void onPullUpToRefresh(
//							PullToRefreshBase<ExpandableListView> refreshView) {
//						// TODO Auto-generated method stub
//						new LoadMoreTask().execute();
//					}
//				});
//
//		if ((matchInfos != null) && (matchInfos.size() > 0)) {
//			progressBarBool = false;
//		} else {
//			progressBarBool = true;
//		}
//
//		if (progressBarBool) {
//			loadingProgressBar.setVisibility(View.VISIBLE);
//		}
//		return mView;
//	}
//
//	@Override
//	public void refresh() {
//
//		if (!isRefreshing) {
//			isRefreshing = true;
//			if (ptr_epd_lv != null) {
//				ptr_epd_lv.showRefreshing();
//			}
//		}
//	}
//
//	@Override
//	public void loadMore() {
//		new LoadMoreTask().execute();
//	}
//
//	public void refreshGroup() {
//		doShaiXuan();
//		fenZu();
//		zbExpandableListAdapter.notifyDataSetChanged();
//		for (int i = 0; i < zbExpandableListAdapter.getGroupCount(); i++) {
//			epd_lv.expandGroup(i);
//		}
//	}
//
//	// 筛选
//	public void doShaiXuan() {
//		if (matchInfos == null) {
//			return;
//		}
//
//		if (tempMatchInfos == null) {
//			return;
//		}
//
//		selectedLeagueSet.clear();
//		stateSet.clear();
//		myMatchAlarm.clear();
//		teamSet.clear();
//
//		for (ShaiXuanInfo info : shaiXuanInfos) {
//			if (info.isSelected()) {
//				if (info.getName().equals("已完场")) {
//					stateSet.add("wan");
//				} else if (info.getName().equals("未开赛")) {
//					stateSet.add("wei");
//				} else if (info.getName().equals("直播中")) {
//					stateSet.add("live");
//				} else if (info.getName().equals("我的比赛")) {
//					getAlarmMatchId();
//					myMatchAlarm.add("myMatch");
//				} else if (info.getName().equals("我的球队")) {
//					getTeamId();
//				} else {
//					selectedLeagueSet.add(info.getName());
//				}
//			}
//		}
//
//		tempMatchInfos.clear();
//
//		for (MatchInfo info : matchInfos) {
//			boolean b_state = false;
//			boolean b_match = false;
//			boolean b_league = false;
//			boolean b_team = false;
//
//			if (stateSet.size() == 0 || stateSet.contains(info.getMatchState())) {
//				b_state = true;
//			}
//			if (myMatchAlarm.size() == 0
//					|| myMatchAlarm.contains(info.getMatchInfoID())) {
//				b_match = true;
//			}
//
//			if ((selectedLeagueSet.size() == 0 && teamSet.size() == 0)) {
//				b_league = true;
//				b_team = true;
//			}
//			if (selectedLeagueSet.contains(info.getLeagueName())) {
//				b_league = true;
//			}
//			if (teamSet.contains(info.getHomeID())
//					|| teamSet.contains(info.getAwayID())) {
//				b_team = true;
//			}
//
//			if (b_state && b_match && (b_league || b_team)) {
//				tempMatchInfos.add(info);
//			}
//		}
//
//	}
//
//	// 得到提醒的比赛id
//	public void getAlarmMatchId() {
//		new AlarmUtils();
//		for (String id : AlarmUtils.getSaishiMap().keySet()) {
//			myMatchAlarm.add(id);
//		}
//		new JPushUtils();
//		for (String id : JPushUtils.getMatchMap().keySet()) {
//			myMatchAlarm.add(id);
//		}
//	}
//
//	// 得到关注球队
//	public void getTeamId() {
//		HashMap<String, PushInfo> map = JPushUtils.getTeamMap();
//		if (map != null) {
//			teamSet.addAll(map.keySet());
//		}
//	}
//
//	// 分组
//	private Group<MatchInfo> tempGroup;
//	private String tempDate = "";
//	private SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
//	private SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd E");
//
//	private void newGroup(String date) {
//		tempGroup = new Group<MatchInfo>();
//		tempDate = date;
//		try {
//			tempGroup.setType(format4.format(format3.parse(date)));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		matchGroups.add(tempGroup);
//	}
//
//	public void fenZu() {
//		matchGroups.clear();
//		tempDate = "";
//		for (int i = 0; i < tempMatchInfos.size(); i++) {
//			String date = tempMatchInfos.get(i).getTime().substring(0, 10);
//			if (!tempDate.equals(date)) {
//				newGroup(date);
//			}
//			tempGroup.add(tempMatchInfos.get(i));
//		}
//
//	}
//
//	// 切换期号
//	public void requestNewTermNo() {
//		String tempTermNo = termNo;
//		for (TermNoInfo termNoInfo : termNoInfos) {
//			if (termNoInfo.isSelected()) {
//				this.termNo = termNoInfo.getTermNo();
//			}
//		}
//		if (!termNo.equals(tempTermNo)) {
//			matchInfos.clear();
//			tempMatchInfos.clear();
//			matchGroups.clear();
//			zbExpandableListAdapter.notifyDataSetChanged();
//			ll_list_group_title.setVisibility(View.GONE);
//			loadingProgressBar.setVisibility(View.VISIBLE);
//			new RefreshTask().execute();
//		}
//	}
//
//	// 刷新比赛数据
//	class RefreshTask extends AsyncTask<Void, Void, BSZBForm> {
//
//		private Exception mException = null;
//
//		public RefreshTask() {
//			progressBarBool = true;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			// ll_list_group_title.setVisibility(View.GONE);
//		}
//
//		@Override
//		protected BSZBForm doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//
//			// 刷新请求
//			curDate = "";
//			SuperLive superLive = ((SuperLiveApplication) context
//					.getApplicationContext()).getSuperLive();
//			BSZBForm form = null;
//			try {
//				if ("zhibo".equals(FormType)) {
//					form = superLive.getBszbForm(curDate, type);
//				} else if ("huifang".equals(FormType)) {
//					form = superLive.getHuiFangForm(curDate, type);
//				} else if ("caimin".equals(FormType)) {
//					form = superLive.getBszbFormForLottery(type, termNo);
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				mException = e;
//			}
//			return form;
//		}
//
//		@Override
//		protected void onPostExecute(BSZBForm result) {
//			// TODO Auto-generated method stub
//			progressBarBool = false;
//			loadingProgressBar.setVisibility(View.GONE);
//			if (result == null) {
//				NotificationsUtil.ToastReasonForFailure(context, mException);
//			}
//			if (result != null) {
//
//				tuPianInfos.clear();
//				tuPianInfos.addAll(result.getTuPianInfos());
//
//				matchInfos.clear();
//				matchInfos.addAll(result.getMatchInfos());
//				if (sharedPreferences.getBoolean("isFirst", true)
//						&& matchInfos.size() > 0) {
//					matchInfos.get(0).setPeiLvVisible(true);
//					sharedPreferences.edit().putBoolean("isFirst", false)
//							.commit();
//				}
//
//				termNoInfos.clear();
//				termNoInfos.addAll(result.getTermNoInfos());
//
//				for (TermNoInfo info : termNoInfos) {
//					if (termNo.equals(info.getTermNo())) {
//						info.setSelected(true);
//						TimeMark = info.getTimeMark();
//					}
//				}
//				if ("current".equals(TimeMark) && termNoInfos.size() > 0) {
//					for (TermNoInfo info : termNoInfos) {
//						if ("current".equals(info.getTimeMark())) {
//							info.setSelected(true);
//						}
//					}
//				}
//
//				resetShaiXuanInfos();
//
//				((MenuContraller) getActivity()).refreshTermNo();
//				((MenuContraller) getActivity()).refreshShaiXuanInfo();
//				refreshGroup();
//			}
//
//			if (ptr_epd_lv != null) {
//				ptr_epd_lv.onRefreshComplete();
//			}
//
//			if (cvp != null) {
//				makeCVP();
//			}
//
//			isFirstTime = false;
//			isRefreshing = false;
//			isLoadingMore = false;
//
//			if (NetworkUtils.isWifiConnected(context)) {
//				new BiFenTask().execute();
//			} else {
//				if (!sharedPreferences.getBoolean(Preferences.autoBiFen, false)) {
//					new BiFenTask().execute();
//				}
//			}
//		}
//	}
//
//	class LoadMoreTask extends AsyncTask<Void, Void, BSZBForm> {
//
//		private Exception mException = null;
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			isLoadingMore = true;
//		}
//
//		@Override
//		protected BSZBForm doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			if (matchInfos.size() > 0) {
//				MatchInfo info = matchInfos.get(matchInfos.size() - 1);
//				try {
//					Date date = format1.parse(info.getTime());
//					calendar.setTime(date);
//					if ("zhibo".equals(FormType)) {
//						calendar.add(Calendar.DATE, 1);
//					} else if ("huifang".equals(FormType)) {
//						calendar.add(Calendar.DATE, 0);
//					}
//					date = calendar.getTime();
//					curDate = format2.format(date);
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//			SuperLive superLive = ((SuperLiveApplication) context
//					.getApplicationContext()).getSuperLive();
//			BSZBForm form = null;
//			try {
//				if ("zhibo".equals(FormType)) {
//
//					form = superLive.getBszbForm(curDate, type);
//
//				} else if ("huifang".equals(FormType)) {
//					form = superLive.getHuiFangForm(curDate, type);
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				mException = e;
//			}
//			return form;
//		}
//
//		@Override
//		protected void onPostExecute(BSZBForm result) {
//			// TODO Auto-generated method stub
//
//			isLoadingMore = false;
//			if (ptr_epd_lv != null) {
//				ptr_epd_lv.onRefreshComplete();
//			}
//
//			if (result == null) {
//				if (!"caimin".equals(FormType)) {
//					NotificationsUtil
//							.ToastReasonForFailure(context, mException);
//				}
//				isLoadingMore = true;
//			}
//			if (result != null) {
//				matchInfos.addAll(result.getMatchInfos());
//				if (result.getMatchInfos().size() == 0) {
//					isLoadingMore = true;
//					Toast.makeText(context, "暂时没有更多的比赛信息", 1000).show();
//				}
//			}
//
//			addLeagues();
//			sortShaiXuanInfos();
//			((MenuContraller) getActivity()).refreshShaiXuanInfo();
//			refreshGroup();
//			zbExpandableListAdapter.notifyDataSetChanged();
//		}
//	}
//
//	// 获取比分
//	class BiFenTask extends AsyncTask<Void, Void, BiFenForm> {
//
//		@Override
//		protected BiFenForm doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			SuperLive superLive = ((SuperLiveApplication) context
//					.getApplicationContext()).getSuperLive();
//			BiFenForm form = null;
//
//			try {
//				if ("caimin".equals(FormType)) {
//					form = superLive.getBiFenFormForLottery(type);
//				} else {
//					form = superLive.getBiFenForm(type);
//				}
//				if (form.getMatchBfMap().size() > 0
//						|| form.getTpBfMap().size() > 0) {
//					return form;
//				}
//			} catch (SuperLiveError e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SuperLiveCredentialsException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SuperLiveException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(BiFenForm result) {
//			// TODO Auto-generated method stub
//			if (result != null) {
//
//				for (MatchInfo info : matchInfos) {
//					if (result.getMatchBfMap().containsKey(
//							info.getMatchInfoID())) {
//						BiFenInfo bfInfo = result.getMatchBfMap().get(
//								info.getMatchInfoID());
//						if (!bfInfo.getHomeGoals().equals(info.getHomeGoals())) {
//							info.setHomeGoals(bfInfo.getHomeGoals());
//							if (!goalMap.containsKey(info.getMatchInfoID()
//									+ "H")
//									|| !info.getHomeGoals().equals(
//											goalMap.get(info.getMatchInfoID()
//													+ "H"))) {
//								goalMap.put(info.getMatchInfoID() + "H",
//										info.getHomeGoals());
//								if (!"0".equals(info.getHomeGoals())) {
//									info.setHomeGoalTime(System
//											.currentTimeMillis());
//								}
//							}
//
//						}
//						if (!bfInfo.getAwayGoals().equals(info.getAwayGoals())) {
//							info.setAwayGoals(bfInfo.getAwayGoals());
//							if (!goalMap.containsKey(info.getMatchInfoID()
//									+ "A")
//									|| !info.getAwayGoals().equals(
//											goalMap.get(info.getMatchInfoID()
//													+ "A"))) {
//								goalMap.put(info.getMatchInfoID() + "A",
//										info.getAwayGoals());
//								if (!"0".equals(info.getAwayGoals())) {
//									info.setAwayGoalTime(System
//											.currentTimeMillis());
//								}
//							}
//						}
//						info.setBiFenState(bfInfo.getMatchState());
//						info.setRealStartTime(bfInfo.getRealStartTime());
//						info.setLiveTime(bfInfo.getLiveTime());
//					}
//				}
//				for (TuPianInfo info : tuPianInfos) {
//					if (result.getMatchBfMap().containsKey(
//							info.getMatchInfoID())) {
//						BiFenInfo bfInfo = result.getMatchBfMap().get(
//								info.getMatchInfoID());
//						if (!bfInfo.getHomeGoals().equals(info.getHomeGoals())) {
//							info.setHomeGoals(bfInfo.getHomeGoals());
//							if (!tpGoalMap.containsKey(info.getMatchInfoID()
//									+ "H")
//									|| !info.getHomeGoals().equals(
//											tpGoalMap.get(info.getMatchInfoID()
//													+ "H"))) {
//								tpGoalMap.put(info.getMatchInfoID() + "H",
//										info.getHomeGoals());
//								if (!"0".equals(info.getHomeGoals())) {
//									info.setHomeGoalTime(System
//											.currentTimeMillis());
//								}
//							}
//
//						}
//						if (!bfInfo.getAwayGoals().equals(info.getAwayGoals())) {
//							info.setAwayGoals(bfInfo.getAwayGoals());
//							if (!tpGoalMap.containsKey(info.getMatchInfoID()
//									+ "A")
//									|| !info.getAwayGoals().equals(
//											tpGoalMap.get(info.getMatchInfoID()
//													+ "A"))) {
//								tpGoalMap.put(info.getMatchInfoID() + "A",
//										info.getAwayGoals());
//								if (!"0".equals(info.getAwayGoals())) {
//									info.setAwayGoalTime(System
//											.currentTimeMillis());
//								}
//							}
//						}
//						info.setBiFenState(bfInfo.getMatchState());
//						info.setRealStartTime(bfInfo.getRealStartTime());
//						info.setLiveTime(bfInfo.getLiveTime());
//					}
//				}
//
//				if (zbExpandableListAdapter != null) {
//					zbExpandableListAdapter.notifyDataSetChanged();
//				}
//			}
//		}
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		if (!NetworkUtils.isNetworkConnected(context)) {
//			ptr_epd_lv.setVisibility(View.GONE);
//			ll_detail_loading.setVisibility(View.VISIBLE);
//		} else {
//			ptr_epd_lv.setVisibility(View.VISIBLE);
//			ll_detail_loading.setVisibility(View.GONE);
//		}
//		MobclickAgent.onPageStart(activityName + "-" + tabName);
//		alarmChange();
//		handler.sendEmptyMessage(2);
//		refreshGroup();
//	}
//
//	public void alarmChange() {
//		if (zbExpandableListAdapter != null) {
//			zbExpandableListAdapter.notifyDataSetChanged();
//		}
//	}
//
//	@Override
//	public void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		MobclickAgent.onPageEnd(activityName + "-" + tabName);
//		handler.removeCallbacksAndMessages(null);
//	}
//
//	// 更新
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//
//			case 2:
//				if (NetworkUtils.isWifiConnected(context)) {
//					new BiFenTask().execute();
//				} else {
//					if (sharedPreferences.getBoolean(Preferences.autoBiFen,
//							false)) {
//						new BiFenTask().execute();
//					}
//				}
//				sendEmptyMessageDelayed(2, 30 * 1000);
//				break;
//			}
//
//		}
//
//	};
//
//	// 筛选初始化
//	public void addMyMatch() {
//		ShaiXuanInfo shaiXuanInfo = new ShaiXuanInfo();
//		shaiXuanInfo.setName("我的比赛");
//		shaiXuanInfo.setPriority(0);
//		shaiXuanInfo.setSelected(false);
//		shaiXuanInfos.add(shaiXuanInfo);
//	}
//
//	public void addMyTeam() {
//		ShaiXuanInfo shaiXuanInfo = new ShaiXuanInfo();
//		shaiXuanInfo.setName("我的球队");
//		shaiXuanInfo.setPriority(0);
//		shaiXuanInfo.setSelected(false);
//		shaiXuanInfos.add(shaiXuanInfo);
//	}
//
//	public void addMatchState() {
//		ShaiXuanInfo shaiXuanInfo = new ShaiXuanInfo();
//		shaiXuanInfo.setName("已完场");
//		shaiXuanInfo.setPriority(0);
//		shaiXuanInfo.setSelected(false);
//		shaiXuanInfos.add(shaiXuanInfo);
//		shaiXuanInfo = new ShaiXuanInfo();
//		shaiXuanInfo.setName("未开赛");
//		shaiXuanInfo.setPriority(0);
//		shaiXuanInfo.setSelected(false);
//		shaiXuanInfos.add(shaiXuanInfo);
//		shaiXuanInfo = new ShaiXuanInfo();
//		shaiXuanInfo.setName("直播中");
//		shaiXuanInfo.setPriority(0);
//		shaiXuanInfo.setSelected(false);
//		shaiXuanInfos.add(shaiXuanInfo);
//	}
//
//	public void addLeagues() {
//		for (MatchInfo info : matchInfos) {
//			if (!leagueSet.contains(info.getLeagueName())) {
//				leagueSet.add(info.getLeagueName());
//				ShaiXuanInfo shaiXuanInfo = new ShaiXuanInfo();
//				shaiXuanInfo.setName(info.getLeagueName());
//				shaiXuanInfo.setLogoUrl(info.getPlayLogo());
//				shaiXuanInfo.setPriority(info.getLeaguePriority());
//				shaiXuanInfo.setSelected(false);
//				shaiXuanInfos.add(shaiXuanInfo);
//			}
//		}
//
//	}
//
//	public void sortShaiXuanInfos() {
//		Collections.sort(shaiXuanInfos, new Comparator<ShaiXuanInfo>() {
//
//			@Override
//			public int compare(ShaiXuanInfo lhs, ShaiXuanInfo rhs) {
//				// TODO Auto-generated method stub
//				if (lhs.getPriority() < rhs.getPriority()) {
//					return -1;
//				}
//				return 0;
//			}
//		});
//	}
//
//	public void resetShaiXuanInfos() {
//		shaiXuanInfos.clear();
//		leagueSet.clear();
//		// 开赛状态
//		if ("caimin".equals(FormType)) {
//			addMatchState();
//		}
//		// 我的比赛
//		if (!"old".equals(TimeMark) && !"huifang".equals(FormType)) {
//			addMyMatch();
//		}
//		// 我的球队
//		if ("zhibo".equals(FormType) || "huifang".equals(FormType)) {
//			addMyTeam();
//		}
//		// 联赛名
//		addLeagues();
//		sortShaiXuanInfos();
//	}
//
//	public void refreshShaiXuanInfo() {
//		ShaiXuanUtils.shaiXuanInfos.clear();
//		ShaiXuanUtils.shaiXuanInfos.addAll(shaiXuanInfos);
//	}
//
//	public void saveShaiXuanInfo() {
//		shaiXuanInfos.clear();
//		shaiXuanInfos.addAll(ShaiXuanUtils.shaiXuanInfos);
//	}
//
//	public void refreshTermNo() {
//		TermUtils.termNoInfos.clear();
//		TermUtils.termNoInfos.addAll(termNoInfos);
//	}
//
//	public void saveTermNo() {
//		termNoInfos.clear();
//		termNoInfos.addAll(TermUtils.termNoInfos);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.ll_detail_loading:
//			if (!NetworkUtils.isNetworkConnected(context)) {
//				ptr_epd_lv.setVisibility(View.GONE);
//				ll_detail_loading.setVisibility(View.VISIBLE);
//				Toast.makeText(context, "请检查网络！", Toast.LENGTH_SHORT).show();
//			} else {
//				ptr_epd_lv.setVisibility(View.VISIBLE);
//				ll_detail_loading.setVisibility(View.GONE);
//				progressBarBool = true;
//				new RefreshTask().execute();
//			}
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	public void makeIndicator() {
//		ll_indicator.removeAllViews();
//		indicators.clear();
//		for (int i = 0; i < tuPianInfos.size(); i++) {
//			ImageView imageView = new ImageView(context);
//			imageView.setBackgroundResource(R.drawable.indicator_off);
//			indicators.add(imageView);
//			ll_indicator.addView(imageView);
//		}
//
//	}
//
//	public void makeCVP() {
//		if (tuPianInfos.size() == 0) {
//			ll_indicator.setVisibility(View.GONE);
//			cvp.setVisibility(View.GONE);
//			rl_header_view.setVisibility(View.GONE);
//			zbExpandableListAdapter.notifyDataSetChanged();
//			return;
//		}
//		rl_header_view.setVisibility(View.VISIBLE);
//		cvp.setVisibility(View.VISIBLE);
//		ll_indicator.setVisibility(View.VISIBLE);
//		makeIndicator();
//		Group<TuPianInfo> group = new Group<TuPianInfo>();
//
//		if (tuPianInfos.size() == 1) {
//			group.add(tuPianInfos.get(0));
//		} else if (tuPianInfos.size() > 1) {
//			for (int i = 0; i < 4; i++) {
//				group.addAll(tuPianInfos);
//			}
//		}
//		zbcvpAdapter = new ZBCVPAdapter(context, group, type, FormType, tabName);
//		cvp.setInfinateAdapter(handler, zbcvpAdapter);
//
//		cvp.setCurrentItem(tuPianInfos.size() * 2, false);
//
//		cvp.setOffscreenPageLimit(tuPianInfos.size());
//
//		indicators.get(0).setBackgroundResource(R.drawable.indicator_on);
//		cvp.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int arg0) {
//				// TODO Auto-generated method stub
//				if (tuPianInfos.size() > 0) {
//
//					int current = arg0 % tuPianInfos.size();
//
//					for (int i = 0; i < indicators.size(); i++) {
//						if (i == current) {
//							indicators.get(i).setBackgroundResource(
//									R.drawable.indicator_on);
//						} else {
//							indicators.get(i).setBackgroundResource(
//									R.drawable.indicator_off);
//
//						}
//					}
//				}
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//			}
//		});
//
//		zbExpandableListAdapter.notifyDataSetChanged();
//	}
//}
