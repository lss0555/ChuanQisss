package Fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chuanqi.yz.R;
/**
 */
public class RegalListFragment extends BaseFragment {
    public RegalListFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_regal_list,null);
        initview();
        return layout;
    }

    private void initview() {

    }
}
