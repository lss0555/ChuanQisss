package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;

import Utis.UILUtils;
import Utis.Utis;
import model.FaskTask.faskTask;
import model.RedTime;

public class RedTimeAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<RedTime> mDate;
	private ViewHold viewHold;
	private onchangeStateLister l;
	private  int pos;
	public RedTimeAdapter(Context context, ArrayList<RedTime> mDate) {
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
			convertView=View.inflate(context, R.layout.item_select_red_time, null);
			viewHold = new ViewHold();
			viewHold.TvTime= (TextView) convertView.findViewById(R.id.tv_time);
			viewHold.TvState= (TextView) convertView.findViewById(R.id.tv_state);
			viewHold.LlRedRunning= (LinearLayout) convertView.findViewById(R.id.ll_red_running);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}

		String time=mDate.get(position).getTime().substring(0,2);
			viewHold.TvTime.setText(mDate.get(position).getTime()+"");
		String ThisHours=Utis.getHours();
			if(Integer.parseInt(time)<Integer.parseInt(ThisHours)){
				viewHold.TvState.setText("已抢光");
				viewHold.LlRedRunning.setBackground(context.getResources().getDrawable(R.mipmap.red_un_running));
			}else if(Integer.parseInt(time)==Integer.parseInt(ThisHours)){
				viewHold.TvState.setText("进行中");
				viewHold.LlRedRunning.setBackground(context.getResources().getDrawable(R.mipmap.red_running));
			}else {
				viewHold.LlRedRunning.setBackground(context.getResources().getDrawable(R.mipmap.red_un_running));
				viewHold.TvState.setText("即将开始");
			}
		if(position == pos){
			convertView.setSelected(true);
			viewHold.TvTime.setTextColor(Color.RED);
			viewHold.TvState.setTextColor(Color.RED);
			viewHold.LlRedRunning.setBackground(context.getResources().getDrawable(R.mipmap.red_running));
		}else if(position==13){
			viewHold.LlRedRunning.setBackground(context.getResources().getDrawable(R.mipmap.red_un_running));
			viewHold.TvTime.setTextColor(Color.WHITE);
			viewHold.TvState.setTextColor(Color.WHITE);
		}else{
			viewHold.LlRedRunning.setBackground(context.getResources().getDrawable(R.mipmap.red_un_running));
			convertView.setSelected(false);
			viewHold.TvTime.setTextColor(Color.BLACK);
			viewHold.TvState.setTextColor(Color.BLACK);
		}
		return convertView;
	}
	class ViewHold{
		TextView TvTime;
		TextView TvState;
		LinearLayout LlRedRunning;
	}
	public  void  setRedState(int i){
			pos=i;
	}
	public  interface  onchangeStateLister{
		public  void  ChangeState(int position);
	}
	public  void  setonChangeStateLister(onchangeStateLister l){
		this.l = l;
	}
}
