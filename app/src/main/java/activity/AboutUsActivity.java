package activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanqi.yz.R;
import com.google.zxing.WriterException;

import Utis.Utis;
import Utis.MakeQRCodeUtil;


public class AboutUsActivity extends BaseActivity {

    private TextView mTvBanben;
    private Bitmap makeQRImage;
    private ImageView mImgQrCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initview();
    }
    private void initview() {
        mImgQrCode = (ImageView) findViewById(R.id.img_qrcode);
        mTvBanben = (TextView) findViewById(R.id.tv_banben);
        mTvBanben.setText("版本号:"+Utis.getVersion(AboutUsActivity.this));
        try {
            makeQRImage = MakeQRCodeUtil.makeQRImage(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.icons),"http://www.yzatm.com/", 500, 500);
//            makeQRImage = MakeQRCodeUtil.makeQRImage(((BitmapDrawable) mImgIcons.getDrawable()).getBitmap(),"http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getActivity()), 300, 300);
            mImgQrCode.setImageBitmap(makeQRImage);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
