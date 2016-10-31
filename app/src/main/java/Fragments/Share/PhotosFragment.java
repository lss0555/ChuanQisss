package Fragments.Share;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Constance.constance;
import Dialogs.SelectPicPop;
import Fragments.BaseFragment;
import Fragments.ImgShowFragment;
import Mob.Share.OnekeyShare;
import Utis.GsonUtils;
import Utis.Utis;
import Utis.MakeQRCodeUtil;
import Utis.UILUtils;
import Utis.SharePre;
import Utis.OkHttpUtil;
import ViewPageAlpfer.AlphaPageTransformer;
import ViewPageAlpfer.RotateDownPageTransformer;
import ViewPageAlpfer.ZoomOutPageTransformer;
import Views.HorizontalListView;
import adapter.ImgShowSelectAdapter;
import app.AndroidSystem;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import model.Photos;
import model.UserInfo;

/**
 */
public class PhotosFragment extends BaseFragment {
    private String savePath = Utis.getAppFolder();
    private ImageView mImgIcons;
    private TextView mTvNickName;
    private RelativeLayout mRtlShare;
    private ImageView mImgQrCode;
    private Bitmap makeQRImage;
    public PhotosFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_photos,null);
        initview(layout);
        initdate();
        initevent();
        return layout;
    }
    private void initdate() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("udid", Utis.getIMEI(getActivity()));
        OkHttpUtil.getInstance().Post(map, constance.URL.USER_INFO, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                    showTip("个人资料:"+data.toString());
                stopProgressDialog();
                Log.i("个人资料",""+data.toString());
                if(IsSuccess){
                    UserInfo mUserInfo= GsonUtils.parseJSON(data, UserInfo.class);
                    SharePre.saveUserId(getActivity(),mUserInfo.getId());
                    UILUtils.displayImage(mUserInfo.getHeadportrait(),mImgIcons);
                    mTvNickName.setText(""+mUserInfo.getUname());
                } else {
                    Toast(data.toString());
                }
            }
        });

        try {
//            makeQRImage = MakeQRCodeUtil.makeQRImage("http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getActivity()), 300, 300);
            makeQRImage = MakeQRCodeUtil.makeQRImage(BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.icons),"http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getActivity()), 500, 500);
            mImgQrCode.setImageBitmap(makeQRImage);
        } catch (WriterException e) {
            e.printStackTrace();
        }
//			Bitmap makeQRImage=MakeQRCodeUtil.makeQRImage(drawingCache,"http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid=100087", 200, 200);
    }
    /**
     * 初始化添加数据
     */
    private void initevent() {
        mRtlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Share();
                    }
                }).start();
            }
        });

        mImgQrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                SelectPicPop selectPicPop = new SelectPicPop(getActivity());
                selectPicPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                selectPicPop.setOnSelectPicListner(new SelectPicPop.OnSelecListner() {
                    @Override
                    public void onSelect(String Type) {
                        switch (Type) {
                            case "Save":
//								Bitmap bitmap = ((BitmapDrawable) mImgShow.getDrawable()).getBitmap();
//                                mImgQrCode.setDrawingCacheEnabled(true);
//                                Bitmap drawingCache = mImgQrCode.getDrawingCache();
                                Bitmap thisShot = AndroidSystem.getThisShot(getActivity());
                                saveImgs(thisShot);
//                                mImgQrCode.setDrawingCacheEnabled(false);
                                break;
                        }
                    }
                });
                return false;
            }
        });
    }
    private void initview(View layout) {
        mImgIcons = (ImageView) layout.findViewById(R.id.img_icons);
        mTvNickName = (TextView) layout.findViewById(R.id.tv_nickname);
        mRtlShare = (RelativeLayout) layout.findViewById(R.id.rtl_share);
        mImgQrCode = (ImageView) layout.findViewById(R.id.img_qrcode);
    }
    public  void  Share(){
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();//关闭sso授权
        oks.setTitle("易钻ATM");  // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getActivity()));
        oks.setText("易钻ATM,快来加入一起来赚吧！");  // text是分享文本，所有平台都需要这个字段
        oks.setImageUrl("http://i.qingyiyou.cn/yz/Interface/banner/icons.png");
        oks.setUrl("http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getActivity())); // url仅在微信（包括好友和朋友圈）中使用
        oks.setComment("易钻ATM有你才完美");// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSiteUrl("http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getActivity()));   // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast("分享成功");
            }
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("分享状态","onError");
                Toast("分享错误");
            }
            @Override
            public void onCancel(Platform platform, int i) {
                Toast("分享取消");
            }
        });
        oks.show(getActivity()); // 启动分享GUI
    }

    /**
     * 保存图片
     *
     * @param bigImg
     */
    private void saveImgs(Bitmap bigImg) {
        if (Utis.getSDPath() == null) {
            Toast.makeText(getActivity(), "没有内存卡", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdir();
        }
        File imageFile = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            imageFile.getParentFile().mkdirs();
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bigImg.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
//			Toast.makeText(getActivity(), "图片已保存到本地文件夹" + savePath, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), imageFile.getPath(), "title", "description");
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri uri = Uri.fromFile(file);
//            intent.setData(uri);
//            getActivity().sendBroadcast(intent);
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageFile.getPath())));

        } catch (FileNotFoundException e) {
            Toast.makeText(getActivity(), "图片保存失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "图片保存失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
