package Fragments.JingDianTask;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Constance.constance;
import Fragments.BaseFragment;
import Interfaces.BitmapDownloadListener;
import Manager.BitmapLoaderManager;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import Views.RefreshLayout;
import Views.XListView.XListView;
import activity.AllProfitActivity;
import activity.FaskTaskDetailActivity;
import activity.OfferWallAdDetailActivity;
import activity.YaoQingSharesActivity;
import adapter.ListViewAdapter;
import adapter.TaskAdapter;
import adapter.ZhuanshuAdapter;
import model.CustomObject;
import model.FaskTask.faskTask;
import model.FaskTask.task;
import model.Result;
import model.TaskState;
import tj.zl.op.AdManager;
import tj.zl.op.os.OffersManager;
import tj.zl.op.os.PointsManager;
import tj.zl.op.os.df.AdExtraTaskStatus;
import tj.zl.op.os.df.AppExtraTaskObject;
import tj.zl.op.os.df.AppExtraTaskObjectList;
import tj.zl.op.os.df.AppSummaryDataInterface;
import tj.zl.op.os.df.AppSummaryObject;
import tj.zl.op.os.df.AppSummaryObjectList;
import tj.zl.op.os.df.DiyOfferWallManager;
/**
 */
public class ZhuanShuFragment extends BaseFragment implements BitmapDownloadListener {
    private ArrayList<faskTask> mTask=new ArrayList<>();
    private ListView mListTask;
    private final int REFRESH=1;
    private final int LOADMORE=2;
    private TaskAdapter mTaskAdapter;
    private String RunningTask="";
    private ZhuanshuAdapter mLvAdapter;
    private int mPageIndex = 1;//请求页码
    private RefreshLayout mSwipeRefreshLayout;
    public ZhuanShuFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_jinadian,null);
        initview(layout);
        inityoumi();
        initdate();
        initevent();
        return layout;
    }
    /**
     * 初始化有米sdk
     */
    private void inityoumi() {
        // 初始化接口，应用启动的时候调用，参数：appId, appSecret, isTestMode, isEnableYoumiLog
        AdManager.getInstance(getActivity()).init("89c4d15bcfe42455", "5929d7270930ad09", false, true);
        // (可选) 检查积分墙源数据配置
        DiyOfferWallManager.getInstance(getActivity()).checkDiyOffersAdConfig();
        // (可选) 服务器回调积分配置
        OffersManager.getInstance(getActivity()).setCustomUserId(SharePre.getUserId(getActivity()));
        OffersManager.getInstance(getActivity()).setUsingServerCallBack(true);
        // 请务必调用以下代码，告诉积分墙源数据SDK应用启动，可以让SDK进行一些初始化操作。该接口务必在SDK的初始化接口之后调用。
        DiyOfferWallManager.getInstance(getActivity()).onAppLaunch();
        // （可选）注册积分余额变动监听-随时随地获得积分的变动情况
//        PointsManager.getInstance(getActivity()).registerNotify(this);
        // （可选）注册广告下载安装监听-随时随地获得应用下载安装状态的变动情况
        DiyOfferWallManager.getInstance(getActivity()).registerListener(mLvAdapter);
        // 发起列表请求
    }
    /**
     * 初始化数据，，下拉刷新
     */
    private void initdate() {
        mPageIndex = 1;
        mLvAdapter.reset();
        requestList(0);
    }
    /**
     * 发起列表请求
     */
    private int mRequestType;
    private final static int AD_PER_NUMBER = 10;
    private void requestList(final int type) {
        // 获取指定类型 的广告，并更新listview，下面展示两种加载方式，开发者可选择适合自己的方式
        // 异步加载方式
        // 请求类型，页码，请求数量，回调接口
        startProgressDialog("加载中...");
        DiyOfferWallManager.getInstance(getActivity())
                .loadOfferWallAdList(mRequestType, mPageIndex, AD_PER_NUMBER, new AppSummaryDataInterface() {
                    /**
                     * 当成功获取到积分墙列表数据的时候，会回调这个方法（注意:本接口不在UI线程中执行， 所以请不要在本接口中进行UI线程方面的操作）
                     * 注意：列表数据有可能为空（比如：没有广告的时候），开发者处理之前，请先判断列表是否为空，大小是否大与0
                     */
                    @Override
                    public void onLoadAppSumDataSuccess(Context context, AppSummaryObjectList adList) {
                        stopProgressDialog();
                        Log.i("有没任务列表",""+adList.toString());
                        updateListView(adList,type);
                        if (adList != null) {
                            updateLimitInfo(adList.getInstallLimit(), adList.getInstallTimes(),type);
                        }
                    }
                    /**
                     * 因为网络问题而导致请求失败时，会回调这个接口（注意:本接口不在UI线程中执行， 所以请不要在本接口中进行UI线程方面的操作）
                     */
                    @Override
                    public void onLoadAppSumDataFailed() {
                        stopProgressDialog();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 如果是请求第一页的时候，请求失败，就致列表为空
                                if (mPageIndex == 1) {
                                    mLvAdapter.reset();
                                    mLvAdapter.notifyDataSetChanged();
                                }
                                mSwipeRefreshLayout.setRefreshing(false);
                                mSwipeRefreshLayout.setPushRefreshing(false);
                                Toast.makeText(getActivity(), "请求失败，请检查网络～", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    /**
                     * 请求成功，但是返回有米错误代码时候，会回调这个接口（注意:本接口不在UI线程中执行， 所以请不要在本接口中进行UI线程方面的操作）
                     */
                    @Override
                    public void onLoadAppSumDataFailedWithErrorCode(final int code) {
                        stopProgressDialog();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 如果是请求第一页的时候，请求失败，就致列表为空
                                if (mPageIndex == 1) {
                                    mLvAdapter.reset();
                                    mLvAdapter.notifyDataSetChanged();
                                }
                                mSwipeRefreshLayout.setRefreshing(false);
                                mSwipeRefreshLayout.setPushRefreshing(false);
                                Toast.makeText(getActivity(), String.format("请求失败,请联系客服", code),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }
    /**
     * 更新用户当前可做的任务数
     * <p/>
     * 计算当前还可以做多少个处于《未完成状态》广告：安装上限-今天已经安装过的数量
     *
     * @param installLimit 当天新任务安装限制
     * @param installTimes 当天已经完成的新任务数量
     */
    private void updateLimitInfo(final int installLimit, final int installTimes, final int type) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type==REFRESH){
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setPushRefreshing(false);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("可做任务数：");
                sb.append(installLimit - installTimes);
                updateLimitInfo(sb.toString());
            }
        });
    }
    protected MenuItem mPointsItem, mLimitItem;
    protected void updateLimitInfo(String msg) {
        if (mLimitItem != null) {
            mLimitItem.setTitle(msg);
        }
    }
    private void initview(View layout) {
        mRequestType=DiyOfferWallManager.REQUEST_ALL;
        mSwipeRefreshLayout = (RefreshLayout) layout.findViewById(R.id.sr_ad_list);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setPushRefreshing(false);
//               mPageIndex = 1;
//                mLvAdapter.reset();
//                mSwipeRefreshLayout.setRefreshing(true);
//                requestList(REFRESH);
            }
        });
        mSwipeRefreshLayout.setOnPushRefreshListener(new RefreshLayout.OnPushRefreshListener() {
            @Override
            public void onPushRefresh() {
                ++mPageIndex;
                mSwipeRefreshLayout.setPushRefreshing(true);
                requestList(LOADMORE);
            }
        });
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mSwipeRefreshLayout
                .setColorSchemeColors(Color.parseColor("#ff00ddff"), Color.parseColor("#ff99cc00"), Color.parseColor
                                ("#ffffbb33"),
                        Color.parseColor("#ffff4444"));
        mListTask = (ListView) layout.findViewById(R.id.list_tassk);
        mTaskAdapter = new TaskAdapter(getActivity(), mTask);
        View headLayout = View.inflate(getActivity(), R.layout.item_top_task, null);
        View Top_items = headLayout.findViewById(R.id.ll_top_items);
        View title_st=headLayout.findViewById(R.id.rtl_title_jd);
        View title_rm=headLayout.findViewById(R.id.rtl_title_rm);
//        mListTask.addHeaderView(headLayout);
        mLvAdapter = new ZhuanshuAdapter(getActivity(), null);
        mListTask.setAdapter(mLvAdapter);
        Top_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), YaoQingSharesActivity.class);
                startActivity(intent);
            }
        });
        title_rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        title_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void initevent() {
        mListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=mLvAdapter.getCount()+1){
                    Intent intent = new Intent(getActivity(), OfferWallAdDetailActivity.class);
                    intent.putExtra("ad", mLvAdapter.getItem(position).getAppSummaryObject());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
        }
    }
    /**
     * 更新listview
     *
     * @param adList
     */
    private void updateListView(final AppSummaryObjectList adList, final int type) {
        if (adList == null || adList.isEmpty()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type==LOADMORE){
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setPushRefreshing(false);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setPushRefreshing(false);
                    Toast.makeText(getActivity(), "没有获取到更多任务", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            ArrayList<CustomObject> customObjectArrayList = new ArrayList<CustomObject>();
            for (int k = 0; k < adList.size(); ++k) {
                // 如果请求的是追加任务的列表，demo将会把所有的追加任务独立为一个item项，因此需要把同一个appSummaryObject多次加入到列表中
                if (mRequestType == DiyOfferWallManager.REQUEST_EXTRA_TASK) {
                    // 下面是判断是否追加任务，如果是的话就会在写入一次列表
                    AppSummaryObject appSummaryObject = adList.get(k);
                    AppExtraTaskObjectList extraTaskObjectList = appSummaryObject.getExtraTaskList();
                    for (int j = 0; j < extraTaskObjectList.size(); ++j) {
                        AppExtraTaskObject extraTaskObject = extraTaskObjectList.get(j);
                        if (extraTaskObject.getStatus() == AdExtraTaskStatus.NOT_START ||
                                extraTaskObject.getStatus() == AdExtraTaskStatus.IN_PROGRESS) {
                            CustomObject customObject = new CustomObject();
                            customObject.setAppSummaryObject(adList.get(k));
                            customObject.setAppicon(null);
                            customObject.setShowMultSameAd(true);
                            customObject.setShowExtraTaskIndex(j);
                            customObjectArrayList.add(customObject);
                        }
                    }
                } else {
                    CustomObject customObject = new CustomObject();
                    customObject.setAppSummaryObject(adList.get(k));
                    customObject.setAppicon(null);
                    customObjectArrayList.add(customObject);
                }
            }
            mLvAdapter.addData(customObjectArrayList);
            // 获取到数据之后向ui线程中handler发送更新view的信息（这里先显示文字信息，后续加载图片）
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type==LOADMORE){
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setPushRefreshing(false);
                    }
                    mLvAdapter.notifyDataSetChanged();
//                    Toast.makeText(getActivity(),
//                            String.format("请求成功\n请求页码：%s\n请求数量：%s\n实际返回数量:%s\n当天新任务限制:%s\n已安装的新任务数:%s", adList.getPageIndex(),
//                                    adList.getPerPageNumber(), adList.size(), adList.getInstallLimit(), adList.getInstallTimes
//                                            ()),
//                            Toast.LENGTH_LONG).show();
                }
            });
            // 获取需要加载的图片url地址，然后加载
            String[] iconUrlArray = new String[customObjectArrayList.size()];
            for (int i = 0; i < customObjectArrayList.size(); i++) {
                iconUrlArray[i] = customObjectArrayList.get(i).getAppSummaryObject().getIconUrl();
            }
            // 线程池异步加载图片
            //TODO
            BitmapLoaderManager.loadBitmap(getActivity(),this, iconUrlArray);
        }
    }
    /**
     * 每当有一张图片加载完成的时候就会回调这个接口（本接口在非UI线程中调用）
     */
    @Override
    public void onLoadBitmap(String url, Bitmap bm) {
        try {
            if (mLvAdapter == null || mLvAdapter.getData() == null || mLvAdapter.getData().isEmpty()) {
                return;
            }
            for (int i = 0; i < mLvAdapter.getData().size(); i++) { // 显示app_icon
                if (url.equals(mLvAdapter.getItem(i).getAppSummaryObject().getIconUrl())) {
                    mLvAdapter.getItem(i).setAppicon(bm);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLvAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
