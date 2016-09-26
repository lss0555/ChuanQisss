package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Utis.SharePre;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import model.Result;
import model.UserInfo;

public class YaoQingActivity extends BaseActivity {
    private UserInfo mUserInfo;
    private ListView mList;
    private RelativeLayout mRtlNo;
    private RelativeLayout mRtlYes;
    private EditText mEtYqm;
    private boolean IsRegist;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaoqing);
        initview();
//        initdate();
        initevent();
    }
    private void initdate() {
        if(SharePre.getUserId(getApplicationContext())!=null){
            Intent intent=new Intent(YaoQingActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            IsRegistUser();
        }
    }
    public void IsRegistUser(){
        HashMap<String,String> map=new HashMap<>();
        map.put("udid",""+Utis.getIMEI(getApplicationContext()));
        OkHttpUtil.getInstance().Post(map, constance.URL.IS_USER, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("1")){
                         //已经注册
                        IsRegist=true;
                        Intent intent=new Intent(YaoQingActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(result.getRun().equals("0")){
                        IsRegist=false;
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }

    private void initevent() {
        mRtlYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEtYqm.getText().toString().equals("")){
                     Toast("请填写邀请码！");
                }else {
                    YqTudi();
                }
            }
        });
        mRtlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(YaoQingActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("填写邀请码将获得0.3元奖金，确认不填写?")//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                Intent intent=new Intent(YaoQingActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });
    }
    /**
     * 邀请徒弟
     */
    private void YqTudi() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("MasterId",""+mEtYqm.getText().toString().trim());
        OkHttpUtil.getInstance().Post(map, constance.URL.USERID_ISEXIST, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("1")){
                        //userid存在
                        HashMap<String,String> map=new HashMap<>();
                        map.put("udid", Utis.getIMEI(getApplicationContext()));
                        OkHttpUtil.getInstance().Post(map, constance.URL.USER_INFO, new OkHttpUtil.FinishListener() {
                            @Override
                            public void Successfully(boolean IsSuccess, String data, String Msg) {
                                Log.i("个人资料",""+data.toString());
                                if(IsSuccess){
                                    mUserInfo= GsonUtils.parseJSON(data, UserInfo.class);
                                    SharePre.saveUserId(getApplicationContext(),mUserInfo.getId());//保存userid
                                    HashMap<String,String> map=new HashMap<>();
                                    map.put("MasterId",""+mEtYqm.getText().toString().trim());
                                    map.put("ApprenticeId",""+SharePre.getUserId(getApplicationContext()));
                                    OkHttpUtil.getInstance().Post(map, constance.URL.YQM_IS_ZQ, new OkHttpUtil.FinishListener() {
                                        @Override
                                        public void Successfully(boolean IsSuccess, String data, String Msg) {
                                            stopProgressDialog();
                                            Log.i("判断邀请码是否正确",""+data.toString());
//                                            showTip(data.toString());
                                            if(IsSuccess){
                                                Result result = GsonUtils.parseJSON(data, Result.class);
                                                if(result.getRun().equals("1")){
                                                    Toast("恭新您，获得0.3元");
                                                    Intent intent=new Intent(YaoQingActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else {
                                                    Toast("建立失败");
                                                }
                                            }
                                        }
                                    });

                                } else {
                                    Toast(data.toString());
                                }
                            }
                        });

                    }else if(result.getRun().equals("0")){
                        stopProgressDialog();
                        Toast("抱歉，您输入的邀请码不存在");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    private void initview() {
        mEtYqm = (EditText) findViewById(R.id.et_yqm);
        mRtlYes = (RelativeLayout) findViewById(R.id.rtl_yes);
        mRtlNo = (RelativeLayout) findViewById(R.id.rtl_no);
    }
}
