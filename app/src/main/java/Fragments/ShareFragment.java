package Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Mob.Share.OnekeyShare;
import Utis.GsonUtils;
import Utis.MD5Utis;
import Utis.OkHttpUtil;
import Utis.SharePre;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import model.Result;

public class ShareFragment extends BaseFragment {
    private String platformType;
    public ShareFragment() {

    }
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    getShareMoney();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_share,null);
        initview(layout);
        return layout;
    }
    private void initview(View layout) {
           layout.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
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
                platformType=platform.getName();
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
    private void getShareMoney() {
        if(SharePre.getUserId(getActivity())!=null ||!SharePre.getUserId(getActivity()).equals("")){
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("userid",""+ SharePre.getUserId(getActivity()));
            map.put("rwstyle",""+platformType);
            map.put("sign",""+ MD5Utis.MD5_Encode(platformType+"传祺chuanqi"));
            OkHttpUtil.getInstance().Post(map, constance.URL.SHARE_GET, new OkHttpUtil.FinishListener() {
                @Override
                public void Successfully(boolean IsSuccess, String data, String Msg) {
//                Toast(data.toString()+platformType);
                    Log.i("分享的平台",""+platformType);
                    Log.i("收到的数据",""+data.toString());
                    if(IsSuccess){
                        Log.i("分享的返回结果",""+data.toString());
                        Result result = GsonUtils.parseJSON(data, Result.class);
                        if(result.getRun().equals("1")){
                            Toast("恭喜您获得0.5元");
                            Intent intent = new Intent();
                            intent.putExtra("update",true);
                            intent.setAction("update");   //
                            getActivity().sendBroadcast(intent);
                        }else if(result.getRun().equals("2")){
                            Toast("抱歉，非法操作");
                        }
                    }else {
                        Toast(data.toString());
                    }
                }
            });
        }
    }
}
