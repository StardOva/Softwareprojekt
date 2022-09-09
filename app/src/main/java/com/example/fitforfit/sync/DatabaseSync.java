package com.example.fitforfit.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.fitforfit.singleton.Database;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseSync extends AppCompatActivity {

    public static void uploadDB(Context context) {
        Log.d("UPLOAD", "Datei hochladen...");
        File dbFile = new File(Database.BACKUP_PATH);
        String path = dbFile.getAbsolutePath();

        // API erwartet die Datei im Feld db_file
        String attachmentName = "db_file";
        String attachmentFileName = dbFile.getName();
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            byte[] bytesData = Files.readAllBytes(Paths.get(path));

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

            //request.write(bytesData);

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







            /*
            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            writer.write(Arrays.toString(bytesData));
            writer.flush();
            writer.close();
            out.close();

            urlConnection.disconnect();

             */
        } catch (Exception e) {
            Log.e("abc", e.getMessage());
        }
    }

    public static void downloadDB(Context context) {

        File dbFile = new File(context.getCacheDir(), Database.DUMP_NAME);

        try {
            URL url = new URL(getBackupUrl(context));
            Log.i("abc", "Deine hässliche Mutter");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("abc", "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage());

                return;
            }

            InputStream input = connection.getInputStream();
            OutputStream output = new FileOutputStream(dbFile);

            byte[] data = new byte[4096];
            int count;

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.close();
            input.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static String getBackupUrl(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String backupUrl = sharedPrefs.getString("backup_url", "localhost");
        String apiKey = sharedPrefs.getString("api_key", "");

        return backupUrl + apiKey;
    }
}



