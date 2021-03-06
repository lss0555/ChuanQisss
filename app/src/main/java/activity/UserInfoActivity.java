package activity;
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
//    private UserInfo mUserInfo;
    private CircleImageView mImgIcons;
    private TextView mTvNickName;
    private TextView mTvPhone;
    private TextView mTvBirthday;
    private TextView mTvBindWxState;
    private TextView mTvYqm;
    private TextView mTvAddress;
    private TextView mTvBindAlipayState;

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
        map.put("userid", SharePre.getUserId(UserInfoActivity.this));
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
        HashMap<String,String> map1=new HashMap<String, String>();
        map1.put("userid", SharePre.getUserId(getApplicationContext()));
        map1.put("accountstype", "2");
        OkHttpUtil.getInstance().Post(map1, constance.URL.IS_BIND_WX_ALIPAY_ACCOUNT, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
                stopProgressDialog();
                if(IsSuccess){
                    IsBindAccount bindAccount = GsonUtils.parseJSON(data, IsBindAccount.class);
//                showTip(data.toString());
                    if(!bindAccount.getAccount().equals("")){
                        //有绑定
                        mTvBindAlipayState.setText("已绑定");
                    }else {
                        //未绑定
                        mTvBindAlipayState.setText("未绑定");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    private void iniview() {
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mImgIcons = (CircleImageView) findViewById(R.id.img_icons);
        mTvNickName = (TextView) findViewById(R.id.tv_username);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvBirthday = (TextView) findViewById(R.id.tv_birthday);
        mTvBindWxState = (TextView) findViewById(R.id.tv_bind_wx_state);
        mTvYqm = (TextView) findViewById(R.id.tv_yqm);
        mTvBindAlipayState = (TextView) findViewById(R.id.tv_bind_alipay_state);
    }
    private void getDate() {
//        mUserInfo= (UserInfo) getIntent().getSerializableExtra("info");
        if(getIntent().getStringExtra("Head")==null || getIntent().getStringExtra("Head").equals("")){
        }else {
            UILUtils.displayImage(getIntent().getStringExtra("Head"),mImgIcons);
        }
        if(!getIntent().getStringExtra("NickName").equals("")){
            mTvNickName.setText(getIntent().getStringExtra("NickName"));
        }
        if(getIntent().getStringExtra("Tel")==null){
            mTvPhone.setText("暂未绑定");
        }else {
            mTvPhone.setText(""+getIntent().getStringExtra("Tel"));
        }
        mTvYqm.setText(""+SharePre.getUserId(UserInfoActivity.this));
        if(getIntent().getStringExtra("Address").equals("")){
            mTvAddress.setText("暂无");
        }else {
            mTvAddress.setText(""+getIntent().getStringExtra("Address"));
        }
    }
}
