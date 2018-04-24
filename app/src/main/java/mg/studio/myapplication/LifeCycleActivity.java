package mg.studio.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LifeCycleActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private String previousContent;
    private String currentState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        Log.d(TAG, "onCreate:" + TAG);
        currentState = "onCreate";
        setText();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + TAG);
        currentState = "onResume";
        setText();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: " + TAG);
        currentState = "onRestart";
        setText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + TAG);
        currentState = "onPause";
        setText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + TAG);
        currentState = "onStop";
        setText();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + TAG);
        currentState = "onDestroy";
        setText();
    }

    private void setText() {
        previousContent = ((TextView) findViewById(R.id.display)).getText().toString();

        ((TextView) findViewById(R.id.display)).setText(previousContent + "\n" + currentState);
    }

}
