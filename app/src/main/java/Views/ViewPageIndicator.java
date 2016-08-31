package Views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ViewPageIndicator extends View{
	
	private static final int part = 5;
	private static final float cx = 1;
	private static final float cy = 30;
	private static final float radius = 10;
	private Paint paint;
	private Paint paint2;
	private int  offset;
	private int yxx;
	
	public ViewPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint2.setColor(Color.GRAY);
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		yxx = (int) (width/2-(part-1)*radius*1.5);
		for (int i = 0; i < part; i++) {
			canvas.drawCircle(yxx+3*radius*i, cy, radius, paint);
		}
		canvas.drawCircle(yxx+offset, cy, radius, paint2);
	}
	public void  Move(float pecent,int position){
		offset =(int) ((pecent+position)*3*radius);
		invalidate();
	}
}
