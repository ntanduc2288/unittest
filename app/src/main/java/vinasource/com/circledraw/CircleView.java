package vinasource.com.circledraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2/2/16.
 */
public class CircleView extends View {
    private RectF rectF;
    Paint paint;

    public CircleView(Context context) {
        super(context);
        initRect();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRect();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRect();
    }

    private void initRect(){
        rectF = new RectF(0,0, 200, 200);
        paint = new Paint();
        paint.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawArc(rectF, 0, -270, true, paint);
    }
}
