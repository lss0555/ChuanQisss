package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;

import Utis.UILUtils;
import Views.CircleImageView;
import model.RichList;

public class WeekRichListAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<RichList> mDate;
	private ArrayList<String> mRewardType;
	private String type;
	private ViewHold viewHold;
	public WeekRichListAdapter(Context context, ArrayList<RichList> mDate,ArrayList<String> mRewardType) {
		this.context = context;
		this.mDate = mDate;
		this.mRewardType = mRewardType;
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
			convertView=View.inflate(context, R.layout.item_list_day, null);
			viewHold = new ViewHold();
			viewHold.ImgHead= (CircleImageView) convertView.findViewById(R.id.img_head);
			viewHold.TvId= (TextView) convertView.findViewById(R.id.tv_id);
			viewHold.TvPos= (TextView) convertView.findViewById(R.id.tv_pos);
			viewHold.TvTuDiNum= (TextView) convertView.findViewById(R.id.tv_tudi_num);
			viewHold.TvYesIncome= (TextView) convertView.findViewById(R.id.tv_yes_income);
			viewHold.TvType= (TextView) convertView.findViewById(R.id.tv_type);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
		viewHold.TvPos.setText(""+(position+1));
		  viewHold.TvId.setText("ID:"+mDate.get(position).getUserid());
		  viewHold.TvTuDiNum.setText("徒弟："+mDate.get(position).getTdcount()+"名");
		   if(mDate.get(position).getJine()==null ||mDate.get(position).getJine().equals("")){
			   viewHold.TvYesIncome.setText("上周收入:0.00元");
		   }else {
					   viewHold.TvYesIncome.setText("上周收入:"+mDate.get(position).getJine()+"元");
		   }
		viewHold.TvType.setText(""+mRewardType.get(position));
		UILUtils.displayImageNoAnim(mDate.get(position).getHeadportrait(),viewHold.ImgHead);
		return convertView;
	}
	class ViewHold{
		CircleImageView ImgHead;
		TextView TvId;
		TextView TvTuDiNum;
		TextView TvYesIncome;
		TextView TvType;
		TextView TvPos;
	}
}
