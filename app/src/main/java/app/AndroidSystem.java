package app;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by lss on 2016/7/23.
 */
public class AndroidSystem {
    private AndroidSystem(){}
    private static AndroidSystem instance;
    public synchronized static AndroidSystem getInstance() {
        if( instance == null ) {
            instance = new AndroidSystem();
        }
        return instance;
    }
    /**
     * 显示软键盘
     */
    public void showSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager)
                BaseApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }
}
