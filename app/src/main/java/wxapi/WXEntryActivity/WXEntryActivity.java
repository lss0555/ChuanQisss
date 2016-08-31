package wxapi.WXEntryActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.youmi.android.offers.OffersManager;


/**
 * 本类的作用
 * <ol>
 * <li>当微信向应用发送请求的时候，会回调这里</li>
 * <li>应用发送请求到微信后，有结果时会回调这里</li>
 * </ol>
 * <p>
 * 使用有米分享墙的开发者需要在本类（"应用包名.wxapi.WXEntryActivity"）中，重写{@link #onCreate(Bundle)} 和 {@link #onNewIntent(Intent)}
 * 方法，并在super语句之后调用有米的处理方法
 * <p/>
 * <pre>
 * OffersManager.getInstance(this).handleIntent(getIntent());
 * </pre>
 * <p/>
 * </p>
 *
 * @author youmi
 * @since 2015-05-22 10:46
 */
public class WXEntryActivity extends AppCompatActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 添加有米的处理方法
		OffersManager.getInstance(this).handleIntent(getIntent());

		// 分享有结果之后会打开这个activity，但是因为这个activity在这个demo中没有界面，因此将会是一片空白的，开发者请酌情判断是否需要finish掉
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// 添加有米的处理方法
		OffersManager.getInstance(this).handleIntent(getIntent());

		// 分享有结果之后会打开这个activity，但是因为这个activity在这个demo中没有界面，因此将会是一片空白的，开发者请酌情判断是否需要finish掉
		finish();

	}


	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打开任何页面
	 */
//	public void onGetMessageFromWXReq(WXMediaMessage msg) {
//		if (msg != null) {
//			Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
//			startActivity(iLaunchMyself);
//		}
//	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
	 * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
	 * 回调。
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
//	public void onShowMessageFromWXReq(WXMediaMessage msg) {
//		if (msg != null && msg.mediaObject != null
//				&& (msg.mediaObject instanceof WXAppExtendObject)) {
//			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
//			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
//		}
//	}
}
