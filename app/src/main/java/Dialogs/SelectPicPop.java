package Dialogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

import com.chuanqi.yz.R;

/**
 */
public class SelectPicPop extends PopupWindow{
    private final View view;
    private Button mBtnTake;
    private OnSelecListner listner;
    private Button mBtnCancle;
    private Button mBtnSave;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public SelectPicPop(Context context){
        view = View.inflate(context, R.layout.item_img_select_pop, null);
            view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.fade_ins));
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            initViews();
            initEvent();
            update();

    }
    public static SelectPicPop getInstance(Context context) {
        if( instance == null ) instance = new SelectPicPop(context);
        return instance;
    }
    private static SelectPicPop instance;
    public void ShowPop(){
            showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    /**
     * 查找控价
     */
    private void initViews() {
        mBtnSave = (Button) view.findViewById(R.id.btn_save);
        mBtnCancle = (Button) view.findViewById(R.id.btn_cancle);
    }

    /**
     * 事件处理
     */
    private void initEvent() {
        /**
         * 保存图片
         */
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                    listner.onSelect("Save");
                }
                dismiss();
            }
        });
        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public  interface  OnSelecListner{
         void onSelect(String Type);
    }
    public void setOnSelectPicListner(OnSelecListner listner){
        this.listner = listner;
    }
}
