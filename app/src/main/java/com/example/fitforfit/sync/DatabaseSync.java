package com.example.fitforfit.sync;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.fitforfit.singleton.Database;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatabaseSync extends AppCompatActivity {

    public static void uploadDB(Context context) {
        Log.d("UPLOAD", "Datei hochladen...");
        File dbFile = new File(Database.BACKUP_PATH);

        // API erwartet die Datei im Feld db_file
        String attachmentName = "db_file";
        String attachmentFileName = dbFile.getName();
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            URL url = new URL(getBackupUrl(context));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Cache-Control", "no-cache");
            urlConnection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(
                    urlConnection.getOutputStream());

            outputStream.writeBytes(twoHyphens + boundary + crlf);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
            outputStream.writeBytes(crlf);

            FileInputStream inputStream = new FileInputStream(dbFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();

            outputStream.writeBytes(crlf);
            outputStream.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);

            outputStream.flush();
            outputStream.close();

            InputStream responseStream = new
                    BufferedInputStream(urlConnection.getInputStream());
            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();

            Log.d("abc", response);

            responseStream.close();
            urlConnection.disconnect();

        } catch (Exception e) {
            Log.e("abc", e.getMessage());
        }
    }

    public static File downloadDB(Context context) {

        File dbFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Database.DUMP_NAME);

        // aufräumen bevor es losgeht
        if (dbFile.exists()) {
            dbFile.delete();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getBackupUrl(context)));

        // Save the file in the "Downloads" folder of SDCARD
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, dbFile.getName());

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        return dbFile;
    }

    public static String getBackupUrl(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String backupUrl = sharedPrefs.getString("backup_url", "localhost");
        String apiKey = sharedPrefs.getString("api_key", "");

        return backupUrl + apiKey;
    }


}



