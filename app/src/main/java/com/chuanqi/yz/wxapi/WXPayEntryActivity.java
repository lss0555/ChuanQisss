package com.chuanqi.yz.wxapi;
import com.chuanqi.yz.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import Constance.constance;
import Utis.OkHttpUtil;
import Utis.SharePre;
import activity.BaseActivity;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.wxpay_result);
        
    	api = WXAPIFactory.createWXAPI(this, "wx541c42bc54fac5cf");
        api.handleIntent(getIntent(), this);
		finish();
    }
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}
	@Override
	public void onResp(BaseResp resp) {
		Log.i("微信支付回调",""+resp.toString());
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (String.valueOf(resp.errCode)){
				case "0":
					Toast("支付成功");;
					break;
				case "-1":
					Toast("支付错误,请检查支付参数");
					break;
				case "-2":
					Toast("您已取消支付");
					break;
			}
		}
	}
}