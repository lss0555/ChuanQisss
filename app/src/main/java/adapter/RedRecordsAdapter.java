package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;

import model.Master.tudiList;
import model.RedRecord.record;
import model.RedRecord.recordList;

public class RedRecordsAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<recordList> mDate;
	private ViewHold viewHold;
	public RedRecordsAdapter(Context context, ArrayList<recordList> mDate) {
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
			convertView=View.inflate(context, R.layout.item_list_red_record, null);
			viewHold = new ViewHold();
			viewHold.TvPrice= (TextView) convertView.findViewById(R.id.tv_price);
			viewHold.TvTime= (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
		   viewHold.TvPrice.setText(""+mDate.get(position).getJine());
		   viewHold.TvTime.setText(""+mDate.get(position).getDtime());
		return convertView;
	}
	class ViewHold{
		TextView TvPrice;
		TextView TvTime;
	}
}
