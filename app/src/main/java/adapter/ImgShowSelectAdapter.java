package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanqi.yz.R;

import java.util.ArrayList;

import Utis.Utis;
import Utis.UILUtils;
import Views.CircleImageView;
import model.Photos;
import model.RedTime;

public class ImgShowSelectAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Photos> mDate;
	private ViewHold viewHold;
	private onchangeStateLister l;
	private  int pos;
	public ImgShowSelectAdapter(Context context, ArrayList<Photos> mDate) {
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
			convertView=View.inflate(context, R.layout.item_img_show, null);
			viewHold = new ViewHold();
			viewHold.Img= (CircleImageView) convertView.findViewById(R.id.img);
			viewHold.Img1= (CircleImageView) convertView.findViewById(R.id.img1);
			viewHold.FlImg= (FrameLayout) convertView.findViewById(R.id.fl_img);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
		UILUtils.displayImageNoAnim(mDate.get(position).getImgUrl(),viewHold.Img);
		UILUtils.displayImageNoAnim(mDate.get(position).getImgUrl(),viewHold.Img1);
		if(position == pos){
			convertView.setSelected(true);
			viewHold.FlImg.setVisibility(View.GONE);
			viewHold.Img1.setVisibility(View.VISIBLE);
		}else{
			viewHold.Img1.setVisibility(View.GONE);
			viewHold.FlImg.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	class ViewHold{
		CircleImageView Img;
		CircleImageView Img1;
		FrameLayout FlImg;
	}
	public  void  setSelectState(int i){
			pos=i;
	}
	public  interface  onchangeStateLister{
		public  void  ChangeState(int position);
	}
	public  void  setonChangeStateLister(onchangeStateLister l){
		this.l = l;
	}
}
