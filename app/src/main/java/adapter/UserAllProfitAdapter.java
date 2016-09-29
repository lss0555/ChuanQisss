package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;

import Utis.UILUtils;
import model.AllProfit.allprofit;
import model.RedRecord.recordList;

public class UserAllProfitAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<allprofit> mDate;
	private ViewHold viewHold;
	public UserAllProfitAdapter(Context context, ArrayList<allprofit> mDate) {
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
			convertView=View.inflate(context, R.layout.item_list_allprofit, null);
			viewHold = new ViewHold();
			viewHold.ImgIcon= (ImageView) convertView.findViewById(R.id.img);
			viewHold.TvPrice= (TextView) convertView.findViewById(R.id.tv_price);
			viewHold.TvName= (TextView) convertView.findViewById(R.id.tv_name);
			viewHold.TvTime= (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
		   viewHold.TvPrice.setText("+"+mDate.get(position).getPrice()+"元");
		   viewHold.TvTime.setText(""+mDate.get(position).getdTime());
		   viewHold.TvName.setText(""+mDate.get(position).getApplyName());
//			UILUtils.displayImageNoAnim(mDate.get(position).getApplyIcon(),viewHold.ImgIcon);
		return convertView;
	}
	class ViewHold{
		ImageView ImgIcon;
		TextView TvName;
		TextView TvPrice;
		TextView TvTime;
	}
}
