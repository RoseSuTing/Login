package mg.studio.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Header;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tvName;
    private SessionManager session;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //The app starts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //The view (layout have been loaded
        tvName = findViewById(R.id.user_name);
        listView = findViewById(R.id.list_view);
        String []data = {"LifeCycle","Username","Username_Final","Layout","Layout_final","Button_design","Button_intent","Button_startActivity","ImageButton ","EditText","RadioButtons_Listener","GetColor","GradientBackground","weather_app","listview","Audiorecorder","database","frangmentone","webview","servierDemo","appprivateDirector","asestsFolers","intenExras"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     switch (position)
                     {
                         case 0:
                             startActivity( new Intent(MainActivity.this,LifeCycleActivity.class));
                             break;
                         case 1:
                             startActivity( new Intent(MainActivity.this,UsernameActivity.class));
                             break;
                         case 2:
                             startActivity( new Intent(MainActivity.this,Username_FinalActivity.class));
                             break;
                         case 3:
                             startActivity( new Intent(MainActivity.this,LayoutActivity.class));
                             break;
                         case 4:
                             startActivity( new Intent(MainActivity.this,Layout_finalActivity.class));
                             break;
                         case 5:
                             startActivity( new Intent(MainActivity.this,Button_designActivity.class));
                             break;
                         case 6:
                             startActivity( new Intent(MainActivity.this,Button_IntentActivity.class));
                             break;
                         case 7:
                             startActivity( new Intent(MainActivity.this,ButtonStartActivityActivity.class));
                             break;
                         case 8:
                             startActivity( new Intent(MainActivity.this,ImageButtonActivity.class));
                             break;
                         case 9:
                             startActivity( new Intent(MainActivity.this,EditTextActivity.class));
                             break;
                         case 10:
                             startActivity( new Intent(MainActivity.this,RadioListenerActivity.class));
                             break;
                         case 11:
                             startActivity( new Intent(MainActivity.this,GetColorActivity.class));
                             break;
                         case 12:
                             startActivity( new Intent(MainActivity.this,GrdientBackgroundActivity.class));
                             break;
                         case 13:
                             startActivity( new Intent(MainActivity.this,Weather_appActivity.class));
                             break;
                         case 14:
                             startActivity( new Intent(MainActivity.this,ListviewActivity.class));
                             break;
                         case 15:
                             startActivity( new Intent(MainActivity.this,AudioRecorderActivity.class));
                             break;
                         case 16:
                             startActivity( new Intent(MainActivity.this,DataBaseActivity.class));
                             break;
                         case 17:
                             startActivity( new Intent(MainActivity.this,FragmentOneActivity.class));
                             break;
                         case 18:
                                 startActivity( new Intent(MainActivity.this,WebviewActivity.class));
                             break;
                         case 19:
                             startActivity( new Intent(MainActivity.this,ServerDemoActivity.class));
                             break;
                         case 20:
                             startActivity( new Intent(MainActivity.this,APPprivateDirecotryActivity.class));
                             break;
                         case 21:
                             startActivity( new Intent(MainActivity.this,AssetsFolderActivity.class));
                             break;
                         case 22:
                             startActivity( new Intent(MainActivity.this,IntentExtraActivity.class));
                             break;
                     }
            }
        });

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logout();
        }


        /**
         * If the user just registered an account from Register.class,
         * the parcelable should be retrieved
         * ok, have fun
         * Check this https://stackoverflow.com/questions/39795122/upload-file-to-server-java-net-protocolexception-cannot-write-request-body-aft
         * ok, read that website, I wil implement the upload like that, I will just create the server side for you, ok?
         * ok
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Retrieve the parcelable
            Feedback feedback = bundle.getParcelable("feedback");
            // Get the from the object
            String userName = feedback.getName();
            tvName.setText(userName);
        }
    }
    /**
     * Logging out the user:
     * - Will set isLoggedIn flag to false in SharedPreferences
     * - Clears the user data from SqLite users table
     */

    public void btnLogout(View view) {
        logout();
    }

    public void logout() {
        // Updating the session
        session.setLogin(false);
        // Redirect the user to the login activity
        startActivity(new Intent(this, Login.class));
        finish();
    }

}