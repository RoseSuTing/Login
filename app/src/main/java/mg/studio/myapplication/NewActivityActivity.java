package mg.studio.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NewActivityActivity extends AppCompatActivity {
    private String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString("keyword");
        }

        ((TextView) findViewById(R.id.display)).setText(value);
    }
}
