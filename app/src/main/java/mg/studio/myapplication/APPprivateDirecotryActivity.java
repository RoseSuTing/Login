package mg.studio.myapplication;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class APPprivateDirecotryActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appprivate_direcotry);

        Calendar calendar = Calendar.getInstance();
        String timeStamp /* use as file name */ = String.valueOf(calendar.getTimeInMillis());
        String text_to_save = "Created on " + timeStamp;

        //Save the text_to_save in a file inside the private Directory of the application
        saveFileInPrivateDirectory(this, timeStamp + ".txt", text_to_save);
    }
    public File getPrivateStorageDirectory(Context context, String folderName) {

        // Get the directory for the app's private downloads directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS), folderName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    /**
     * Creates a file, writes in a string and saves it in the private Storage
     * directory of the application. Writing the private directory of the application
     * does not require specific permissions.
     * Location may vary but here is what I have while testing this:
     * /storage/emulated/0/Android/data/mg.studio.writefileexternalstorage/files/Download/directory
     *
     * @param context   the current context
     * @param sFileName the file name to create to save the string
     * @param sBody     string to save in the text file
     */
    public void saveFileInPrivateDirectory(Context context, String sFileName, String sBody) {

        String folderName = "directory";
        try {
            File root = getPrivateStorageDirectory(context, folderName);
            File file = new File(root, sFileName);
            FileWriter writer = new FileWriter(file);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Log.d(LOG_TAG, "Saved: " + root.toString());
        } catch (IOException e) {
            Log.e(LOG_TAG, "saveFileInPrivateDirectory(): " + e);
        }
    }
}
