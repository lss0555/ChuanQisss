package activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import Utis.UILUtils;
import Views.CircleImageView;
import model.IsBindAccount;
import model.UserInfo;

public class UserInfoActivity extends BaseActivity {
    private UserInfo mUserInfo;
    private CircleImageView mImgIcons;
    private TextView mTvNickName;
    private TextView mTvPhone;
    private TextView mTvBirthday;
    private TextView mTvBindWxState;
    private TextView mTvYqm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        iniview();
        initState();
        getDate();
    }
    /**
     * 初始化状态
     */
    private void initState() {
        //初始化微信绑定状态
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("userid", SharePre.getUserId(getApplicationContext()));
        map.put("accountstype", "1");
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_BIND_WX_ALIPAY_ACCOUNT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                IsBindAccount bindAccount = GsonUtils.parseJSON(data, IsBindAccount.class);
                if(!bindAccount.getAccount().equals("")){
                    mTvBindWxState.setText("已绑定");
                }else {
                    mTvBindWxState.setText("未绑定");
                }
            }
        });
    }

    private void iniview() {
        mImgIcons = (CircleImageView) findViewById(R.id.img_icons);
        mTvNickName = (TextView) findViewById(R.id.tv_username);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvBirthday = (TextView) findViewById(R.id.tv_birthday);
        mTvBindWxState = (TextView) findViewById(R.id.tv_bind_wx_state);
        mTvYqm = (TextView) findViewById(R.id.tv_yqm);
    }
    private void getDate() {
        mUserInfo= (UserInfo) getIntent().getSerializableExtra("info");
        UILUtils.displayImage(mUserInfo.getHeadportrait(),mImgIcons);
        mTvNickName.setText(mUserInfo.getUname());
        mTvPhone.setText(mUserInfo.getTel()+"");
        mTvBirthday.setText(""+mUserInfo.getBirthday().substring(0,10));
        mTvYqm.setText(""+SharePre.getUserId(getApplicationContext()));
    }
}
