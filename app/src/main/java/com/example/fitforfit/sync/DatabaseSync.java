package com.example.fitforfit.sync;

import android.content.Context;



import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

public class DatabaseSync {

    private static void uploadDB(Context context) {
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


    }

}



