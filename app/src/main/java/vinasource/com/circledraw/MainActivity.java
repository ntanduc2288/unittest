package vinasource.com.circledraw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnProgressListener {

    CircleView circleView;
    float value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleView = (CircleView) findViewById(R.id.circleView);

        findViewById(R.id.button_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        circleView.setOnCircleTouchListener(this);
    }

    @Override
    public void singleTouch() {
        Log.d(MainActivity.class.getSimpleName(), "Start Take picture");
    }

    @Override
    public void startLongTouch() {
        Log.d(MainActivity.class.getSimpleName(), "Start recording");
    }

    @Override
    public void stopLongTouch() {
        Log.d(MainActivity.class.getSimpleName(), "Stop recording");
    }

    @Override
    public void progressIsReset() {

    }
}
