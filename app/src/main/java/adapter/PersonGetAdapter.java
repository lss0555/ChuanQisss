package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;

import model.FaskTask.task;

public class PersonGetAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<task> mDate;
	private ViewHold viewHold;
	public PersonGetAdapter(Context context, ArrayList<task> mDate) {
		this.context = context;
		this.mDate = mDate;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 18;
//		return mDate.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item_list_get_detail, null);
			viewHold = new ViewHold();
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
		return convertView;
	}
	class ViewHold{
		TextView TvTitle;
		ImageView imgStudy;
	}
}
