package Fragments.JingDianTask;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chuanqi.yz.R;

import Fragments.BaseFragment;

/**
 */
public class ZhuanShuFragment extends BaseFragment {


    public ZhuanShuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zhuan_shu, container, false);
    }

}
