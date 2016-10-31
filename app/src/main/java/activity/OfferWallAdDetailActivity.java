package activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chuanqi.yz.R;

import Interfaces.BitmapDownloadListener;
import Manager.BitmapLoaderManager;
import Utis.Utis;
import adapter.GridViewAdapter;
import adapter.ListViewAdapter_TaskDesc;
import model.TaskDescObject;
import tj.zl.op.os.PointsChangeNotify;
import tj.zl.op.os.PointsManager;
import tj.zl.op.os.df.DiyAppNotify;
import tj.zl.op.os.df.AdExtraTaskStatus;
import tj.zl.op.os.df.AdTaskStatus;
import tj.zl.op.os.df.AppDetailDataInterface;
import tj.zl.op.os.df.AppDetailObject;
import tj.zl.op.os.df.AppExtraTaskObject;
import tj.zl.op.os.df.AppExtraTaskObjectList;
import tj.zl.op.os.df.AppSummaryObject;
import tj.zl.op.os.df.DiyOfferWallManager;

import java.util.ArrayList;
import java.util.List;

public class OfferWallAdDetailActivity extends BaseActivity
		implements OnClickListener, BitmapDownloadListener, DiyAppNotify, PointsChangeNotify {
	public AppSummaryObject appSumObject;
	private ImageView appIcon;
	private TextView rewardCount;
	private TextView appName;
	private TextView appVersion;
	private TextView appSize;
	private TextView appStyle;
	private TextView appDesc;
	private Button openOrDownloadBtn;
	private ProgressBar downlaodProgressBar;
	private SwipeRefreshLayout mSwipeRefreshLayout; // 下拉刷新组件
	private GridView gridView;
	private ListView listView;
	private GridViewAdapter gvAdapter;
	private ListViewAdapter_TaskDesc lvAdapter;
	private AppDetailObject appDetailObject;
	private ArrayList<Bitmap> bmLists;
	/**
	 * 任务描述列表的数据列表
	 */
	private ArrayList<TaskDescObject> mTaskDescList;

	private boolean isPackageExist = false;

	private final static Handler handler = new Handler();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youmi_task_detail);

		// 检查传入的Intent是否合法，不合法就直接finish
		Object obj = getIntent().getSerializableExtra("ad");
		if (obj == null || !(obj instanceof AppSummaryObject)) {
			this.finish();
			return;
		}
		appSumObject = (AppSummaryObject) obj;

		// 检查这个应用是否已经存在于手机中
		isPackageExist = Utis.checkApkExist(this, appSumObject.getPackageName());

		// 初始化View
		initView();

		// （可选）注册积分余额变动监听-随时随地获得积分的变动情况
		PointsManager.getInstance(this).registerNotify(this);

		// （可选）注册广告下载安装监听-随时随地获得应用下载安装状态的变动情况
		DiyOfferWallManager.getInstance(this).registerListener(this);

		// 获取广告的详细数据
		requestDetailData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// （可选）注销积分余额监听-如果在onCreate中注册了，那这里必须得注销
		PointsManager.getInstance(this).unRegisterNotify(this);

		// （可选）注销下载安装监听-如果在onCreate中注册了，那这里必须得注销
		DiyOfferWallManager.getInstance(this).removeListener(this);

	}

	private void initView() {

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_ad_detail);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				// 重新刷新页面
				requestDetailData();
			}
		});
		mSwipeRefreshLayout
				.setColorSchemeColors(Color.parseColor("#ff00ddff"), Color.parseColor("#ff99cc00"), Color.parseColor
								("#ffffbb33"),
						Color.parseColor("#ffff4444"));

		appIcon = (ImageView) findViewById(R.id.iv_detailpage_appicon);
		appName = (TextView) findViewById(R.id.tv_detailpage_appname);
		appVersion = (TextView) findViewById(R.id.tv_detailpage_apppvn);
		appSize = (TextView) findViewById(R.id.tv_detailpage_appsize);
		appStyle = (TextView) findViewById(R.id.tv_detailpage_appstyle);
		appDesc = (TextView) findViewById(R.id.tv_detailpage_appdesc);
		rewardCount = (TextView) findViewById(R.id.tv_detailpage_rewards_count);

		downlaodProgressBar = (ProgressBar) findViewById(R.id.pb_download);
		downlaodProgressBar.setVisibility(View.GONE);

		openOrDownloadBtn = (Button) findViewById(R.id.btn_detailpage_open_or_install);
		openOrDownloadBtn.setVisibility(View.INVISIBLE);
		openOrDownloadBtn.setOnClickListener(this);
		updateOpenOrDownloadButtonStatus(appSumObject.getAdTaskStatus());

		gridView = (GridView) findViewById(R.id.detailpage_gridView);
		gvAdapter = new GridViewAdapter(OfferWallAdDetailActivity.this, null);
		gridView.setAdapter(gvAdapter);

		listView = (ListView) findViewById(R.id.detailpage_listview);
		listView.setEnabled(false);
		lvAdapter = new ListViewAdapter_TaskDesc(OfferWallAdDetailActivity.this, null);
		listView.setAdapter(lvAdapter);
	}
	protected MenuItem mPointsItem, mLimitItem;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//TODO
//		if (mPointsItem != null) {
//			mPointsItem.setTitle("积分：" + PointsManager.getInstance(this).queryPoints());
//		}
//		mLimitItem.setVisible(false);
		return true;
	}

	@Override
	public void onPointBalanceChange(float points) {
//		if (mPointsItem != null) {
//			mPointsItem.setTitle("积分：" + points);
//		}
	}

	/**
	 * 获取广告的详细数据,下面展示两种加载方式，开发者可选择适合自己的方式
	 */
	private void requestDetailData() {

		mSwipeRefreshLayout.setRefreshing(true);
		// 异步加载方式
		startProgressDialog("加载中...");
		DiyOfferWallManager.getInstance(this)
				.loadAppDetailData(appSumObject, new AppDetailDataInterface() {
					/**
					 * 当成功请求到数据的时候，会回调本方法（注意:本接口不在UI线程中执行， 所以请不要在本接口中进行UI线程方面的操作） 注意：广告详细数据有可能为空，开发者处理之前，请先判断是否为空
					 */
					@Override
					public void onLoadAppDetailDataSuccess(Context context, AppDetailObject appDetailObject) {
						stopProgressDialog();
						updateView(appDetailObject);
					}
					/**
					 * 请求成功，但是返回有米错误代码时候，会回调这个接口
					 */
					@Override
					public void onLoadAppDetailDataFailedWithErrorCode(int code) {
						stopProgressDialog();
						notifyRequestFailed("请求错误，错误代码 ： %d， 请联系客服", code);
					}
					/**
					 * 因为网络问题而导致请求失败时，会回调这个接口（注意:本接口不在UI线程中执行， 所以请不要在本接口中进行UI线程方面的操作）
					 */
					@Override
					public void onLoadAppDetailDataFailed() {
						stopProgressDialog();
						notifyRequestFailed("请求失败，请检查网络");
					}
				});

		//		// 同步加载方式
		//		new Thread(new Runnable() {
		//			@Override
		//			public void run() {
		//				try {
		//					AppDetailObject data = DiyOfferWallManager.getInstance(OfferWallAdDetailActivity
		//							.this).getAppDetailData(appSumObject);
		//					updateView(data);
		//				} catch (NetworkException e) {
		//					Log.e("YoumiSdk", "", e);
		//					notifyRequestFailed("请求失败，请检查网络");
		//				} catch (ErrorCodeException e) {
		//					Log.e("YoumiSdk", "", e);
		//					notifyRequestFailed("请求错误，错误代码 ： %d， 请联系客服", e.getErrCode());
		//				}
		//			}
		//		}).start();
	}

	private void notifyRequestFailed(final String format, final Object... args) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
				new AlertDialog.Builder(OfferWallAdDetailActivity.this).setTitle("请求失败").setMessage(String.format(format, args))
						.create().show();
			}
		});
	}

	/**
	 * 更新按钮状态
	 */
	private void updateOpenOrDownloadButtonStatus(int status) {
		switch (status) {
		case AdTaskStatus.NOT_COMPLETE: // 未完成
			openOrDownloadBtn.setEnabled(true);
			openOrDownloadBtn.setText(isPackageExist ? "任务未完成，打开体验" : "下载安装");
			break;
		case AdTaskStatus.HAS_EXTRA_TASK: // 有追加任务
			openOrDownloadBtn.setEnabled(true);
			boolean isExtraTaskCanDo = false; // 标记追加任务现在是否可以进行
			for (int i = 0; i < appSumObject.getExtraTaskList().size(); ++i) {
				if (AdExtraTaskStatus.IN_PROGRESS ==
				    appSumObject.getExtraTaskList().get(i).getStatus()) {
					isExtraTaskCanDo = true;
					break;
				}
			}
			openOrDownloadBtn.setText(isPackageExist ? (isExtraTaskCanDo ? "任务未完成，打开体验" : "任务等待中") : "下载安装");
			break;
		case AdTaskStatus.ALREADY_COMPLETE: // 已完成
			openOrDownloadBtn.setEnabled(true);
			openOrDownloadBtn.setText(isPackageExist ? "任务已完成，打开" : "重新安装");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_detailpage_open_or_install:
			if (appDetailObject != null) {

				// 如果应用已经安装了，就统一使用有米sdk的打开api
				// 如果应用还没有安装的话,需要判断选用哪种下载方式，
				// 1、调用sdk的下载api进行下载（适用于国内app）
				// 2、跳转到googleplay进行下载（使用与国外app）
				if (!isPackageExist) {
					// 如果googleplay的跳转url不为空，就用用外部浏览器打开googleplay的url进行跳转
					String googlePlayUrl = appDetailObject.getGoogleAdDownloadUrl();
					if (googlePlayUrl != null && googlePlayUrl.trim().length() > 0) {
//						Util.tryToOpenGooglePlayToDownload(this, googlePlayUrl);
						//TODO
						break;
					}
				}
				DiyOfferWallManager.getInstance(this).openOrDownloadApp(this, appDetailObject);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 构造任务描述列表
	 *
	 * @return
	 */
	private void generateDestList() {
		if (mTaskDescList == null) {
			mTaskDescList = new ArrayList<TaskDescObject>();
		}

		// 1、将正常的任务加入到描述列表中
		int status = 0;
		if (appDetailObject.getAdTaskStatus() == AdTaskStatus.ALREADY_COMPLETE ||
		    appDetailObject.getAdTaskStatus() == AdTaskStatus.HAS_EXTRA_TASK) {
			status = AdExtraTaskStatus.COMPLETE; // 标记任务已经完成
		} else if (appDetailObject.getAdTaskStatus() == AdTaskStatus.NOT_COMPLETE) {
			status = AdExtraTaskStatus.IN_PROGRESS; // 标记任务可以进行
		}
		TaskDescObject normalTask =
				new TaskDescObject(status, appDetailObject.getTaskSteps(), appDetailObject.getPoints());
		mTaskDescList.add(normalTask);

		// 2、将追加任务加入到描述列表中
		AppExtraTaskObjectList extraTastkList = appDetailObject.getExtraTaskList();
		if (extraTastkList != null && extraTastkList.size() > 0) {
			for (int i = 0; i < extraTastkList.size(); ++i) {
				AppExtraTaskObject extraTaskObject = extraTastkList.get(i);
				TaskDescObject temp =
						new TaskDescObject(extraTaskObject.getStatus(), extraTaskObject.getAdText(),
								extraTaskObject.getPoints());
				mTaskDescList.add(temp);
			}
		}
	}

	/**
	 * 更新视图
	 *
	 * @param
	 */
	private void updateView(final AppDetailObject detailData) {
		if (detailData != null) {
			this.appDetailObject = detailData;
			// 这里生成一下描述列表
			generateDestList();
			// 当获取到数据的时候，先设置默认图片，等后续图片下载完毕之后在更新
			bmLists = new ArrayList<Bitmap>();
			for (int i = 0; i < appDetailObject.getScreenShotUrls().length; i++) {
				bmLists.add(BitmapFactory.decodeResource(getResources(), R.mipmap.icons));
			}
			// 在主线程中更新数据
			handler.post(new Runnable() {
				@Override
				public void run() {
					openOrDownloadBtn.setVisibility(View.VISIBLE);
					appName.setText(appDetailObject.getAppName());
					appVersion.setText("版本号：" + appDetailObject.getVersionName());
					appSize.setText("大小：" + appDetailObject.getAppSize());
					appStyle.setText(appDetailObject.getAppCategory());
					appDesc.setText(appDetailObject.getDescription());
					updateOpenOrDownloadButtonStatus(appDetailObject.getAdTaskStatus());
					updateGridView(bmLists);
					updateListView(mTaskDescList);
					mSwipeRefreshLayout.setRefreshing(false);
					rewardCount.setText("今天已有 " + appDetailObject.getRewardsCount() + " 个用户获得奖励");
				}
			});

			// 构造需要加载的图片url数组，当图片加载完毕的时候更新显示
			// 1、传入图标url
			int ssUrlsLength = appDetailObject.getScreenShotUrls().length;
			String[] imageUrlArray = new String[ssUrlsLength + 1];
			imageUrlArray[0] = appDetailObject.getIconUrl();

			// 2、传入截图url
			if (appDetailObject.getScreenShotUrls() != null) {
				System.arraycopy(appDetailObject.getScreenShotUrls(), 0, imageUrlArray, 1, ssUrlsLength); // 传入截图地址
			}

			// 线程池异步加载图片
			BitmapLoaderManager.loadBitmap(this, this, imageUrlArray);
		}
	}

	@Override
	public void onLoadBitmap(String url, final Bitmap bm) {
		try {
			if (url.equals(appDetailObject.getIconUrl())) { // 显示app图标
				handler.post(new Runnable() {

					@Override
					public void run() {
						appIcon.setImageBitmap(bm);
					}
				});
			}
			for (int i = 0; i < appDetailObject.getScreenShotUrls().length; i++) { // 显示app截图
				if (url.equals(appDetailObject.getScreenShotUrls()[i])) {
					bmLists.set(i, bm);
					gvAdapter.setData(bmLists);
					handler.post(new Runnable() {

						@Override
						public void run() {
							gvAdapter.notifyDataSetChanged();
						}
					});
					break;
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	/**
	 * 更新gridview显示app截图
	 *
	 * @param bmLists
	 */
	private void updateGridView(ArrayList<Bitmap> bmLists) {
		if (bmLists != null && !bmLists.isEmpty()) {
			int colWidth = getResources().getDisplayMetrics().widthPixels / 2;
			LayoutParams params =
					new LayoutParams(bmLists.size() * colWidth, LayoutParams.WRAP_CONTENT);
			gridView.setLayoutParams(params);
			gridView.setColumnWidth(colWidth);
			gridView.setHorizontalSpacing(6);
			gridView.setStretchMode(GridView.NO_STRETCH);
			gridView.setNumColumns(bmLists.size());
			gridView.setVisibility(View.VISIBLE);
			gvAdapter.setData(bmLists);
			gvAdapter.notifyDataSetChanged();
		} else {
			gridView.setVisibility(View.GONE);
		}
	}

	/**
	 * 更新listview显示任务描述
	 *
	 * @param
	 */
	private void updateListView(List<TaskDescObject> list) {
		if (list != null && !list.isEmpty()) {
			listView.setVisibility(View.VISIBLE);
			lvAdapter.setData(list);
			lvAdapter.notifyDataSetChanged();
		} else {
			listView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDownloadStart(int id) {

	}

	@Override
	public void onDownloadFailed(int id) {
		try {
			if (appDetailObject == null) {
				return;
			}

			if (appDetailObject.getAdId() != id) {
				return;
			}
			this.downlaodProgressBar.setProgress(0);
			this.downlaodProgressBar.setVisibility(View.GONE);
			this.openOrDownloadBtn.setEnabled(true);
			this.openOrDownloadBtn.setText("下载失败,请稍候重试!");
		} catch (Throwable e) {
			Log.d("Youmi", "", e);
		}
	}

	@Override
	public void onDownloadSuccess(int id) {
		try {
			if (appDetailObject == null) {
				return;
			}

			if (appDetailObject.getAdId() != id) {
				return;
			}
			this.downlaodProgressBar.setProgress(0);
			this.downlaodProgressBar.setVisibility(View.GONE);
			this.openOrDownloadBtn.setEnabled(true);
			this.openOrDownloadBtn.setText("下载成功,请安装!");
		} catch (Throwable e) {
			Log.d("Youmi", "", e);
		}
	}

	@Override
	public void onDownloadProgressUpdate(int id, long contentLength, long completeLength, int percent, long speedBytesPerS) {
		try {
			if (appDetailObject == null) {
				return;
			}

			if (appDetailObject.getAdId() != id) {
				return;
			}
			this.downlaodProgressBar.setVisibility(View.VISIBLE);
			this.downlaodProgressBar.setProgress(percent);
			this.openOrDownloadBtn.setEnabled(false);
			this.openOrDownloadBtn.setText(String.format("正在下载,已完成%d%% ,下载速度: %dKB/s", percent, (speedBytesPerS / 1024)));
		} catch (Throwable e) {
			Log.d("Youmi", "", e);
		}

	}

	@Override
	public void onInstallSuccess(int id) {
		try {
			if (appDetailObject == null) {
				return;
			}

			if (appDetailObject.getAdId() != id) {
				return;
			}
			this.downlaodProgressBar.setProgress(0);
			this.downlaodProgressBar.setVisibility(View.GONE);
			this.openOrDownloadBtn.setEnabled(true);
			this.openOrDownloadBtn.setText("已安装成功,打开");
		} catch (Throwable e) {
			Log.d("Youmi", "", e);
		}
	}

}
