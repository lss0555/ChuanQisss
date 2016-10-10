package Fragments.Share;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.chuanqi.yz.R;

import java.util.ArrayList;

import Fragments.BaseFragment;
import Fragments.ImgShowFragment;
import ViewPageAlpfer.AlphaPageTransformer;
import ViewPageAlpfer.RotateDownPageTransformer;
import ViewPageAlpfer.ZoomOutPageTransformer;
import Views.HorizontalListView;
import adapter.ImgShowSelectAdapter;
import model.Photos;

/**
 */
public class PhotosFragment extends BaseFragment {
    private ArrayList<Photos> mDate=new ArrayList<Photos>();

    private ViewPager mVpPhoto;
    private int NUM=10000;
    private HorizontalListView mListPhotos;
    private ImgShowSelectAdapter imgShowSelectAdapter;

    public PhotosFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_photos,null);
        initdate();
        initview(layout);
        initevent();
        return layout;
    }
    private void initevent() {
        imgShowSelectAdapter = new ImgShowSelectAdapter(getActivity(), mDate);
        imgShowSelectAdapter.setSelectState(4);
        mListPhotos.setAdapter(imgShowSelectAdapter);
        mListPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mVpPhoto.setCurrentItem(i);
                imgShowSelectAdapter.setSelectState(i);
                imgShowSelectAdapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * 初始化添加数据
     */
    private void initdate() {
        mDate.add(new Photos("0","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/699434b740a8dbd2803f8fd654fe7bbe.png"));
        mDate.add(new Photos("1","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/919a5b83403b6d9680296073c550d5cd.png"));
        mDate.add(new Photos("2","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/208b4b31407cc68480589643621cef6d.png"));
        mDate.add(new Photos("3","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/3bd83c5540447a658016ccd873da4474.png"));
        mDate.add(new Photos("4","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/e6eba8704051504a803881884556fa78.png"));
        mDate.add(new Photos("5","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/527db86c40621d22809e60cb494175b7.png"));
        mDate.add(new Photos("6","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/3cf28f8c406454a380949ecb9756fd64.png"));
        mDate.add(new Photos("7","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/1dc6e2b240091b1a80f48c0880b1732c.png"));
        mDate.add(new Photos("8","http://bmob-cdn-6460.b0.upaiyun.com/2016/10/09/fb80cce34020ee51805f6fed65b36dcc.png"));
    }
    private void initview(View layout) {
        mListPhotos = (HorizontalListView) layout.findViewById(R.id.list_img);
        mVpPhoto = (ViewPager) layout.findViewById(R.id.vp_photo);
        FragmentManager fm=getChildFragmentManager();
//        mVpPhoto.setPageMargin(50);
        mVpPhoto.setOffscreenPageLimit(5);
        mVpPhoto.setAdapter(new BannerAdpter(fm));
        mVpPhoto.setPageTransformer(true, new ZoomOutPageTransformer());
        mVpPhoto.setCurrentItem(5000);

        mVpPhoto.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                int pos=position%mDate.size();
                imgShowSelectAdapter.setSelectState(pos);
                imgShowSelectAdapter.notifyDataSetChanged();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
//        mVpPhoto.setPageTransformer(true, new RotateDownPageTransformer(new AlphaPageTransformer()));
    }
    /**
     * page adapter
     * @author Administrator
     */
    class BannerAdpter extends FragmentPagerAdapter {

        public BannerAdpter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return NUM;
        }
        @Override
        public Fragment getItem(int position) {
            int pos=position%mDate.size();
            return new ImgShowFragment(pos,mDate);
        }
    }
}
