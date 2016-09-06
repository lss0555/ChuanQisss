package Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

public class BaseFragment extends Fragment {

	public final static int RESULT_CLOSE_ACTIVITY = -9999;
	private static ArrayList<BaseFragment> activityList = new ArrayList<BaseFragment>();
	private boolean background = false;
	private Toast tip;
	private ProgressDialog loading;
	private CustomProgressDialog progressDialog = null;
	private int loadingCount = 0; // loading��������ʱ�ж���
	private Map<View, Runnable> touchOutsideListenerMap = new Hashtable<>();
	private Map<View, Runnable> touchListenerMap = new Hashtable<>();
	private boolean containVisiableStartActivity = false;
	private ConnectionChangeReceiver myReceiver;
	public boolean IsConnectNet=true;
	private SystemBarTintManager tintManager;
	/**
	 */
	public static boolean isAllActivityBackground() {
		for(BaseFragment a : activityList) {
			if( !a.isBackground() ) return false;
		}
		return true;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityList.add(this);
		setSystemTintBar(R.color.yellow_dark);
	}
	protected void setSystemTintBar(int ColorId){
		// create our manager instance after the content view is set
		tintManager = new SystemBarTintManager(getActivity());
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);
		// set a custom tint color for all system bars
		tintManager.setStatusBarTintColor(getResources().getColor(ColorId));
	}
	/**
	 */
	public boolean isBackground() {
		return background;
	}
	/**
	 */
	public void setViewOutsideClickListener(View v, Runnable l) {
		if( v != null ) {
			if( l != null ) touchOutsideListenerMap.put(v, l);
			else touchOutsideListenerMap.remove(v);
		}
	}
	/**
	 */
	public void setViewClickListener(View v, Runnable r) {
		if( v != null ) {
			if( r != null ) touchListenerMap.put(v, r);
			else  touchListenerMap.remove(v);
		}
	}
	public void onDestroy() {
		background = true;
		activityList.remove(this);
		super.onDestroy();
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
		Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
	}



	/**
	 * ����һ�����ݵ���ʾ�������ǰһ����ʾδ��ʧǰ���ã���ֱ��ȡ��ǰһ����ʾ
	 * @param msg
	 */
	public void showTip(String msg) {
		if( tip != null ) tip.cancel();
		tip = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
		tip.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout tipView = (LinearLayout) tip.getView();
		tipView.setBackgroundResource(R.drawable.toast_bg);
		ImageView tipIcon = new ImageView(getActivity());
		tipIcon.setImageResource(R.mipmap.success);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		lp.setMargins(10, 10, 10, 15);
		tipIcon.setLayoutParams(lp);
		tipView.addView(tipIcon, 0);
		tip.show();
	}

	/**
	 * ����һ��������ʾ��������ͼ��
	 * @param msg
	 */
	public void showErrorTip(String msg) {
		if( tip != null ) tip.cancel();
		tip = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
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
			progressDialog = CustomProgressDialog.createDialog(getActivity());
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
	/**
	 * ����һ�����ض������趨һ����ʱʱ�䣬���ʱ�䵽��û�б��رգ����Զ��رղ���ʾ������ʾ
	 * @param title ����
	 * @param msg ��Ϣ
	 * @param timeout ��ʱʱ�䣬��λ����
	 * @param timeoutCallback ��ʱ�ص�����,����Ϊnull
	 * @deprecated
	 */
	public void showLoading(String title, String msg, int timeout, final String timeoutMsg, final Runnable timeoutCallback) {
		if( loading == null ) loading = new ProgressDialog(getActivity());
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
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(msg)
				.setTitle("��ʾ")
				.setPositiveButton("ȷ��", null)
				.show();
	}

	/**
	 * ����һ���Ի�����ȷ����ȡ��������ť���õ��������Ӧ����
	 * @param msg
	 * @param ok
	 * @param cancle
	 */
	public void showDialog(String msg, OnClickListener ok, OnClickListener cancle) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(msg)
				.setTitle("��ʾ")
				.setPositiveButton("ȷ��", ok)
				.setNegativeButton("ȡ��", cancle)
				.show();
	}
	/**
	 * У���¼�˺�,�˺Ų���Ϊ�գ�6��11λ,���԰�����������ĸ�������ִ�Сд�������������ַ�
	 * @return �Ƿ�ͨ��У��
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
	 * У���¼����,6-16λ������Ϊ��
	 * @return �Ƿ�ͨ��У��
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
	 * У���ֻ�����
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
	 * У���¼��
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
	 * У����֤��
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
	 * У���Ƿ�Ϊ��
	 * @return
	 */
	public boolean validateEmpty(TextView view){
		if("".equals(view.getText().toString().trim())){
			showErrorTip("��������������");
			return false;
		}
		return true;
	}
//
//	/**
//	 * ���ظ�activity
//	 * @see #show
//	 */
//	public void hide() {
//		if( !(this instanceof MainActivity) )
//			setVisible(false);
//	}
//
//	/**
//	 * ��ʾ��activity
//	 * @see #hide()
//	 */
//	public void show() {
//		setVisible(true);
//	}
//
//	/**
//	 * ����һ��activity�����ص�ǰactivity
//	 * @see #nextTimeStartActivityContainVisiable
//	 */
//	public void startActivityForResult(Intent intent, int requestCode) {
//		startActivityForResult(intent, requestCode, null);
//	}
//
//	/**
//	 * ����һ��activity�����ص�ǰactivity
//	 * @see #nextTimeStartActivityContainVisiable
//	 */
//	public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
//		if( !containVisiableStartActivity ) {
//			hide();
//		}
//		containVisiableStartActivity = false;
//		super.startActivityForResult(intent, requestCode, options);
//	}
//
//	/**
//	 * ����һ��activity�������ص�ǰactivity
//	 */
//	public void startActivityForResultNotHide(Intent intent, int requestCode) {
//		super.startActivityForResult(intent, requestCode);
//	}
//	/**
//	 * ��һ��ʹ��startActivityForResult���ֽ�����ʾ
//	 */
//	protected void nextTimeStartActivityContainVisiable() {
//		this.containVisiableStartActivity = true;
//	}
//
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		show();
//	}
}
