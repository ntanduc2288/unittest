package vinasource.com.circledraw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2/2/16.
 */
public class CircleView extends View {
    private RectF rectF;
    Paint paint, paint2, paintCircle, paintClear, paintBg, paintText;
    private RectF rectF2;
    boolean isStartRecording = false;
    private int positionCircleX;
    private int radiusCircle;
    private OnProgressListener circleTouchListener;

    private static final int MAX_CLICK_DURATION = 300;
    private long startClickTime;
    private long clickDuration = 0;
    Handler handler = new Handler();
    Timer timer;

    private static int UPDATE_PROGRESS_INTERVAL = 10; //millisecond
    private static int CIRCLE_DEGREE = 360;
    private int maxTimeToFillFullCircle = 10 * 1000; //Max time to fill all progress
    private float rangeDegree = 0;

    public CircleView(Context context) {
        super(context);
//        initRect();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initRect();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initRect();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Draw bg
        canvas.drawCircle(positionCircleX, positionCircleX, getMeasuredWidth() / 2, paintBg);

        //Draw circle
        canvas.drawArc(rectF, 0, CIRCLE_DEGREE, false, paint);

        //Draw progress
        canvas.drawArc(rectF2, 270, value, false, paint2);

        if(isStartRecording){
            Log.d(CircleView.class.getSimpleName(), "draw circle view");
            canvas.drawCircle(positionCircleX, positionCircleX, radiusCircle, paintCircle);
        }else {
            Log.d(CircleView.class.getSimpleName(), "Clear circle view");
            canvas.drawCircle(positionCircleX, positionCircleX, radiusCircle, paintClear);
        }

        //Draw percent
        canvas.drawText(String.valueOf((int)(value / CIRCLE_DEGREE * 100)) + " %", getMeasuredWidth() / 2, getMeasuredWidth() / 2, paintText);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        checkClickEvent();
                        break;
                    case MotionEvent.ACTION_UP:
                        long duration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if(duration < MAX_CLICK_DURATION){
                            Log.d(MainActivity.class.getSimpleName(), "Remove checkClickEvent() callbacks");
                            handler.removeCallbacks(clickEventRunnable);
                           singleTouch();
                        }else {
                            notifyStopRecording();
                        }
                        break;
                }
                return true;
    }

    public void setOnCircleTouchListener(OnProgressListener onCircleTouchListener){
        this.circleTouchListener = onCircleTouchListener;
    }

    private void updateRangeDegree(){
        rangeDegree = ((float)UPDATE_PROGRESS_INTERVAL * CIRCLE_DEGREE) / maxTimeToFillFullCircle;
    }

    /**
     *
     * @param maxTime: millisecond
     */
    private void setMaxTimeToFillFullCircle(int maxTime){
        maxTimeToFillFullCircle = maxTime;
        updateRangeDegree();
    }

    private void initData(){
        updateRangeDegree();
        initRect();
    }

    private void initRect(){
        int strokeWidth = 50;
        int diameter = getMeasuredWidth() - strokeWidth;
        int leftPosition = getMeasuredWidth() / 2 - diameter/2 ;
        positionCircleX = getMeasuredWidth() / 2;
        radiusCircle = diameter / 2  - strokeWidth * 2;
        rectF = new RectF(leftPosition,leftPosition, diameter + leftPosition , diameter + leftPosition);
        paint = new Paint();
        paint.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        rectF2 = new RectF(leftPosition,leftPosition, diameter + leftPosition , diameter + leftPosition);

        paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setStrokeWidth((int) (strokeWidth));
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setFlags(Paint.ANTI_ALIAS_FLAG);


        paintCircle = new Paint();
        paintCircle.setColor(Color.RED);
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setFlags(Paint.ANTI_ALIAS_FLAG);

        paintClear = new Paint();
        paintClear.setColor(Color.TRANSPARENT);
        paintClear.setStyle(Paint.Style.FILL);
        paintClear.setFlags(Paint.ANTI_ALIAS_FLAG);

        paintBg = new Paint();
        paintBg.setColor(Color.GRAY);
        paintBg.setStyle(Paint.Style.FILL);
        paintBg.setFlags(Paint.ANTI_ALIAS_FLAG);

        paintText = new Paint();
        paintText.setColor(Color.YELLOW);
        paintText.setTextSize(35);
    }

    float value = 0;
    public void updateProgress(float value){
        this.value = value;
        isStartRecording = true;
        invalidate();
    }

    public void stopLongTouch(){
        if(circleTouchListener != null){
            circleTouchListener.stopLongTouch();
        }
        value = 0;
        isStartRecording = false;
        invalidate();
    }

    private void startLongTouch(){
        if(circleTouchListener != null){
            circleTouchListener.startLongTouch();
        }


    }

    private void singleTouch(){

        if(circleTouchListener != null){
            circleTouchListener.singleTouch();
        }
    }

    private void checkClickEvent(){
        handler.postDelayed(clickEventRunnable, MAX_CLICK_DURATION);
    }

    private Runnable clickEventRunnable = new Runnable() {
        @Override
        public void run() {
            clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
            if(clickDuration < MAX_CLICK_DURATION){
                singleTouch();
            }else {
                notifyStartProgress();
            }
        }
    };



    private void notifyStartProgress(){

        if(timer != null){
            timer.cancel();
        }
        startLongTouch();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                checkToUpdateProgress();
            }
        }, 100, UPDATE_PROGRESS_INTERVAL);
    }

    private void notifyStopRecording(){

        if(timer != null){
            timer.cancel();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLongTouch();
                    }
                });

            }
        }, 100);
    }


    private void checkToUpdateProgress(){
        if (value >= CIRCLE_DEGREE) {
//            if(circleTouchListener != null){
//                circleTouchListener.stopLongTouch();
//            }
//
//            value = 0;
            notifyStopRecording();
        }

        value += rangeDegree;
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateProgress(value);
            }
        });

    }
}
