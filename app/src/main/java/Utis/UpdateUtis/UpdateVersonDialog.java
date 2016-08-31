package Utis.UpdateUtis;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chuanqi.yz.R;

public class UpdateVersonDialog extends DialogFragment implements OnClickListener{
	private onupdateversion confirmupdate;
	public UpdateVersonDialog(){
		
	}
	public interface  onupdateversion{
		void onupdate();
	}
	public void setonupdateversion(onupdateversion mconfirmupdate)
	{
		confirmupdate=mconfirmupdate;
	}
	public static UpdateVersonDialog TransferInfo (String message)
	{
		UpdateVersonDialog dialog = new UpdateVersonDialog();
		Bundle bundle=new Bundle();
		bundle.putString("Info", message);
		dialog.setArguments(bundle);
		return dialog;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments()!=null)
		{
			message = getArguments().getString("Info");
		}
	}
	private Button btn_qd;
	private Button btn_cancle;
	private String message;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(STYLE_NO_TITLE);//设置没有标题
		View view=inflater.inflate(R.layout.version_dialog, null);
		TextView tv_version=(TextView) view.findViewById(R.id.tv_version);
		tv_version.setText(message);
		btn_qd = (Button) view.findViewById(R.id.btn_qd);
		btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		btn_qd.setOnClickListener(this);
		btn_cancle.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_qd:
			confirmupdate.onupdate();
			getDialog().dismiss();
			break;
		case R.id.btn_cancle:
			getDialog().dismiss();
			break;
		default:
			break;
		}
	}
}
