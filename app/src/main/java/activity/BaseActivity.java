package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import Interfaces.ConnectionChangeReceiver;
import Utis.SystemBarTintManager;
import dialog.CustomProgressDialog;

public class BaseActivity extends FragmentActivity {
	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	public static final int NETTYPE_WIFI = 11;
	public static final int NETTYPE_CMWAP = 12;
	public static final int NETTYPE_CMNET = 13;
	public final static int RESULT_CLOSE_ACTIVITY = -9999;
	private static ArrayList<BaseActivity> activityList = new ArrayList<BaseActivity>();
	private boolean background = false;
	private Toast tip;
	private ProgressDialog loading;
	private Map<View, Runnable> touchOutsideListenerMap = new Hashtable<>();
	private CustomProgressDialog progressDialog = null;
	private int loadingCount = 0; // loading��������ʱ�ж���
	private Map<View, Runnable> touchListenerMap = new Hashtable<>();
	private boolean containVisiableStartActivity = false;
	private ConnectionChangeReceiver myReceiver;
	private boolean IsConnectNet=true;
	private SystemBarTintManager tintManager;

	/**
	 */
	public static boolean isAllActivityBackground() {
		for(BaseActivity a : activityList) {
			if( !a.isBackground() ) return false;
		}
		return true;
	}
	@Override
	protected void onStart() {
		super.onStart();
//		StatusBarCompat.setStatusBarColor(this,R.color.red, true);
		IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		myReceiver=new ConnectionChangeReceiver();
		myReceiver.SetNetStateListner(new ConnectionChangeReceiver.NetStateListner() {
			@Override
			public void NetState(boolean IsConnect) {
				if(IsConnect){
					IsConnectNet=true;
				}else {
					IsConnectNet=false;
				}
//				Toast("连接网络状态:"+IsConnect);
			}
		});
		registerReceiver(myReceiver, filter);
	}
	public  void IsNewConnect(boolean IsConnectNet){
		Toast("连接网络网站"+IsConnectNet);
	}
	public void onDestroy() {
		background = true;
		activityList.remove(this);
		unregisterReceiver(myReceiver);
		super.onDestroy();
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityList.add(this);
//		StatusBarCompat.setStatusBarColor(this,R.color.red, true);
		setSystemTintBar(R.color.yellow_dark);
	}
	protected void setSystemTintBar(int ColorId){
		// create our manager instance after the content view is set
		tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);
		// set a custom tint color for all system bars
		tintManager.setStatusBarTintColor(getResources().getColor(ColorId));
	}
	public void back(View view) {
		finish();
	}
	/**
	 */
	public boolean isBackground() {
		return background;
	}

	public void bringToFront() {
		Intent intent = new Intent(this, this.getClass());
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		background = false;
	}
	/**
	 */
	public void setViewOutsideClickListener(View v, Runnable l) {
		if( v != null ) {
			if( l != null ) touchOutsideListenerMap.put(v, l);
			else touchOutsideListenerMap.remove(v);
		}
	}
	public void setViewClickListener(View v, Runnable r) {
		if( v != null ) {
			if( r != null ) touchListenerMap.put(v, r);
			else  touchListenerMap.remove(v);
		}
	}
	public void NetTip(){
		if(!isNetworkConnected(getApplicationContext())){
			Toast("当前无网络，请检查网络");
		}
	}
	/**
	 * 判断有无网络
	 * @param context
	 * @return
     */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public void onResume() {
		background = false;
//		AndroidSystem.getInstance().clearNotification();
		super.onResume();
	}
	public void onStop() {
		background = true;
		super.onStop();
	}
	public void Toast(String msg){
		Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	}

	public void setVisible(boolean visible) {
		background = !visible;
		super.setVisible(visible);
	}

	public void finish() {
		background = true;
		activityList.remove(this);
		super.finish();
	}

	/**
	 * ����һ�����ݵ���ʾ�������ǰһ����ʾδ��ʧǰ���ã���ֱ��ȡ��ǰһ����ʾ
	 * @param msg
	 */
	public void showTip(String msg) {
		if( tip != null ) tip.cancel();
		tip = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		tip.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout tipView = (LinearLayout) tip.getView();
		tipView.setBackgroundResource(R.drawable.toast_bg);
		ImageView tipIcon = new ImageView(this);
		tipIcon.setImageResource(R.mipmap.success);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		lp.setMargins(10, 10, 10, 15);
		tipIcon.setLayoutParams(lp);
		tipView.addView(tipIcon, 0);
		tip.show();
	}

	/**
	 * @param msg
	 */
	public void showErrorTip(String msg) {
		if( tip != null ) tip.cancel();
		tip = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		tip.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout tipView = (LinearLayout) tip.getView();
		tipView.setBackgroundResource(R.drawable.toast_bg);
		ImageView tipIcon = new ImageView(tipView.getContext());
		tipIcon.setImageResource(R.mipmap.fail);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		lp.setMargins(10, 10, 10, 15);
		tipIcon.setLayoutParams(lp);
		tipView.addView(tipIcon, 0);
		tip.show();
	}

	public void startProgressDialog(String msg){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
			if(!"".equals(msg)) {
				progressDialog.setMessage(msg);
			}
		}

		progressDialog.show();
	}
	public void stopProgressDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	/*eprecated
	 */
	public void showLoading(String title, String msg, int timeout, final String timeoutMsg, final Runnable timeoutCallback) {
		if( loading == null ) loading = new ProgressDialog(this);
		final Handler handler = new Handler();
		final int count = ++loadingCount;
		loading.setTitle(title);
		loading.setMessage(msg);
		if( timeout > 0 ) {
			handler.postDelayed(new Runnable() {
				public void run() {
					if (loading != null && loading.isShowing() && count >= loadingCount) {
						loading.cancel();
						if (timeoutMsg != null && !timeoutMsg.equals("")) showTip(timeoutMsg);
						if (timeoutCallback != null) timeoutCallback.run();
					}
				}
			}, timeout);
		}
		loading.show();
	}

	/**
	 * ������Ϣ��ʾ����һ��ȷ����ť��ֻ���û����ȷ�����ܹر�
	 * @param msg
	 */
	public void showMessage(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
				.setTitle("��ʾ")
				.setPositiveButton("ȷ��", null)
				.show();
	}

	/**
	 * @param msg
	 * @param ok
	 * @param cancle
	 */
	public void showDialog(String msg, OnClickListener ok, OnClickListener cancle) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
				.setTitle("��ʾ")
				.setPositiveButton("ȷ��", ok)
				.setNegativeButton("ȡ��", cancle)
				.show();
	}

	/**
	 */
	public boolean validateLoginAccount(TextView view) {
		int minLen = 6;
		int maxLen = 11;
		String loginAccount = view.getText().toString().trim();
		if( loginAccount == null || loginAccount.equalsIgnoreCase("") ) {
			showErrorTip("�˺Ų���Ϊ��");
			return false;
		}
		if( loginAccount.length() < minLen ) {
			showErrorTip("�˺ű������" + minLen + "λ");
			return false;
		}
		if( loginAccount.length() > maxLen ) {
			showErrorTip("�˺Ų�������" + maxLen + "λ");
			return false;
		}
		if( !loginAccount.matches("[a-z|A-Z|0-9]+") ) {
			showErrorTip("�˺�ֻ�������ֻ���ĸ");
			return false;
		}
		return true;
	}

	/**
	 */
	public boolean validateLoginPassword(TextView view) {
		int minLen = 6;
		int maxLen = 16;
		String password = view.getText().toString().trim();
		if( password == null || password.equals("") ) {
			showErrorTip("���벻��Ϊ��");
			return false;
		}
		if( password.length() < minLen ) {
			showErrorTip("����������" + minLen + "λ");
			return false;
		}
		if( password.length() > maxLen ) {
			showErrorTip("���벻������" + maxLen + "λ");
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean validateTelNumber(TextView view){
		int len = 11;
		if("".equals(view.getText().toString().trim())){
			showErrorTip("�������ֻ���");
			return false;
		}else if(view.getText().toString().trim().length() != len){
			showErrorTip("�ֻ��ű���Ϊ11λ");
			return false;
		} else if(!isTelNumber(view.getText().toString().trim())){
			showErrorTip("�ֻ��Ÿ�ʽ����ȷ");
			return false;
		}
		return true;
	}
	/**
	 * @return
	 */
	public boolean validateLoginNumber(TextView view){
		int minLen = 6;
		if("".equals(view.getText().toString().trim())){
			showErrorTip("�������¼��");
			return false;
		}else if(view.getText().toString().trim().length() < minLen){
			showErrorTip("��¼��Ϊ6-11λ");
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean validateCode(TextView view){
		if("".equals(view.getText().toString().trim())){
			showErrorTip("��������֤��");
			return false;
		}
		return true;
	}
	public boolean isTelNumber(String tel){
		String telRegex = "[1][3758]\\d{9}";
		if(!tel.matches(telRegex)){
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean validateEmpty(TextView view){
		if("".equals(view.getText().toString().trim())){
			showErrorTip("��������������");
			return false;
		}
		return true;
	}

}
