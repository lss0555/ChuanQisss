package dialog;

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
public class ShowRedPricePop extends PopupWindow{
    private final View view;
    private Button mBtnTake;
    private Button mBtnSelect;
    private Button mBtnCancle;
    private OnComfirmListner listner;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ShowRedPricePop(Context context){
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
    public static ShowRedPricePop getInstance(Context context) {
        if( instance == null ) instance = new ShowRedPricePop(context);
        return instance;
    }
    private static ShowRedPricePop instance;
    public void ShowPop(){
            showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    /**
     * 查找控价
     */
    private void initViews() {
    }

    /**
     * 事件处理
     */
    private void initEvent() {
        /**
         * 拍照
         */
//        mBtnTake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listner!=null){
//                    listner.onSelect("Take");
//                }
//                dismiss();
//            }
//        });
    }
    public  interface  OnComfirmListner{
         void onComfrim();
    }
    public void setOnSelectPicListner(OnComfirmListner listner){
        this.listner = listner;
    }
}
