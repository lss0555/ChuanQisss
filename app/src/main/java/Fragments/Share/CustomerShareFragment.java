package Fragments.Share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Fragments.BaseFragment;
import Mob.Share.OnekeyShare;
import activity.CustomerShareActivity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 */
public class CustomerShareFragment extends BaseFragment {

    private RelativeLayout rtlCustomer;
    private RelativeLayout mRtlYq;
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
//                    getShareMoney();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public CustomerShareFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_customer_share,null);
        initview(layout);
        initevent();
        return layout;
    }

    private void initevent() {
        rtlCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CustomerShareActivity.class);
                startActivity(intent);
            }
        });
        mRtlYq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Share();
                    }
                }).start();
            }
        });
    }

    private void initview(View layout) {
        rtlCustomer = (RelativeLayout) layout.findViewById(R.id.rtl_customer);
        mRtlYq = (RelativeLayout) layout.findViewById(R.id.rtl_yq);

    }
    public  void  Share(){
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();//关闭sso授权
        oks.setTitle("易赚ATM");  // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://weibo.com/ttarticle/p/show?id=2309404017475523111097");
        oks.setText("易赚ATM,快来加入一起来赚吧！");  // text是分享文本，所有平台都需要这个字段
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl("http://bmob-cdn-4915.b0.upaiyun.com/2016/09/08/e52ab1af40b10546807df1d125c2bc70.jpg");
        oks.setUrl("http://weibo.com/ttarticle/p/show?id=2309404017475523111097"); // url仅在微信（包括好友和朋友圈）中使用
        oks.setComment("易赚有你才完美");// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSiteUrl("http://weibo.com/ttarticle/p/show?id=2309404017475523111097");   // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast("分享成功");
                Message message=new Message();
                message.what=1;
                mHandler.sendMessage(message);
            }
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("分享状态","onError");
                Message message=new Message();
                message.what=2;
                mHandler.sendMessage(message);
            }
            @Override
            public void onCancel(Platform platform, int i) {
                Toast("分享取消");
                Message message=new Message();
                message.what=3;
                mHandler.sendMessage(message);
            }
        });
        oks.show(getActivity()); // 启动分享GUI
    }
}
