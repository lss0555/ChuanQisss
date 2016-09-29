package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.chuanqi.yz.R;

import java.util.HashMap;

import Constance.constance;
import Utis.SharePre;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import model.Result;

public class WelcomeActivity extends BaseActivity {
    private ListView mList;
    private boolean IsRegist=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        IsRegistUser();
        initview();
    }
    private void initview() {
        if(!SharePre.getUserId(WelcomeActivity.this).equals("") ){
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
                //这里的数字为延时时长
            }, 1500);
        }else {
            IsRegistUser();
        }
    }

    public void IsRegistUser(){
        HashMap<String,String> map=new HashMap<>();
        map.put("udid",""+ Utis.getIMEI(getApplicationContext()));
       OkHttpUtil.getInstance().Post(map, constance.URL.IS_USER, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                if(IsSuccess){
                    Result result = GsonUtils.parseJSON(data, Result.class);
                    if(result.getRun().equals("0")){
                        //还没注册
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Intent intent=new Intent(WelcomeActivity.this,YaoQingActivity.class);
                                startActivity(intent);
                                WelcomeActivity.this.finish();
                            }
                            //这里的数字为延时时长
                        }, 1500);
                    }else if(result.getRun().equals("1")){
                        //已经注册
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                                startActivity(intent);
                                WelcomeActivity.this.finish();
                            }
                            //这里的数字为延时时长
                        }, 1500);
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
}
