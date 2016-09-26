package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.chuanqi.yz.R;

public class WelcomeActivity extends BaseActivity {
    private ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initview();
    }

    private void initview() {
        new Handler().postDelayed(new Runnable()
        {
            //封装的run()方法，用在
            @Override
            public void run()
            {
                //页面跳转
                //*********修改1**************修改1*****************修改1******************
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);//修改:(注："MainActivityWelcome"当前类名，"MainActivity"当前需要跳转的Activity第二个页面的类名)
                //保存跳转信息
                startActivity(intent);
                //进入第二个界面前销毁当前的活动，"finish()"销毁活动
                WelcomeActivity.this.finish();
            }
            //这里的数字为延时时长
        }, 1000);
    }
}
