package Fragments.Share;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chuanqi.yz.R;
import java.util.HashMap;
import Constance.constance;
import Fragments.BaseFragment;
import Mob.Share.OnekeyShare;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.Utis;
import Utis.UILUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import model.ShaiDan;
import model.UserInfo;
/**
 * A simple {@link Fragment} subclass.
 */
public class ShowOrderShareFragment extends BaseFragment {
    private TextView mTvAllTaskNum;
    private TextView mTvAllTudiNum;
    private TextView mTvTudiGet;
    private TextView mTvAllGet;
    private ImageView mImgIcon;
    private TextView mTvNickName;
    private RelativeLayout mRtlShare;

    public ShowOrderShareFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_show_order_share,null);
        initview(layout);
        initUserInfo();
        getDate();
        initevent();
        return layout;
    }

    /**
     * 分享事件
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
     * 用户资料
     */
    private void initUserInfo() {
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
                   UserInfo  mUserInfo= GsonUtils.parseJSON(data, UserInfo.class);
                    SharePre.saveUserId(getActivity(),mUserInfo.getId());
                    UILUtils.displayImage(mUserInfo.getHeadportrait(),mImgIcon);
                    mTvNickName.setText(""+mUserInfo.getUname());
                } else {
                    Toast(data.toString());
                }
            }
        });
    }

    private void getDate() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+ SharePre.getUserId(getActivity()));
        OkHttpUtil.getInstance().Post(map, constance.URL.SHAI_DAN, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                Log.i("晒单",""+data.toString());
                stopProgressDialog();
                if(IsSuccess){
                    ShaiDan shaiDan = GsonUtils.parseJSON(data, ShaiDan.class);
                    if(shaiDan.getUserappcount()==null){
                        mTvAllTaskNum.setText("0个");
                    }else {
                        mTvAllTaskNum.setText(""+shaiDan.getUserappcount()+"个");
                    }
                    if(shaiDan.getTudicount()==null){
                        mTvAllTudiNum.setText("0个");
                    }else {
                        mTvAllTudiNum.setText(""+shaiDan.getTudicount()+"个");
                    }
                    if(shaiDan.getTudisy()==null){
                        mTvTudiGet.setText("0元");
                    }else {
                        mTvTudiGet.setText(""+shaiDan.getTudisy()+"元");
                    }
                    if(shaiDan.getAllsy()==null){
                        mTvAllGet.setText("0元");
                    }else {
                        mTvAllGet.setText(""+shaiDan.getAllsy()+"元");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    private void initview(View layout) {
        mTvAllTaskNum = (TextView) layout.findViewById(R.id.tv_all_task_num);
        mTvAllTudiNum = (TextView) layout.findViewById(R.id.tv_all_tudi_num);
        mTvTudiGet = (TextView) layout.findViewById(R.id.tv_tudi_get);
        mTvAllGet = (TextView) layout.findViewById(R.id.tv_all_get);
        mImgIcon = (ImageView) layout.findViewById(R.id.img_icons);
        mTvNickName = (TextView) layout.findViewById(R.id.tv_nickname);
        mRtlShare = (RelativeLayout) layout.findViewById(R.id.rtl_share);
    }
}
