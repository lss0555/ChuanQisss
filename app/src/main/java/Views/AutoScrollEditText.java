package Views;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import app.AndroidSystem;

/**
 * (垂直滚动)
 *
 * @author lss
 */
public class  AutoScrollEditText extends EditText {

    String[] items;
    boolean isScrollAble = false;
    private int index = 0;
    private Paint mPaint;
    private float y; // 第一行y轴位置,相对于该view
    private float x; // x轴中间
    private int scrollTime = 100; // 滚动时间 ms
    private boolean isHalt = true;
    private int haltDelay = 2500; // 停顿间隔 ms

    public AutoScrollEditText(Context context) {
        super(context);
        init();
    }

    public AutoScrollEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoScrollEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setScrollItem(String... strings) {
        this.items = strings;
    }

    public void setScrollAble(boolean b) {
        this.isScrollAble = b;
    }

    private void init() {
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(getTextSize());
        mPaint.setColor(getCurrentHintTextColor());
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (updateThread) {
            // 显示滚动文字
            if ( isScrollAble && hasScrollItem() && index>-1 && !isFocused() ) {
                canvas.translate(getScrollX(), 0);
                canvas.drawText(currentScrollItem(), x, y, mPaint);
            }
        }
    }
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(focused) {
            if(getText().toString().isEmpty() && hasScrollItem() && isScrollAble) {
                setHint(getHint());
                AndroidSystem.getInstance().showSoftInput(this);//setHint方法会导致软键盘出不来
            }
            isScrollAble = false;
        } else {
            isScrollAble = getText().toString().isEmpty();
        }
        if( isScrollAble ) {
            startScroll();
        } else pauseScroll();
    }
    private String currentScrollItem() {
        synchronized (updateThread) {
            if(index < 0 || index >= items.length) index = 0;
            return items[index];
        }
    }

    private String nextScrollItem() {
        synchronized (updateThread) {
            index++;
            if( index >= items.length ) {
                index = 0;
            }
            return items[index];
        }
    }

    public boolean hasScrollItem() {
        return items != null && items.length > 0;
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        setHint(null);
        isScrollAble = true;
        if( getText().toString().isEmpty() && hasScrollItem() ) {
            if( updateThread.isAlive() ) {
                updateThreadPause = false;
                synchronized (updateThread) {
                    updateThread.notify(); // 唤醒
                }
            }
            else updateThread.start();
        }
    }

    public void pauseScroll() {
        updateThreadPause = true;
        isScrollAble = false;
    }

    public String getHint() {
        if( hasScrollItem() ) return currentScrollItem();
        else return super.getHint() + "";
    }

    private boolean updateThreadPause = false;

    private final Thread updateThread = new Thread() {
        public void run() {
            int updateDelay = 100;
            int speed;
            while (true) {
                try {
                    synchronized (this) {
                        x = getWidth() / 2;
                        // 停顿
                        if(isHalt) {
                            y = (int) ((getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
                            nextScrollItem();
                            updateDelay = haltDelay;
                            isHalt = false;
                        } else {
                            // 滚动
                            speed = (int) ((getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2)) * 10 / scrollTime; // 每10毫秒滚动的距离
                            speed = speed > 0 ? speed : 1;
                            y = y - speed;
                            if( y - speed - (mPaint.descent() + mPaint.ascent()) < 0  ) isHalt = true;
                            updateDelay = scrollTime/speed;
                        }
                        postInvalidate();

                        wait(updateDelay);
                        if(updateThreadPause) wait();
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    };

}