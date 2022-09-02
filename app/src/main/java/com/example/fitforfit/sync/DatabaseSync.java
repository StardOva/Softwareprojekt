package com.example.fitforfit.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.fitforfit.singleton.Database;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.Arrays;

public class DatabaseSync extends AppCompatActivity {
    // die Zeit aus den Shared Preferences auslesen
    // SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    // String urlString = sharedPrefs.getString("backup", "localhost");
    //String urlString = "localhost";
    static String filePath = "/path/to/file";


    public static void uploadDB(Context context, String urlString) {
        Log.d("UPLOAD", "Datei hochladen...");
        File dbFile = context.getDatabasePath(Database.DB_NAME);
        String path = dbFile.getAbsolutePath();

        OutputStream out;

        String attachmentName = "db";
        String attachmentFileName = "database.db";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            byte[] bytesData = Files.readAllBytes(Paths.get(path));
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true);

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("Cache-Control", "no-cache");
                urlConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);


                DataOutputStream request = new DataOutputStream(
                        urlConnection.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);

                request.write(bytesData);

                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary +
                        twoHyphens + crlf);

                request.flush();
                request.close();

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
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadDB(Context context, String urlString) {

        File dbFile = context.getDatabasePath(Database.DB_NAME);
        String path = dbFile.getAbsolutePath();
        StringBuilder data = new StringBuilder();

        try {
            URL url = new URL(urlString);
            Log.i("abc", "Deine hässliche Mutter");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                Log.i("abc", "Schreibe Daten " + line);

                data.append(line);
            }

            if (!data.toString().isEmpty()) {
                writeToFile(data.toString(), context);
            } else {
                Toast.makeText(context, "Heruntergeladene Datei ist leer", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            //codeText.setText("SEITE NICHT ERREICHBAR");
            //error = "SEITE NICHT ERREICHBAR";
            //startErrorActivity(error);
            //Intent mir error String an ErrorScanActivity
        }//codeText.setText("KEINE NETZWERKVERBINDUNG");
//error = "KEINE NETZWERKVERBINDUNG";
    }

    private static void writeToFile(String data, Context context) {

        File dbFile = context.getDatabasePath(Database.DB_NAME);
        String path = dbFile.getAbsolutePath();

        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            fos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String getBackupUrl(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String backupUrl = sharedPrefs.getString("backup_url", "localhost");
        String apiKey = sharedPrefs.getString("api_key", "");

        return backupUrl + apiKey;
    }
}



