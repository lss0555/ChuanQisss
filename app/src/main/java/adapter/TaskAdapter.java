package adapter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;

import Constance.constance;
import Utis.UILUtils;
import Utis.Utis;
import Utis.GsonUtils;
import Utis.SharePre;
import Utis.OkHttpUtil;
import activity.FaskTaskDetailActivity;
import model.FaskTask.faskTask;
import model.Result;

public class TaskAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<faskTask> mDate;
	private ViewHold viewHold;
	public TaskAdapter(Context context, ArrayList<faskTask> mDate) {
		this.context = context;
		this.mDate = mDate;
	}
	@Override
	public int getCount() {
		return mDate.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item_task, null);
			viewHold = new ViewHold();
			viewHold.TvAppName= (TextView) convertView.findViewById(R.id.tv_name);
			viewHold.TvTip= (TextView) convertView.findViewById(R.id.tv_tip);
			viewHold.TvLeftNum= (TextView) convertView.findViewById(R.id.tv_left_num);
			viewHold.TvPrice= (TextView) convertView.findViewById(R.id.tv_price);
			viewHold.ImgIcon= (ImageView) convertView.findViewById(R.id.img_icon);
			viewHold.ImgState= (ImageView) convertView.findViewById(R.id.img_state);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
		viewHold.TvAppName.setText(mDate.get(position).getAppname());
		viewHold.TvTip.setText(mDate.get(position).getPrompt());
		viewHold.TvLeftNum.setText("剩下"+mDate.get(position).getNowAmount()+"份");
		viewHold.TvPrice.setText("￥"+mDate.get(position).getPrice());
		UILUtils.displayImage(mDate.get(position).getApplyIcon(),viewHold.ImgIcon);
//		if(!Utis.checkApkExist(context, mDate.get(position).getsBandleID())){
//			HashMap<String,String> map=new HashMap<String, String>();
//			map.put("sBandleID",""+mDate.get(position).getsBandleID());
//			map.put("userid",""+ SharePre.getUserId(context));
//			Log.i("检查是否可做",""+mDate.get(position).getsBandleID()+ SharePre.getUserId(context));
//			OkHttpUtil.getInstance().Post(map, constance.URL.IS_DONE, new OkHttpUtil.FinishListener() {
//				@Override
//				public void Successfully(boolean IsSuccess, String data, String Msg) {
//					Log.i("检查是否可做返回结果",""+data.toString());
//					if(IsSuccess){
//						Result result = GsonUtils.parseJSON(data, Result.class);
//						if(result.getRun().equals("0")){
//						   //可以做任务
//							viewHold.ImgState.setVisibility(View.GONE);
//						}else if(result.getRun().equals("1")){
//							//已错过此任务
//							viewHold.ImgState.setVisibility(View.VISIBLE);
//						}
//					}else {
//					}
//				}
//			});
//		}else {
////			Toast("抱歉，您已完成此任务");
//			viewHold.ImgState.setVisibility(View.VISIBLE);
//	}
		return convertView;
	}
	class ViewHold{
		ImageView ImgState;
		TextView TvAppName;
		TextView TvTip;
		TextView TvLeftNum;
		TextView TvPrice;
		ImageView ImgIcon;
	}
}
