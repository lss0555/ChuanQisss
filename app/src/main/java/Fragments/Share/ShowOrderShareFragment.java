package Fragments.Share;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chuanqi.yz.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class ShowOrderShareFragment extends Fragment {

    public ShowOrderShareFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.item_opening,null);
        initview();
        return layout;
    }
    private void initview() {
    }
}
