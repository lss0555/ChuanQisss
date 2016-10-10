package Fragments.Share;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.chuanqi.yz.R;
import java.util.HashMap;
import Constance.constance;
import Fragments.BaseFragment;
import Utis.GsonUtils;
import Utis.OkHttpUtil;
import Utis.SharePre;
import model.ShaiDan;
/**
 * A simple {@link Fragment} subclass.
 */
public class ShowOrderShareFragment extends BaseFragment {

    private TextView mTvAllTaskNum;
    private TextView mTvAllTudiNum;
    private TextView mTvTudiGet;
    private TextView mTvAllGet;
    public ShowOrderShareFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_show_order_share,null);
        initview(layout);
        getDate();
        return layout;
    }
    private void getDate() {
        startProgressDialog("加载中...");
        HashMap<String,String> map=new HashMap<>();
        map.put("userid",""+ SharePre.getUserId(getActivity()));
        OkHttpUtil.getInstance().Post(map, constance.URL.SHAI_DAN, new OkHttpUtil.FinishListener() {
            @Override
            public void Successfully(boolean IsSuccess, String data, String Msg) {
//                showTip(data.toString());
                Log.i("晒单",""+data.toString());
                stopProgressDialog();
                if(IsSuccess){
                    ShaiDan shaiDan = GsonUtils.parseJSON(data, ShaiDan.class);
                    if(shaiDan.getUserappcount()==null){
                        mTvAllTaskNum.setText("0个");
                    }else {
                        mTvAllTaskNum.setText(""+shaiDan.getUserappcount()+"个");
                    }
                    if(shaiDan.getTudicount()==null){
                        mTvAllTudiNum.setText("0个");
                    }else {
                        mTvAllTudiNum.setText(""+shaiDan.getTudicount()+"个");
                    }
                    if(shaiDan.getTudisy()==null){
                        mTvTudiGet.setText("0元");
                    }else {
                        mTvTudiGet.setText(""+shaiDan.getTudisy()+"元");
                    }
                    if(shaiDan.getAllsy()==null){
                        mTvAllGet.setText("0元");
                    }else {
                        mTvAllGet.setText(""+shaiDan.getAllsy()+"元");
                    }
                }else {
                    Toast(data.toString());
                }
            }
        });
    }
    private void initview(View layout) {
        mTvAllTaskNum = (TextView) layout.findViewById(R.id.tv_all_task_num);
        mTvAllTudiNum = (TextView) layout.findViewById(R.id.tv_all_tudi_num);
        mTvTudiGet = (TextView) layout.findViewById(R.id.tv_tudi_get);
        mTvAllGet = (TextView) layout.findViewById(R.id.tv_all_get);
    }
}
