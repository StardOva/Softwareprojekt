package com.example.fitforfit.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

public class DatabaseSync extends AppCompatActivity {
    // die Zeit aus den Shared Preferences auslesen
    // SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    // String urlString = sharedPrefs.getString("backup", "localhost");
    //String urlString = "localhost";

    static String dbName = "com.example.fitforfit.database.AppDatabase";
    static String filePath = "/path/to/file";




    public static void uploadDB(Context context, String urlString) {
        Log.d("UPLOAD", "Datei hochladen...");
        File dbFile = context.getDatabasePath(dbName);
        String path = dbFile.getAbsolutePath();

        OutputStream out = null;

        // file to byte[], Path
        try {
            byte[] bytesData = Files.readAllBytes(Paths.get(path));
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                out = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(String.valueOf(bytesData));
                writer.flush();
                writer.close();
                out.close();

                urlConnection.connect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void downloadDB(Context context, String urlString) {

        File dbFile = context.getDatabasePath(dbName);
        String path = dbFile.getAbsolutePath();
        String data = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                data = data + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //codeText.setText("SEITE NICHT ERREICHBAR");
            //error = "SEITE NICHT ERREICHBAR";
            //startErrorActivity(error);
            //Intent mir error String an ErrorScanActivity
        } catch (IOException e) {
            e.printStackTrace();
            //codeText.setText("KEINE NETZWERKVERBINDUNG");
            //error = "KEINE NETZWERKVERBINDUNG";
            //startErrorActivity(error);
        }
        writeToFile(data, context);
    }



    private static void writeToFile(String data, Context context) {

        File dbFile = context.getDatabasePath(dbName);
        String path = dbFile.getAbsolutePath();

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(path, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    /*
    private static void downloadDB(Context context) {
        String urlString = "localhost";
        String dbName = "com.example.fitforfit.database.AppDatabase";
        String filePath = "/path/to/file";

        File dbFile = context.getDatabasePath(dbName);
        String path = dbFile.getAbsolutePath();

        OutputStream out = null;

        // file to byte[], Path
        try {
            byte[] bytesData = Files.readAllBytes(Paths.get(path));
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                out = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(String.valueOf(bytesData));
                writer.flush();
                writer.close();
                out.close();

                urlConnection.connect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/

}



