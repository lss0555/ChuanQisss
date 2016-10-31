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
import Views.CircleImageView;
import model.FaskTask.faskTask;
import model.Master.tudiList;

public class ApprenticeAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<tudiList> mDate;
	private ViewHold viewHold;
	public ApprenticeAdapter(Context context, ArrayList<tudiList> mDate) {
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
			convertView=View.inflate(context, R.layout.item_list_tudidetail, null);
			viewHold = new ViewHold();
			viewHold.TvUser= (TextView) convertView.findViewById(R.id.tv_user);
			viewHold.TvPay= (TextView) convertView.findViewById(R.id.tv_pay);
			viewHold.ImgHead= (CircleImageView) convertView.findViewById(R.id.img_head);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
			viewHold.TvUser.setText(mDate.get(position).getApprenticeId());
			viewHold.TvPay.setText("+"+mDate.get(position).getPay()+"元");
		UILUtils.displayImageNoAnim(mDate.get(position).getHeadportrait(),viewHold.ImgHead);
		return convertView;
	}
	class ViewHold{
		CircleImageView ImgHead;
		TextView TvUser;
		TextView TvPay;
	}
}
