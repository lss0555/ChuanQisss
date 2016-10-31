package activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;

import Fragments.GetFragment;
import Fragments.HomeFragment;
import Fragments.JingDianTask.JingDiansFragment;
import Fragments.JingDianTask.ZhuanShuFragment;
import Fragments.MineFragment;
import Fragments.ShareFragment;
import Utis.GsonUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Constance.constance;
import Utis.Utis;
import Utis.SharePre;
import Views.UnSlideViewPager;
import Views.XListView.XListView;
import adapter.TaskAdapter;
import Utis.OkHttpUtil;
import model.FaskTask.faskTask;
import model.FaskTask.task;
import model.Result;
import model.TaskState;
/**
 * Created by lss on 2016/7/25.
 */
public class FaskTaskActivity extends  BaseActivity{
    private XListView mListTask;
    private ArrayList<faskTask> mTask=new ArrayList<>();
    private TaskAdapter mTaskAdapter;
    private String RunningTask="";
    private TextView mTvPhoneType;
    private TextView mTvJdState;
    private TextView mTvZsState;
    private Fragment  JingDianFragment;
    private  int mCurentPageIndex;//当前的页数
    private JingDiansFragment jingDianFragment;
    private ZhuanShuFragment zhuanShuFragment;
    private Fragment  ZhuanShuFragment;
    private RelativeLayout mRtlJd;
    private RelativeLayout mRtlZs;
    private UnSlideViewPager mVpTabs;
    private ArrayList<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initview();
        initPage();
        initevent();
    }
    /**
     * 初始化页面
     */
    private void initPage() {
        mVpTabs.setOffscreenPageLimit(4);
        mFragments=new ArrayList<Fragment>();
        if(jingDianFragment==null){
            mFragments.add(new JingDiansFragment());
        }else {
            mFragments.add(jingDianFragment);
        }
        if(zhuanShuFragment==null){
            mFragments.add(new ZhuanShuFragment());
        }else {
            mFragments.add(zhuanShuFragment);
        }
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }
            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mVpTabs.setAdapter(mAdapter);
        mVpTabs.setCurrentItem(0);
        mVpTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                ResetTab();
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                }
                mCurentPageIndex = position;
            }

            /**
             * 重置状态
             */
            private void ResetTab() {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void initview() {
        mVpTabs = (UnSlideViewPager) findViewById(R.id.vp_tabs);
        mTvJdState = (TextView) findViewById(R.id.tv_jd_state);
        mTvZsState = (TextView) findViewById(R.id.tv_zs_state);
        mRtlJd = (RelativeLayout) findViewById(R.id.rtl_jd);
        mRtlZs = (RelativeLayout) findViewById(R.id.rtl_zs);
    }
    private void initevent() {
        /**
         * 经典
         */
        mRtlJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mTvJdState.setTextColor(getResources().getColor(R.color.red));
               mTvZsState.setTextColor(getResources().getColor(R.color.white));
                mRtlJd.setBackground(getResources().getDrawable(R.color.white));
                mRtlZs.setBackground(getResources().getDrawable(R.color.red));
                mVpTabs.setCurrentItem(0);
            }
        });
        /**
         * 专属
         */
        mRtlZs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvJdState.setTextColor(getResources().getColor(R.color.white));
                mTvZsState.setTextColor(getResources().getColor(R.color.red));
                mRtlJd.setBackground(getResources().getDrawable(R.color.red));
                mRtlZs.setBackground(getResources().getDrawable(R.color.white));
                mVpTabs.setCurrentItem(1);
            }
        });
        /**
         * 帮助
         */
        findViewById(R.id.tv_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),TaskHelpCenterActivity.class);
                startActivity(intent);
            }
        });
    }
}
