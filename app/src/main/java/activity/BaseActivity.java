package activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.chuanqi.yz.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import Interfaces.MyReceiver;
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
	private static final int READ_PHONE_STATE = 0;
	private static ArrayList<BaseActivity> activityList = new ArrayList<BaseActivity>();
	private boolean background = false;
	private Toast tip;
	private ProgressDialog loading;
	private Map<View, Runnable> touchOutsideListenerMap = new Hashtable<>();
	private CustomProgressDialog progressDialog = null;
	private int loadingCount = 0; // loading��������ʱ�ж���
	private Map<View, Runnable> touchListenerMap = new Hashtable<>();
	private boolean containVisiableStartActivity = false;
	private MyReceiver myReceiver;
	private boolean IsConnectNet=true;
	private FrameLayout group;
	private View statusBarView;
	private View content;
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
	}
	public  void IsNewConnect(boolean IsConnectNet){
		Toast("连接网络网站"+IsConnectNet);
	}
	public void onDestroy() {
		background = true;
		activityList.remove(this);
		super.onDestroy();
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		CheckPession();
		activityList.add(this);
		group = new FrameLayout(getApplicationContext());
		// 创建一个statusBarView 占位，填充状态栏的区域，以后设置状态栏背景效果其实是设置这个view的背景。
		group.addView(statusBarView = createStatusBar());
		super.setContentView(group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		// 设置activity的window布局从状态栏开始
		setTranslucentStatus(true);
		setStatusBarColorBg(R.color.red);
	}

	/**
	 * 检查是否有系统权限
	 */
	public   boolean CheckPession() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
				!= PackageManager.PERMISSION_GRANTED) {
			// Camera permission has not been granted.
			requestCameraPermission();
			return  false;
		}else {
			return true;
		}
	}
	/**
	 * Requests the Camera permission.
	 * If the permission has been denied previously, a SnackBar will prompt the user to grant the
	 * permission, otherwise it is requested directly.
	 */
	private   void requestCameraPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				Manifest.permission.READ_PHONE_STATE)) {
		} else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
					READ_PHONE_STATE);
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode ==READ_PHONE_STATE) {
			// Received permission result for camera permission.est.");
			// Check if the only required permission has been granted
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Camera permission has been granted, preview can be displayed
//				Snackbar.make(mLayout, R.string.permision_available_camera,
//						Snackbar.LENGTH_SHORT).show();
			} else {
//				Snackbar.make(mLayout,"",
//						Snackbar.LENGTH_SHORT).show();
			}
		}
	}
	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		content = view;
		if (params == null) {
			group.addView(view, 0);
		} else {
			group.addView(view, 0, params);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (statusBarView.getVisibility() == View.VISIBLE) {
				view.setFitsSystemWindows(true);
			} else {
				view.setFitsSystemWindows(false);
			}
		}
	}
	/**
	 * 设置状态栏的颜色
	 *
	 * @param color
	 */
	public void setStatusBarColorBg(int color) {
		statusBarView.setBackgroundColor(color);
	}
	public void setTranslucentStatus(boolean isTranslucentStatus) {
		if (isTranslucentStatus) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
				localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
			}
			statusBarView.setVisibility(View.VISIBLE);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
				localLayoutParams.flags = ((~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) & localLayoutParams.flags);
			}
			statusBarView.setVisibility(View.GONE);
		}
	}
	/**
	 * 创建状态栏填充的 statusBarView
	 *
	 * @return
	 */
	private View createStatusBar() {
		int height = getStatusBarHeight();
		View statusBarView = new View(this);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
		statusBarView.setBackgroundResource(R.color.red);
		statusBarView.setLayoutParams(lp);
		return statusBarView;
	}
	/**
	 * 获取状态的高度
	 * @return
	 */
	public int getStatusBarHeight() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			int result = 0;
			int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = getResources().getDimensionPixelSize(resourceId);
			}
			return result;
		}
		return 0;
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
