package vinasource.com.circledraw;

/**
 * Created by user on 2/4/16.
 */
public interface OnProgressListener {
    public void singleTouch();
    public void startLongTouch();
    public void stopLongTouch();
    public void progressIsReset();
}
