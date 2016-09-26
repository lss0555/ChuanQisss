package activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;

import Constance.constance;
import Fragments.MineFragment;
import Fragments.Share.CustomerShareFragment;
import Fragments.Share.PhotosFragment;
import Fragments.Share.ShowOrderShareFragment;
import Utis.SharePre;

public class YaoQingSharesActivity extends BaseActivity implements View.OnClickListener{
    private WebView mWeb;
    private RelativeLayout mRtlShowOrder;
    private RelativeLayout mRtlCustomer;
    private RelativeLayout mRtlPhoto;
    private TextView mTvShowOrder;
    private TextView mTvCustomer;
    private TextView mTvPhoto;
    private Fragment photosFragment;
    private Fragment customerShareFragment;
    private Fragment showOrderShareFragment;
    private Fragment fragment;
    private Fragment mCurrentFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_yaoqingshares);
        initview();
    }

    private void initview() {
        mRtlPhoto = (RelativeLayout) findViewById(R.id.rtl_photo);
        mRtlCustomer = (RelativeLayout) findViewById(R.id.rtl_customer);
        mRtlShowOrder = (RelativeLayout) findViewById(R.id.rtl_show_order);
        mTvPhoto = (TextView) findViewById(R.id.tv_photo);
        mTvCustomer = (TextView) findViewById(R.id.tv_customer);
        mTvShowOrder = (TextView) findViewById(R.id.tv_show_order);
        mRtlPhoto.setOnClickListener(this);
        mRtlCustomer.setOnClickListener(this);
        mRtlShowOrder.setOnClickListener(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_main, new PhotosFragment());
        ft.commit();
    }
        //隐藏fragment
        public void hideFragment(FragmentTransaction ft){
            if(photosFragment != null){
                ft.hide(photosFragment);
            }
            if(customerShareFragment != null){
                ft.hide(customerShareFragment);
            }
            if(showOrderShareFragment != null){
                ft.hide(showOrderShareFragment);
            }
        }
    public void showPhotoFragment() {
        if (!(fragment instanceof PhotosFragment)) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            //开始时把所有的fragment都进行隐藏
            hideFragment(ft);
            //如果这个fragment为空的话，就创建一个fragment，并且把它加到ft中去.如果不为空，就把它直接给显示出来
            if(photosFragment == null){
                photosFragment = new PhotosFragment();
                ft.add(R.id.fl_main, photosFragment);
            }else {
                ft.show(photosFragment);
            }
            //一定要记得提交
            ft.commit();
        }
        mCurrentFragment=photosFragment;
    }
    public void showCustomerFragment() {
        if (!(fragment instanceof CustomerShareFragment)) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            //开始时把所有的fragment都进行隐藏
            hideFragment(ft);
            //如果这个fragment为空的话，就创建一个fragment，并且把它加到ft中去.如果不为空，就把它直接给显示出来
            if(customerShareFragment == null){
                customerShareFragment = new CustomerShareFragment();
                ft.add(R.id.fl_main, customerShareFragment);
            }else {
                ft.show(customerShareFragment);
            }
            //一定要记得提交
            ft.commit();
        }
        mCurrentFragment=customerShareFragment;
    }
    public void showShowOrderFragment() {
        if (!(fragment instanceof ShowOrderShareFragment)) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            //开始时把所有的fragment都进行隐藏
            hideFragment(ft);
            //如果这个fragment为空的话，就创建一个fragment，并且把它加到ft中去.如果不为空，就把它直接给显示出来
            if(showOrderShareFragment == null){
                showOrderShareFragment = new ShowOrderShareFragment();
                ft.add(R.id.fl_main, showOrderShareFragment);
            }else {
                ft.show(showOrderShareFragment);
            }
            ft.commit();
        }
        mCurrentFragment=showOrderShareFragment;
    }
    /**
     * 更换Tab状态
     * @param mTextView
     */
    public void ChangeState(TextView mTextView){
//        Drawable nav_top=getResources().getDrawable(DrawbleId);
//        nav_top.setBounds(0, 0, nav_top.getMinimumWidth(), nav_top.getMinimumHeight());
//        mTextView.setCompoundDrawables(null, nav_top, null, null);
//        mTextView.setTextColor();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rtl_photo:
                showPhotoFragment();
                mTvPhoto.setTextColor(getResources().getColor(R.color.red));
                mTvCustomer.setTextColor(getResources().getColor(R.color.black));
                mTvShowOrder.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.rtl_customer:
                showCustomerFragment();
                mTvPhoto.setTextColor(getResources().getColor(R.color.black));
                mTvCustomer.setTextColor(getResources().getColor(R.color.red));
                mTvShowOrder.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.rtl_show_order:
                showShowOrderFragment();
                mTvPhoto.setTextColor(getResources().getColor(R.color.black));
                mTvCustomer.setTextColor(getResources().getColor(R.color.black));
                mTvShowOrder.setTextColor(getResources().getColor(R.color.red));
                break;
        }
    }
}
