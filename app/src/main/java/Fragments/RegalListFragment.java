package Fragments;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.UILUtils;
import Views.CircleImageView;
import Views.XListView.XListView;
import adapter.AllRichListAdapter;
import adapter.RichListAdapter;
import adapter.WeekRichListAdapter;
import model.AllRichList.all;
import model.DayRichList.Dayrichlist;
import model.RichList;
import model.WeekRichList.weeklist;

/**
 */
public class RegalListFragment extends BaseFragment implements View.OnClickListener{
    private ArrayList<RichList> mDate=new ArrayList<>();
    private ArrayList<String> mRewardType=new ArrayList<String>();
    private RelativeLayout mRtlDay;
    private RelativeLayout mRtlWeek;
    private RelativeLayout mRtlAll;
    private TextView mTvDay;
    private TextView mTvWeek;
    private TextView mTvAll;
    private ListView mList;
    private RichListAdapter richListAdapter;
    private CircleImageView mImgHead1;
    private CircleImageView mImgHead2;
    private CircleImageView mImgHead3;
    private TextView mTvId1;
    private TextView mTvId2;
    private TextView mTvId3;
    private TextView mTvGet1;
    private TextView mTvGet2;
    private TextView mTvGet3;
    private WeekRichListAdapter weekrichListAdapter;
    private AllRichListAdapter allrichListAdapter;
    public RegalListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_regal_list,null);
        initview(layout);
        initDate();
        return layout;
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        initDayRichList();
    }
    /**
     * 富豪总榜
     */
    private void initAllRichList() {
        OkHttpUtil.getInstance().Post(null, constance.URL.RICH_ALL, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    all all = GsonUtils.parseJSON(data, all.class);
                    mDate.clear();
                    mDate.addAll(all.getAll());
                    mList.setAdapter(allrichListAdapter);
                    allrichListAdapter.notifyDataSetChanged();
                    UILUtils.displayImageNoAnim(all.getAll().get(0).getHeadportrait(),mImgHead1);
                    UILUtils.displayImageNoAnim(all.getAll().get(1).getHeadportrait(),mImgHead2);
                    UILUtils.displayImageNoAnim(all.getAll().get(2).getHeadportrait(),mImgHead3);
                    mTvId1.setText("ID:"+all.getAll().get(0).getUserid());
                    mTvId2.setText("ID:"+all.getAll().get(1).getUserid());
                    mTvId3.setText("ID:"+all.getAll().get(2).getUserid());
                    mTvGet1.setText(all.getAll().get(0).getJine()+"元");
                    mTvGet2.setText(all.getAll().get(1).getJine()+"元");
                    mTvGet3.setText(all.getAll().get(2).getJine()+"元");
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    /**
     * 富豪周排行榜
     */
    private void initWeekRichList() {
        OkHttpUtil.getInstance().Post(null, constance.URL.RICH_WEEK, new OkHttpUtil.FinishListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                if(IsSuccess){
                    mDate.clear();
                    mRewardType.clear();
                    JSONObject allObj = null;
                    String [] arr = new String[50];
                    try {
                        allObj = new JSONObject(data);
                        JSONArray jsonArray = allObj.getJSONArray("thisweektdcount");
                        Log.i("========奖励数组",""+jsonArray.opt(0).toString());
                        String typeJson= jsonArray.opt(0).toString();
                        JsonReader reader = new JsonReader(new StringReader(typeJson));
                        try {
                            	reader.beginArray();// 开始解析数组
                                	while (reader.hasNext()) {
                                       mRewardType.add(reader.nextString());
                                    	}
                            	reader.endArray();// 结束数组解析
                            	} catch (IOException e) {
                            	e.printStackTrace();
                            	}
                        Log.i("=====类型数组大小",""+mRewardType.size());
                        for(int i =1;i<jsonArray.length();i++){
                            JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
                            RichList richList = new RichList();
                            richList.setJine(jsonObject2.getString("jine"));
                            richList.setTdcount(jsonObject2.getString("tdcount"));
                            richList.setHeadportrait(jsonObject2.getString("Headportrait"));
                            richList.setUserid(jsonObject2.getString("userid"));
                            mDate.add(richList);
                        }
                        mList.setAdapter(weekrichListAdapter);
                        weekrichListAdapter.notifyDataSetChanged();
                        UILUtils.displayImageNoAnim(mDate.get(0).getHeadportrait(),mImgHead1);
                    UILUtils.displayImageNoAnim(mDate.get(1).getHeadportrait(),mImgHead2);
                    UILUtils.displayImageNoAnim(mDate.get(2).getHeadportrait(),mImgHead3);
                    mTvId1.setText("ID:"+mDate.get(0).getUserid());
                    mTvId2.setText("ID:"+mDate.get(1).getUserid());
                    mTvId3.setText("ID:"+mDate.get(2).getUserid());
                    mTvGet1.setText(mDate.get(0).getJine()+"元");
                    mTvGet2.setText(mDate.get(1).getJine()+"元");
                    mTvGet3.setText(mDate.get(2).getJine()+"元");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    weeklist weeklist = GsonUtils.parseJSON(data, weeklist.class);
//                    ArrayList<RichList> thisweektdcount = weeklist.getThisweektdcount();
//                    thisweektdcount.remove(thisweektdcount.get(0));
//                    mDate.clear();
//                    mDate.addAll(thisweektdcount);
//                    mList.setAdapter(weekrichListAdapter);
//                    weekrichListAdapter.notifyDataSetChanged();
//                    UILUtils.displayImageNoAnim(weeklist.getThisweektdcount().get(0).getHeadportrait(),mImgHead1);
//                    UILUtils.displayImageNoAnim(weeklist.getThisweektdcount().get(1).getHeadportrait(),mImgHead2);
//                    UILUtils.displayImageNoAnim(weeklist.getThisweektdcount().get(2).getHeadportrait(),mImgHead3);
//                    mTvId1.setText("ID:"+weeklist.getThisweektdcount().get(0).getUserid());
//                    mTvId2.setText("ID:"+weeklist.getThisweektdcount().get(1).getUserid());
//                    mTvId3.setText("ID:"+weeklist.getThisweektdcount().get(2).getUserid());
//                    mTvGet1.setText(weeklist.getThisweektdcount().get(0).getJine()+"元");
//                    mTvGet2.setText(weeklist.getThisweektdcount().get(1).getJine()+"元");
//                    mTvGet3.setText(weeklist.getThisweektdcount().get(2).getJine()+"元");
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    /**
     * 富豪日排行榜
     */
    private void initDayRichList() {
        OkHttpUtil.getInstance().Post(null, constance.URL.RICH_DAY, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                Toast(data.toString());
                if(IsSuccess){
                    Dayrichlist dayrichlist = GsonUtils.parseJSON(data, Dayrichlist.class);
                    mDate.clear();
                    mDate.addAll(dayrichlist.getDaterich());
                    mList.setAdapter(richListAdapter);
                    richListAdapter.notifyDataSetChanged();
                    UILUtils.displayImageNoAnim(dayrichlist.getDaterich().get(0).getHeadportrait(),mImgHead1);
                    UILUtils.displayImageNoAnim(dayrichlist.getDaterich().get(1).getHeadportrait(),mImgHead2);
                    UILUtils.displayImageNoAnim(dayrichlist.getDaterich().get(2).getHeadportrait(),mImgHead3);
                    mTvId1.setText("ID:"+dayrichlist.getDaterich().get(0).getUserid());
                    mTvId2.setText("ID:"+dayrichlist.getDaterich().get(1).getUserid());
                    mTvId3.setText("ID:"+dayrichlist.getDaterich().get(2).getUserid());
                    mTvGet1.setText(dayrichlist.getDaterich().get(0).getJine()+"元");
                    mTvGet2.setText(dayrichlist.getDaterich().get(1).getJine()+"元");
                    mTvGet3.setText(dayrichlist.getDaterich().get(2).getJine()+"元");
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    private void initview(View layout) {
        mImgHead1 = (CircleImageView) layout.findViewById(R.id.img_head1);
        mImgHead2 = (CircleImageView) layout.findViewById(R.id.img_head2);
        mImgHead3 = (CircleImageView) layout.findViewById(R.id.img_head3);
        mTvId1 = (TextView) layout.findViewById(R.id.tv_id1);
        mTvId2 = (TextView) layout.findViewById(R.id.tv_id2);
        mTvId3 = (TextView) layout.findViewById(R.id.tv_id3);
        mTvGet1 = (TextView) layout.findViewById(R.id.tv_get1);
        mTvGet2 = (TextView) layout.findViewById(R.id.tv_get2);
        mTvGet3 = (TextView) layout.findViewById(R.id.tv_get3);

        mList = (ListView) layout.findViewById(R.id.list);
        mRtlDay = (RelativeLayout) layout.findViewById(R.id.rtl_list_day);
        mRtlWeek = (RelativeLayout) layout.findViewById(R.id.rtl_list_week);
        mRtlAll = (RelativeLayout) layout.findViewById(R.id.rtl_list_all);
        mTvDay = (TextView) layout.findViewById(R.id.tv_day);
        mTvWeek = (TextView) layout.findViewById(R.id.tv_week);
        mTvAll = (TextView) layout.findViewById(R.id.tv_all);
        mRtlDay.setOnClickListener(this);
        mRtlWeek.setOnClickListener(this);
        mRtlAll.setOnClickListener(this);
        richListAdapter = new RichListAdapter(getActivity(),mDate);
        weekrichListAdapter = new WeekRichListAdapter(getActivity(),mDate,mRewardType);
        allrichListAdapter = new AllRichListAdapter(getActivity(),mDate);
        mList.setAdapter(richListAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rtl_list_day:
                initDayRichList();
                mTvDay.setTextColor(getResources().getColor(R.color.red));
                mTvWeek.setTextColor(getResources().getColor(R.color.white));
                mTvAll.setTextColor(getResources().getColor(R.color.white));
                mRtlDay.setBackground(getResources().getDrawable(R.drawable.round_left_half_white_bg));
                mRtlWeek.setBackground(getResources().getDrawable(R.color.red));
                mRtlAll.setBackground(getResources().getDrawable(R.drawable.round_half_red_bg));
                break;
            case R.id.rtl_list_week:
                initWeekRichList();
                mTvDay.setTextColor(getResources().getColor(R.color.white));
                mTvWeek.setTextColor(getResources().getColor(R.color.red));
                mTvAll.setTextColor(getResources().getColor(R.color.white));
                mRtlDay.setBackground(getResources().getDrawable(R.drawable.round_left_half_red_bg));
                mRtlWeek.setBackground(getResources().getDrawable(R.color.white));
                mRtlAll.setBackground(getResources().getDrawable(R.drawable.round_half_red_bg));
                break;
            case R.id.rtl_list_all:
                initAllRichList();
                mTvDay.setTextColor(getResources().getColor(R.color.white));
                mTvWeek.setTextColor(getResources().getColor(R.color.white));
                mTvAll.setTextColor(getResources().getColor(R.color.red));
                mRtlDay.setBackground(getResources().getDrawable(R.drawable.round_left_half_red_bg));
                mRtlWeek.setBackground(getResources().getDrawable(R.color.red));
                mRtlAll.setBackground(getResources().getDrawable(R.drawable.round_half_white_bg));
                break;
        }
    }
}
