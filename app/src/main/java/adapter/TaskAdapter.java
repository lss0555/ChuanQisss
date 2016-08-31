package adapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanqi.yz.R;

import Utis.UILUtils;
import model.FaskTask.faskTask;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item_task, null);
			viewHold = new ViewHold();
			viewHold.TvAppName= (TextView) convertView.findViewById(R.id.tv_name);
			viewHold.TvTip= (TextView) convertView.findViewById(R.id.tv_tip);
			viewHold.TvLeftNum= (TextView) convertView.findViewById(R.id.tv_left_num);
			viewHold.TvPrice= (TextView) convertView.findViewById(R.id.tv_price);
			viewHold.ImgIcon= (ImageView) convertView.findViewById(R.id.img_icon);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
		viewHold.TvAppName.setText(mDate.get(position).getAppname());
		viewHold.TvTip.setText(mDate.get(position).getPrompt());
		viewHold.TvLeftNum.setText("剩下"+mDate.get(position).getNowAmount()+"个任务");
		viewHold.TvPrice.setText("￥"+mDate.get(position).getPrice());
		UILUtils.displayImage(mDate.get(position).getApplyIcon(),viewHold.ImgIcon);
		return convertView;
	}
	class ViewHold{
		TextView TvAppName;
		TextView TvTip;
		TextView TvLeftNum;
		TextView TvPrice;
		ImageView ImgIcon;
	}
}
