package mg.studio.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


public class Login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private Feedback feedback;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /**
         * If the user just registered an account from Register.class,
         * the parcelable should be retrieved (this gets some data from the Register.class, if the re is)
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Retrieve the parcelable
            Feedback feedback = bundle.getParcelable("feedback");
            // Get the from the object
            String userName = feedback.getName();
            TextView display = findViewById(R.id.display);
            display.setVisibility(View.VISIBLE);
            String prompt = userName.substring(0, 1).toUpperCase() + userName.substring(1) + " " + getString(R.string.account_created);
            display.setText(prompt);

        }

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);


        /**
         * Prepare the dialog to display when the login button is pressed
         */
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        /**
         * Use the SessionManager class to check whether
         * the user already logged in, is yest  then go to the MainActivity
         * Read inside the preferences, if the user already logged in before, just redirect into the main
         */
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }



        //检查网络
        if (CheckNet.getNetState(this) == CheckNet.NET_NONE) {
            Log.d("MWeather", "网络不通");
            Toast.makeText(Login.this, "please open Internet and update", Toast.LENGTH_LONG).show();
            loginButton.setEnabled(false);
        } else {
            Log.d("MWeather", "网络ok");
            Toast.makeText(Login.this, "you can login", Toast.LENGTH_LONG).show();
        }


    }
    //定义连接网络的类
    public static class CheckNet {
        final static int NET_NONE = 0;
        final static int NET_WIFI = 1;
        final static int NET_MOBILE = 2;

        static int getNetState(Context context) {

            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null)
                return NET_NONE;
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_MOBILE)
                return NET_MOBILE;
            else if (type == ConnectivityManager.TYPE_WIFI)
                return NET_WIFI;
            return NET_MOBILE;
        }
    }


    /**
     *  Process the user input and log in if credentials are correct
     *  Disable the button while login is processing
     *  @param view from activity_login.xml
     */
    public void btnLogin(View view) {


        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {

            // Avoid multiple clicks on the button
            loginButton.setClickable(false);

            //Todo : ensure the user has Internet connection

            // Display the progress Dialog
            progressDialog.setMessage("Logging in ...");
            if (!progressDialog.isShowing())
                progressDialog.show();

            //Todo: need to check weather the user has Internet before attempting checking the data
            // Start fetching the data from the Internet
            new OnlineCredentialValidation().execute(email,password);


        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    R.string.enter_credentials, Toast.LENGTH_LONG)
                    .show();
        }
    }


    /**
     * Press the button register, go to Registration form
     *
     * @param view from the activity_login.xml
     */
    public void btnRegister(View view) {
        switch (view.getId()) {
            case R.id.btnLinkToRegisterScreen:
            startActivity(new Intent(getApplicationContext(), Register.class));
            finish();
            break;
            case R.id.btn_update:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
    }



    /**
     * Use the email and password provided to log the user in if the credentials are valid
     *
     */



    class OnlineCredentialValidation extends AsyncTask<String, Void, Integer> {
        /**
         * There are two main things which we need:
         * 1-do something in the background, so that we don`t block the UI
         * 2- Get the result after the background task is completed.
       * next question?
         * these are the two we need to
         * The source code for the php?
         * You can just connect your applicaton using the server I created, it is live now
         *
         * @param strings
         * @return
         */

        @Override
        protected Integer doInBackground(String... strings) {
            feedback = new Feedback();

            String response = null;
            OutputStreamWriter request = null;
            int parsingFeedback = feedback.FAIL;


            // Variables
            final String BASE_URL = new Config().getLoginUrl();
            final String EMAIL = "email";
            final String PASSWORD = "password";
            final String PARAMS = EMAIL + "=" + strings[0] + "&" + PASSWORD + "=" + strings[1];
            Log.d("TAG","Email and Pass - "+EMAIL + "=" + strings[0] + "&" + PASSWORD + "=" + strings[1]);

            URL url = null;
            HttpURLConnection connection = null;
            try {
                url = new URL(BASE_URL);
                connection = (HttpURLConnection) url.openConnection();
                //Set the request method to POST
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(15000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(15000);

                // Output the stream to the server
                request = new OutputStreamWriter(connection.getOutputStream());
                request.write(PARAMS);
                request.flush();
                request.close();

                // Get the inputStream using the same connection
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                response = readStream(inputStream, 500);
                inputStream.close();

                // Parsing the response
                parsingFeedback = parsingResponse(response);


            } catch (MalformedURLException e) {
                Log.e("TAG", "URL - " + e);
                feedback.setError_message(e.toString());
                return feedback.FAIL;
            } catch (IOException e) {
                Log.e("TAG", "openConnection() - " + e);
                feedback.setError_message(e.toString());
                return feedback.FAIL;
            } finally {
                if (connection != null) // Make sure the connection is not null before disconnecting
                    connection.disconnect();
                Log.d("TAG", "Response " + response);

                return parsingFeedback;
            }
        }




        @Override
        protected void onPostExecute(Integer mFeedback) {
            super.onPostExecute(mFeedback);
            if (progressDialog.isShowing()) progressDialog.dismiss();

            if (mFeedback == feedback.SUCCESS) {
                // Update the session
                session.setLogin(true);
                // Move the user to MainActivity and pass in the User name which was form the server
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("feedback", feedback);
                startActivity(intent);
            } else {
                // Allow the user to click the button
                loginButton.setClickable(true);
                Toast.makeText(getApplication(), feedback.getError_message(), Toast.LENGTH_SHORT).show();
            }

        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        String readStream(InputStream stream, int maxReadSize)
                throws IOException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] rawBuffer = new char[maxReadSize];
            int readSize;
            StringBuffer buffer = new StringBuffer();
            while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
                if (readSize > maxReadSize) {
                    readSize = maxReadSize;
                }
                buffer.append(rawBuffer, 0, readSize);
                maxReadSize -= readSize;
            }

            Log.d("TAG", buffer.toString());
            return buffer.toString();
        }
    }


    /**
     * Parsing the string response from the Server
     * @param response
     * @return
     */
    public int parsingResponse(String response) {

        try {
            JSONObject jObj = new JSONObject(response);
            /**
             * If the registration on the server was successful the return should be
             * {"error":false}
             * Else, an object for error message is added
             * Example: {"error":true,"error_msg":"Invalid email format."}
             * Success of the registration can be checked based on the
             * object error, where true refers to the existence of an error
             */
            boolean error = jObj.getBoolean("error");

            if (!error) {
                //No error, return from the server was {"error":false}
                JSONObject user = jObj.getJSONObject("user");
                String email = user.getString("email");
                feedback.setName(email);
                return feedback.SUCCESS;
            } else {
                // The return contains error messages
                String errorMsg = jObj.getString("error_msg");
                Log.d("TAG", "errorMsg : " + errorMsg);
                feedback.setError_message(errorMsg);
                return feedback.FAIL;
            }
        } catch (JSONException e) {
            feedback.setError_message(e.toString());
            return feedback.FAIL;
        }

    }
}